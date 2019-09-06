package com.sinux.thread;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.heart.HeartBox;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.server.ResourceServer;
import com.sinux.modules.server.TaskServer;
import com.sinux.thread.handle.SynThreadHandle;


/**
 * 
 * @ClassName MessageSynThread
 * @Description: 消息同步线程，将缓冲队列中的消息同步到本地缓存中。
 * @author Tangjc
 * @date 2019年6月12日 下午5:19:07
 */
public class MessageSynThread implements Runnable{
	/** 心跳相关信息 */
	private HeartBox heartBox;
	/**任务服务接口*/
	private TaskServer taskServer;
	/**资源服务接口*/
	private ResourceServer resourceServer; 
	/**设备服务接口*/
	private DeviceServer deviceServer;
	/**日志服务*/
	private Logger log = LoggerFactory.getLogger(MessageSynThread.class);
	
	public MessageSynThread(HeartBox heartBox,TaskServer taskServer,ResourceServer resourceServer,DeviceServer deviceServer) {
		this.heartBox = heartBox;
		this.taskServer = taskServer;
		this.resourceServer = resourceServer;
		this.deviceServer = deviceServer;
	}
	@Override
	public void run() {
		// 轮询取同步消息
		while(true){
			try {
				/** 
				 	同步消息 分为三个字段 
					同步数据:data;
					同步数据类型:type -> user,task,resource,device;
				 	同步方式: method -> add update del
				*/
				String data = heartBox.getReceiveQueue().take();
				JSONObject json = JSON.parseObject(data);
				if(StringUtils.equals("user", json.getString("type"))){
					SynThreadHandle.cacheUserData(json, taskServer, resourceServer, deviceServer);
				}
				if(StringUtils.equals("childDevice", json.getString("type"))) {
					SynThreadHandle.cacheDeviceData(json);
				}
			} catch (InterruptedException e) {
				log.error("数据同步到redis缓存出错！",e);
			}
		}
	}
}
