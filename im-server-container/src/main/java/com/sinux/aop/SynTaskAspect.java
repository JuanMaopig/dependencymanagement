package com.sinux.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.constants.Constants.SynTaskEnum;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.HeartUtil;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.feign.ImAuthFeign;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.server.ResourceServer;
import com.sinux.utils.ClientCacheManager;
import com.sinux.utils.Sets;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * 
* <p>Title: SynTaskAspect</p>  
* <p>Description: 任务同步切面处理类</p>  
* @author yexj  
* @date 2019年6月24日
 */
@Aspect
@Component
public class SynTaskAspect {
	
	@Autowired
	private DeviceServer deviceServer;
	@Autowired
	private ResourceServer resourceServer;
	@Autowired
	private ImAuthFeign imAuthFeign;
	
	private Logger log = LoggerFactory.getLogger(SynTaskAspect.class);
	
	@Pointcut("@annotation(com.sinux.aop.SynTask)")
	public void synTaskPointCut() {}

	@AfterReturning(returning="rvt",pointcut="synTaskPointCut()")
	public void after(JoinPoint joinPoint, Object rvt) {
		Object[] params = joinPoint.getArgs();
		R r = (R)rvt;
		if(Integer.parseInt(r.get("code").toString()) != 200) {
			return;
		}
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法上的SynTask注解
        SynTask synTask = method.getAnnotation(SynTask.class);
		if(null != synTask) {
			//获取同步任务信息的类型add,update.delete------默认add类型
			SynTaskEnum synEnum =  synTask.type();
			//新增任务
			if(synEnum==SynTaskEnum.ADD || synEnum==SynTaskEnum.UPDATE) {
				TaskParamEntity te = (TaskParamEntity) params[0];
				long taskId = 0;
				String type = "";
				if(synEnum==SynTaskEnum.ADD) {
					taskId = Long.parseLong(r.get("taskId").toString());
					te.setTaskStatus(Constants.TASK_NO_RUNING_STATUS);
					type = Constants.SYN_MSG_TYPE_ADD;
				}else {
					taskId = te.getId();
					type = Constants.SYN_MSG_TYPE_UPDATE;
				}
				te.setId(taskId);
				// 获取需要同步任务信息的设备ip
				String[] userIds = te.getTaskExcuteUesrs().split(",");
				for(String userId : userIds) {
//					if(null == Sets.synMap.get("user")) {
//						break;
//					}
//					if(null == Sets.synMap.get("user").get(String.valueOf(userId))) {
//						continue;
//					}
//					String ip = String.valueOf(Sets.synMap.get("user").get(String.valueOf(userId)).get("ip"));
					String ip = ClientCacheManager.getCliIpByLoginUserId(userId);
					if(StringUtils.isBlank(ip)) {
						continue;
					}
					try {
						List<DeviceInfo> dis = deviceServer.getDevicesByTaskId(taskId);
						List<ResourceInfo> ris = resourceServer.getListByTaskId(taskId);
						Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_TASK_TYPE, type, te, String.valueOf(te.getId())));
						Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_DEVICE_TYPE, type, dis, "id"));
						Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_RESOURCE_TYPE, type, ris, "id"));
					} catch (InterruptedException e) {
						log.error("任务【"+type+"】下发失败！", e);
					}
				}
			}
			//删除任务
			if(synEnum==SynTaskEnum.DELETE) {
				if(null == r.get("resourceIds")) {
					return;
				}
				String resourceIds = r.get("resourceIds").toString();
				String deviceIds = r.get("deviceIds").toString();
				long[] taskIds = (long[]) params[0];
				for(long taskId : taskIds) {
					//通过feign远程调用im-server-auth服务的接口
					r = imAuthFeign.getUserByTaskId(taskId);
					if(Integer.parseInt(r.get("code").toString()) != 200) {
						continue;
					}
					JSONArray sus = JSON.parseArray(JSON.toJSONString(r.get("sus")));
					for(int i=0;i<sus.size();i++) {
						JSONObject obj = sus.getJSONObject(i);
						String userId = obj.getString("id");
//						if(null == Sets.synMap.get("user")) {
//							break;
//						}
//						if(null == Sets.synMap.get("user").get(userId)) {
//							continue;
//						}
//						String ip = String.valueOf(Sets.synMap.get("user").get(userId).get("ip"));
//						if(null == ip) {
//							continue;
//						}
						String ip = ClientCacheManager.getCliIpByLoginUserId(userId);
						if(StringUtils.isBlank(ip)) {
							continue;
						}
						try {
							Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_TASK_TYPE, Constants.SYN_MSG_TYPE_DEL, "", String.valueOf(taskId)));
							Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_DEVICE_TYPE, Constants.SYN_MSG_TYPE_DEL, getIdMap(deviceIds)));
							Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson(Constants.SYN_MAP_RESOURCE_TYPE, Constants.SYN_MSG_TYPE_DEL, getIdMap(resourceIds)));
						}catch (Exception e) {
							log.error("任务【delete】下发失败！", e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * <p>Title: getIdMap</p>  
	 * <p>Description: 将ID信息装换为map结构</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param idStr ID字符串，以逗号隔开
	 * @return
	 */
	private Map<String,Object> getIdMap(String idStr) {
		Map<String,Object> map = new HashMap<>();
		String[] idStrs = idStr.split(",");
		for(String id : idStrs) {
			map.put(id, "");
		}
		return map;
	}
}
