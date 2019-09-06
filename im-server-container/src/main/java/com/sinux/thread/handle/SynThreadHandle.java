package com.sinux.thread.handle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.MapUtil;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.entity.TaskInfo;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.server.ResourceServer;
import com.sinux.modules.server.TaskServer;
import com.sinux.utils.Sets;

/**
 * 
* <p>Title: SynThreadHandle</p>  
* <p>Description: 同步消息处理，将同步消息持久化到缓存数据库中方便取用</p>  
* @author yexj  
* @date 2019年8月9日
 */
public class SynThreadHandle {
	
	/**日志服务*/
	private static Logger log = LoggerFactory.getLogger(SynThreadHandle.class);
	/**
	 * 
	 * <p>Title: cacheDeviceData</p>  
	 * <p>Description: 缓存设备类型数据</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param json 上报参数
	 */
	public static void cacheDeviceData(JSONObject json) {
		//获取客户端上报用户信息携带数据
		JSONObject deviceJson = json.getJSONObject("data");
		//获取客户端上报设备ID
		String cliId = json.getString("cliId");
		//获取客户端上报信息操作方式
		String method = json.getString("method");
		if(StringUtils.isNotBlank(cliId)) {
			executeSynMethod(cliId, Constants.DEVICE_TYPE_CACHE_ITEM, deviceJson, method);
		}
	}
	
	/**
	 * 
	 * <p>Title: cacheUserData</p>  
	 * <p>Description: 缓存用户类型数据</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param json 上报参数
	 * @param taskServer
	 * @param resourceServer
	 * @param deviceServer
	 */
	public static void cacheUserData(JSONObject json, TaskServer taskServer, ResourceServer resourceServer, DeviceServer deviceServer) {
		//获取客户端上报用户信息携带数据
		JSONObject userJson = json.getJSONObject("data");
		//获取客户端上报设备ID
		String cliId = json.getString("cliId");
		//获取客户端上报信息操作方式
		String method = json.getString("method");
		Iterator<Entry<String, Object>> iters = userJson.entrySet().iterator();
		String ip = "";
		long userId = 0;
		while(iters.hasNext()) {
			Entry<String, Object> entry = iters.next();
			userId = Long.parseLong(entry.getKey());
		}
		if(StringUtils.isNotBlank(cliId)) {
			executeSynMethod(cliId, Constants.USER_TYPE_CACHE_ITEM, userJson, method);
			//通过客户端ID，获取客户端IP信息
			ip = RedisUtil.hashGet(Constants.CLIENT_CACHE_KEY_PRE+cliId, "ip").toString();
		}
		
		//通过客户端IP查询属于该客户端的消息同步队列
		ArrayBlockingQueue<String> synQueue;
		if(Sets.heartBox.getMsgQueueMap().containsKey(ip)){
			synQueue = Sets.heartBox.getMsgQueueMap().get(ip);
		}else{
			synQueue = new ArrayBlockingQueue<String>(1000, true);
			Sets.heartBox.getMsgQueueMap().put(ip, synQueue);
		}
		// 查询属于该用户的资源和任务情况
		// 任务list
		List<TaskInfo> taskList = taskServer.getListByUserId(userId);
		List<TaskParamEntity> taskchildList = taskList.parallelStream().map(task -> {
			TaskParamEntity tpe = new TaskParamEntity();
			tpe.setId(task.getId());
			tpe.setSecurityLevel(task.getSecurityLevel());
			tpe.setTaskCreateTime(task.getTaskCreateTime());
			tpe.setTaskDesc(task.getTaskDesc());
			tpe.setTaskEndTime(task.getTaskEndTime());
			tpe.setTaskExecutePlace(task.getTaskExecutePlace());
			tpe.setTaskName(task.getTaskName());
			tpe.setTaskStartTime(task.getTaskStartTime());
			tpe.setTaskStatus(task.getTaskStatus());
			return tpe;
		}).collect(Collectors.toList());
		
		if(!taskchildList.isEmpty()){
			// 资源list
			List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
			// 设备list
			List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
			taskchildList.forEach(task->{
				//查询资源信息
				List<ResourceInfo> tmpList = resourceServer.getListByTaskId(task.getId());
				// 计算资源id
				StringBuilder resources = new StringBuilder();
				for(int i=0;i<tmpList.size();i++) {
					resources.append(tmpList.get(i).getId()).append(",");
				}
				task.setTaskResources(resources.substring(0, resources.length()-1));
				resourceList.addAll(tmpList);
				//查询设备信息
				List<DeviceInfo> dls = deviceServer.getDevicesByTaskId(task.getId());
				// 计算设备id
				StringBuilder devices = new StringBuilder();
				for(int i=0;i<dls.size();i++) {
					devices.append(dls.get(i).getId()).append(",");
				}
				task.setTaskDevices(devices.substring(0, devices.length()-1));
				deviceList.addAll(dls);
			});
			
			try {
				JSONObject taskJson = new JSONObject();
				taskJson.put("type", Constants.SYN_MAP_TASK_TYPE);
				taskJson.put("method", Constants.SYN_MSG_TYPE_ADD);
				taskJson.put("data", MapUtil.listToMap(taskchildList, "id"));
				log.info("任务json数据"+taskJson.toJSONString());
				synQueue.put(taskJson.toJSONString());
				
				if(!resourceList.isEmpty()){
					JSONObject resourceJson = new JSONObject();
					resourceJson.put("type", Constants.SYN_MAP_RESOURCE_TYPE);
					resourceJson.put("method", Constants.SYN_MSG_TYPE_ADD);
					resourceJson.put("data", MapUtil.listToMap(resourceList, "id"));
					// 同步该用户消息
					log.info("任务资源json数据"+resourceJson.toJSONString());
					synQueue.put(resourceJson.toJSONString());
				}
				
				if(!deviceList.isEmpty()) {
					JSONObject deviceJson = new JSONObject();
					deviceJson.put("type", Constants.SYN_MAP_DEVICE_TYPE);
					deviceJson.put("method", Constants.SYN_MSG_TYPE_ADD);
					deviceJson.put("data", MapUtil.listToMap(deviceList, "id"));
					// 同步该用户消息
					log.info("任务资源json数据"+deviceJson.toJSONString());
					synQueue.put(deviceJson.toJSONString());
				}
			}catch (Exception e) {
				log.error("用户任务消息如同步下发失败", e);
			}
		}
	}
	
