package com.sinux.heart.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.util.ByteUtil;
import com.sinux.base.support.common.util.ReflectUtil;
import com.sinux.heart.HeartBox;
import com.sinux.heart.constants.Constants;

/**
 * 接收线程
 * @ClassName Receive
 * @Description: 接收线程
 * @date 2019年6月10日 下午3:58:34
 */
public class Receive implements Runnable{
	
	private Logger log = LoggerFactory.getLogger(Receive.class);
	/** 连接对象 */
	private DatagramSocket socket;
	/** 心跳相关信息 */
	private HeartBox heartBox;
	/** spring取properties对象 */
	/**
	 * 构造方法
	 * @param socket
	 * @time 2019年6月10日-下午6:08:24
	 * @todo 构造方法
	 */
	public Receive(DatagramSocket socket,HeartBox heartBox){
		this.heartBox = heartBox;
		this.socket = socket;
	}
	@Override
	public void run() {
		byte[] bytes = new byte[1024];
		//创建定长接收数据包
		DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
		while(true){
			try {
				if(socket==null){
					socket = new DatagramSocket(heartBox.getPort());
				}
				//阻塞接收数据包
				socket.receive(dp);
				int len = dp.getLength();
				byte[] resBytes = ByteUtil.getCopyByte(dp.getData(), len);
				String str = new String(resBytes,Constants.HeartMessage.ENCODING_UTF_8);
				// 转化为json
				JSONObject json = JSON.parseObject(str);
				// 获取消息类型 1 发送 2 回执
				String msgType = json.getString(Constants.HeartMessage.MSGTYPE);
				String ip = dp.getAddress().toString();
				log.info("客户端【"+ip+"】同步数据为："+str);
				json.put(Constants.HeartMessage.RECEIVE_IP, ip.substring(1, ip.length()));
				json.put(Constants.HeartMessage.RECEIVE_PORT, dp.getPort());
				json.put("id", json.getString("id"));
				String classPath = HeartBox.getHeartMap().get(msgType);
				ReflectUtil.processMethod(Class.forName(classPath), "deal", json.toJSONString(),heartBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	public static void main(String[] args) {
		while(System.currentTimeMillis()%1000==0){
			System.out.println(System.currentTimeMillis());
		}
	}
}
