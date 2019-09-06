package com.sinux.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.server.DeviceServer;

/**
 * 
* <p>Title: RedisSynThread</p>  
* <p>Description: 将redis中缓存的设备数据同步到数据库中</p>  
* @author yexj  
* @date 2019年8月13日
 */
public class RedisSynThread implements Runnable{

	/**日志*/
	private Logger log = LoggerFactory.getLogger(RedisSynThread.class);
	/**设备服务接口*/
	private DeviceServer deviceServer;
	
	public RedisSynThread(DeviceServer deviceServer) {
		this.deviceServer = deviceServer;
	}
	
	@Override
	public void run() {
		try {
			Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
			for(String key : keys) {
				Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
				//获取设备数据
				Object obj = cliMap.get(Constants.DEVICE_TYPE_CACHE_ITEM);
				if(null == obj) {
					continue;
				}
				JSONObject deviceJson = JSON.parseObject(obj.toString());
				Iterator<Entry<String, Object>> iter = deviceJson.entrySet().iterator();
				DeviceInfo di = null;
				while(iter.hasNext()) {
					Entry<String, Object> entry = iter.next();
					String idKey = entry.getKey();
					Object deObj = entry.getValue();
					JSONObject deJson = JSON.parseObject(deObj.toString());
					di = new DeviceInfo();
					di.setId(deJson.getLong("id"));
					di.setDeviceName(deJson.getString("name"));
					di.setDeviceStatus(deJson.getInteger("deviceStatus"));
					di.setDeviceType(deJson.getLong("type"));
					di.setDeviceLongitude(deJson.getDouble("lng"));
					di.setDeviceLatitude(deJson.getDouble("lat"));
					di.setDeviceIp(deJson.getString("ip"));
					//注册客户端直连设备信息
					deviceServer.autoRegisterDevice(idKey, di);
				}
				//注册客户端信息
				int status = Integer.parseInt(cliMap.get("status").toString());
				String ip = cliMap.get("ip").toString();
				di = new DeviceInfo();
				di.setDeviceStatus(status);
				di.setDeviceIp(ip);
				deviceServer.autoRegisterDevice(key.replaceAll(Constants.CLIENT_CACHE_KEY_PRE, ""), di);
				//获取经纬度，现还未实现客户端获取本机设备的经纬度
			}
		}catch (Exception e) {
			log.error("设备缓存同步到数据库时出错", e);
		}
	}

}
