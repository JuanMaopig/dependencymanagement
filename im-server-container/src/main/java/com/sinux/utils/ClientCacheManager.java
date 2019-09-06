package com.sinux.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.modules.entity.DeviceInfo;

/**
 * 
* <p>Title: ResourceCacheManager</p>  
* <p>Description: 资源缓存管理类，用户资源缓存操作实现</p>  
* @author yexj  
* @date 2019年8月12日
 */
public class ClientCacheManager {

	/**
	 * 
	 * <p>Title: getCliIpByLoginUserId</p>  
	 * <p>Description: 通过登录用户ID，获取登录客户端IP</p>  
	 * @author yexj  
	 * @date 2019年8月12日  
	 * @param userId 用户ID
	 * @return 返回客户端IP
	 */
	public static String getCliIpByLoginUserId(String userId) {
		Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
		for(String key : keys) {
			Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
			Object obj = cliMap.get(Constants.USER_TYPE_CACHE_ITEM);
			if(null == obj) {
				return "";
			}
			JSONObject userJson = JSON.parseObject(obj.toString());
			Object user = userJson.get(userId);
			//判断该客户端下是否有查找的登录人，如果有则说明登录人在该客户端下。返回客户端IP
			if(null != user) {
				return cliMap.get("ip").toString();
			}
		}
		return "";
	}
	
	/**
	 * 
	 * <p>Title: getCliIpByDeviceId</p>  
	 * <p>Description: 通过设备ID查找客户端IP</p>  
	 * @author yexj  
	 * @date 2019年8月12日  
	 * @param deviceId
	 * @return
	 */
	public static String getCliIpByDeviceId(String deviceId) {
		Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
		for(String key : keys) {
			Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
			Object obj = cliMap.get(Constants.DEVICE_TYPE_CACHE_ITEM);
			if(null == obj) {
				return "";
			}
			JSONObject deviceJson = JSON.parseObject(obj.toString());
			Object device = deviceJson.get(deviceId);
			//判断设备数据中是否包含需要找的设备，如果有则说明该设备在该客户端下。返回客户端IP
			if(null != device) {
				return cliMap.get("ip").toString();
			}
		}
		return "";
	}
	
	/**
	 * 
	 * <p>Title: delDeviceByIds</p>  
	 * <p>Description: 根据ID删除缓存数据</p>  
	 * @author yexj  
	 * @date 2019年8月14日  
	 * @param deviceIds
	 */
	public static Set<String> delDeviceByIds(String...deviceIds) {
		Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
		//客户端下直接关联设备ID集合
		Set<String> childrenIds = new HashSet<>();
		for(String key : keys) {
			Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
			Object obj = cliMap.get(Constants.DEVICE_TYPE_CACHE_ITEM);
			if(null == obj) {
				continue;
			}
			JSONObject deviceJson = JSON.parseObject(obj.toString());
			String cliId = key.replaceAll(Constants.CLIENT_CACHE_KEY_PRE, "");
			//声明一个是否包含客户端ID的boolean变量
			boolean isConstainsCliId = false;
			for(String deviceId : deviceIds) {
				if(cliId.equals(deviceId)) {
					isConstainsCliId = true;
					//删除整个键值
					RedisUtil.del(key);
					childrenIds.addAll(deviceJson.keySet());
					break;
				}else {
					deviceJson.remove(deviceId);
				}
			}
			if(!isConstainsCliId) {
				//更新缓存表中的数据
				RedisUtil.hashSet(key, Constants.DEVICE_TYPE_CACHE_ITEM, deviceJson);
			}
		}
		return childrenIds;
	}
	
	public static void updateDevice(DeviceInfo di) {
		Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
		for(String key : keys) {
			Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
			Object obj = cliMap.get(Constants.DEVICE_TYPE_CACHE_ITEM);
			if(null == obj) {
				continue;
			}
			JSONObject deviceJson = JSON.parseObject(obj.toString());
			Object device = deviceJson.get(di.getId()+"");
			//判断设备数据中是否包含需要找的设备
			if(null != device) {
				deviceJson.put(di.getId()+"", JSON.toJSONString(di));
				RedisUtil.hashSet(key, Constants.DEVICE_TYPE_CACHE_ITEM, deviceJson);
				break;
			}
		}
	}
}