	/**
	 * 
	 * <p>Title: executeMethod</p>  
	 * <p>Description: 执行同步数据操作函数</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param cliId 客户端ID
	 * @param item 数据项
	 * @param data 数据
	 * @param method 执行方式
	 */
	private static void executeSynMethod(String cliId, String item, JSONObject data, String method) {
		if(Constants.SYN_MSG_TYPE_ADD.equals(method)) {
			add(cliId, item, data);
		}
		if(Constants.SYN_MSG_TYPE_DEL.equals(method)) {
			del(cliId, item, data);
		}
		if(Constants.SYN_MSG_TYPE_UPDATE.equals(method)) {
			update(cliId, item, data);
		}
	}
	
	/**
	 * 
	 * <p>Title: add</p>  
	 * <p>Description: 添加数据操作</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param cliId 客户端ID
	 * @param item 添加数据项
	 * @param data 添加数据
	 */
	private static void add(String cliId, String item, JSONObject data) {
		Object obj = RedisUtil.hashGet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item);
		if(null == obj) {
			RedisUtil.hashSet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item, data);
		}else {
			JSONObject cacheJson = JSON.parseObject(obj.toString());
			cacheJson.putAll(data);
			RedisUtil.hashSet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item, cacheJson);
		}
	}
	
	/**
	 * 
	 * <p>Title: del</p>  
	 * <p>Description: 删除数据操作</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param cliId 客户端ID
	 * @param item 添加数据项
	 * @param data 添加数据
	 */
	private static void del(String cliId, String item, JSONObject data) {
		Object obj = RedisUtil.hashGet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item);
		if(null == obj) {
			return;
		}else {
			JSONObject cacheJson = JSON.parseObject(obj.toString());
			Iterator<Entry<String, Object>> iter = data.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				//删除json中的数据
				cacheJson.remove(key, value);
			}
			RedisUtil.hashSet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item, cacheJson);
		}
	}
	
	/**
	 * 
	 * <p>Title: update</p>  
	 * <p>Description: 更新数据操作</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param cliId 客户端ID
	 * @param item 添加数据项
	 * @param data 添加数据
	 */
	private static void update(String cliId, String item, JSONObject data) {
		Object obj = RedisUtil.hashGet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item);
		if(null == obj) {
			RedisUtil.hashSet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item, data);
		}else {
			JSONObject cacheJson = JSON.parseObject(obj.toString());
			cacheJson.putAll(data);
			RedisUtil.hashSet(Constants.CLIENT_CACHE_KEY_PRE+cliId, item, cacheJson);
		}
	}
}
