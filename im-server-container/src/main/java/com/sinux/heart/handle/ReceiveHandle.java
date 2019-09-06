package com.sinux.heart.handle;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.base.support.common.util.SpringContextUtil;
import com.sinux.heart.HeartBox;
import com.sinux.heart.constants.Constants;
import com.sinux.heart.handle.base.BaseHandle;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.server.impl.DeviceServerImpl;

/**
 * 接收处理类
 * @ClassName ReceiveHandle
 * @Description: 接收处理类
 * @date 2019年6月10日 下午5:36:34
 */
public class ReceiveHandle implements BaseHandle{
	
	/**日志*/
	private Logger log = LoggerFactory.getLogger(ReceiveHandle.class);
	/**
	 * 获取设备管服务接口
	 */
	private DeviceServer deviceServer = SpringContextUtil.getBean(DeviceServerImpl.class);
	
	@Override
	public void deal(String data, HeartBox heartBox) {
		JSONObject json = JSON.parseObject(data);
		//获取客户端ID值
		String id = json.getString("id");
		// 获取完整性校验字段
		String sign = json.getString(Constants.HeartMessage.SIGN);
		// 获取心跳上报时携带的参数信息
		String msg = json.getString(Constants.HeartMessage.MSG);
		// 将携带参数用md5进行加密
		String verifySign = DigestUtils.md5Hex(msg);
		// 获取客户端IP
		String ip = json.getString(Constants.HeartMessage.RECEIVE_IP);
		// 获取客户端端口
		int port = json.getIntValue(Constants.HeartMessage.RECEIVE_PORT);
		//定义是否注册客户端
		boolean isRegistCli = true;
		//查询缓存数据库中客户端信息key为：client_id号
		Object obj = RedisUtil.hashGet(com.sinux.base.support.common.constants.Constants.CLIENT_CACHE_KEY_PRE+id, "ip");
		if(null == obj) {
			//将客户端信息注册到管理服务端缓存数据库中，方便以后查找调用
			R r = deviceServer.getDeviceByHeartIp(ip);
			if(null != r.get("device")) {
				DeviceInfo di = (DeviceInfo)r.get("device");
				//赋值ID
				id = di.getId()+"";
				Map<String,Object> map = new HashMap<String,Object>();
				//注册客户端ip
				map.put("ip", di.getDeviceIp());
				//注册客户端状态
				map.put("status", com.sinux.base.support.common.constants.Constants.DEVICE_ONLINE_STATUS);
				//注册客户端时间
				map.put("registTime", System.currentTimeMillis());
				//将客户端信息保存到缓存数据库中
				RedisUtil.hashSet(com.sinux.base.support.common.constants.Constants.CLIENT_CACHE_KEY_PRE+id, map);
			}else {
				isRegistCli = false;
				log.info("服务端未注册IP为【"+ip+"】的客户端");
			}
		}else {
			//更新客户端的注册时间，防止超时判定为掉线
			RedisUtil.hashSet(com.sinux.base.support.common.constants.Constants.CLIENT_CACHE_KEY_PRE+id, "registTime", System.currentTimeMillis());
		}
		
		// 将加密校验字段与参数加密结果进行对比
		if(StringUtils.equals(sign, verifySign)){
			json.clear();
			try {
				// 取出心跳额外数据 存入本地缓存
				if(StringUtils.isNotEmpty(msg)){
					JSONObject msgJson = JSON.parseObject(msg);
					//将客户端ID与上报信息绑定
					msgJson.put("cliId", id);
					heartBox.getReceiveQueue().put(msgJson.toJSONString());
				}
				// 取出socket
				DatagramSocket socket = heartBox.getSocket();
				//判断是否需要同步数据
				if(heartBox.getMsgQueueMap().containsKey(ip)){
					String synMessage = heartBox.getMsgQueueMap().get(ip).poll();
					//如果不为空，需要向设备同步信息
					if(StringUtils.isNotEmpty(synMessage)){
						json.put(Constants.HeartMessage.MSG, synMessage);
					}
				}else{
					heartBox.getMsgQueueMap().put(ip, new ArrayBlockingQueue<String>(1000, true));
				}
				//将客户端ID同步到客户端上
				json.put("id", id);
				// 返回消息类型，将其设置为ack回执类型
				json.put(Constants.HeartMessage.MSGTYPE, com.sinux.base.support.common.constants.Constants.SYN_MESG_ACK_TYPE);
				json.put(Constants.HeartMessage.SIGN, DigestUtils.md5Hex(json.getString(Constants.HeartMessage.MSG)==null?"":json.getString(Constants.HeartMessage.MSG)));
				// 加入心跳上报时的校验字段回传到客户端，实现ACK捂手机制
				json.put("removeSign", sign);
				//将客户端是否在服务端注册标志发送到客户端
				json.put("isRegistCli", isRegistCli);
				byte[] bytes = json.toJSONString().getBytes(Constants.HeartMessage.ENCODING_UTF_8);
				DatagramPacket dp = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(ip, port));
				socket.send(dp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
