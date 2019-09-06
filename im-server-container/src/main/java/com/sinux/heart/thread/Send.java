package com.sinux.heart.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.sinux.heart.HeartBox;
import com.sinux.heart.constants.Constants;

/**
 * 心跳上报线程类
 * @ClassName Upper
 * @Description: 心跳上报线程类
 * @date 2019年6月10日 下午2:03:37
 */
public class Send implements Runnable{
	/** 连接对象 */
	private DatagramSocket socket;
	/** 心跳信息 */
	private HeartBox heartBox;
	public Send(DatagramSocket ds,HeartBox hb){
		this.socket = ds;
		this.heartBox = hb;
		// 初始化父级消息队列
		heartBox.getMsgQueueMap().put(heartBox.getpIp(), new ArrayBlockingQueue<String>(1000,true));
	}
	@Override
	public void run() {
		// 组装数据
		JSONObject json = new JSONObject();
		String msg = "";
		String jsonStr = "";
		if(heartBox.getAckMap().isEmpty()){
			msg = heartBox.getMsgQueueMap().get(heartBox.getpIp()).poll();
			msg = msg==null?"":msg;
			json.put(Constants.HeartMessage.MSG, msg);
			json.put(Constants.HeartMessage.SIGN, DigestUtils.md5Hex(msg));
			json.put(Constants.HeartMessage.MSGTYPE, 1);
			// 发送数据
			jsonStr = json.toJSONString();
			heartBox.getAckMap().put(json.getString(Constants.HeartMessage.SIGN), jsonStr);
		}else{
			// 如果在取数据时ack接收到消息删除了该消息，无视被删除的消息，继续发送
			jsonStr = heartBox.getAckMap().entrySet().iterator().next().getValue();
		}
		try{
			byte[] bytes = jsonStr.getBytes(Constants.HeartMessage.ENCODING_UTF_8);
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(heartBox.getpIp(), heartBox.getpPort()));
			socket.send(dp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
