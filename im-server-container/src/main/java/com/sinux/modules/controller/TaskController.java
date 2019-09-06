package com.sinux.modules.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.bai.common.message.Message;
import com.sinux.bai.common.message.constats.Constants;
import com.sinux.bai.entity.device.BaseDevice;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.modules.entity.TaskInfo;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.server.TaskServer;
import com.sinux.utils.Sets;

import cn.hutool.http.HttpUtil;

/**
 * 
* <p>Title: TaskController</p>  
* <p>Description: 任务控制类</p>  
* @author yexj  
* @date 2019年6月4日
 */
@RestController
@RequestMapping("/container/task")
public class TaskController {
	private Logger log = LoggerFactory.getLogger(TaskController.class);
	@Autowired
	private TaskServer taskService;
	/**固定站客户端ip*/
	@Value("${fixed.imClient.ip}")
	private String imClientIp;
	/**固定站客户端端口*/
	@Value("${fixed.imClient.port}")
	private int imClientPort;
	@Autowired
	private RabbitTemplate temp;
	/**
	 * 开始任务
	 * @Title: beginTask
	 * @Description: 开始任务
	 * @author Tangjc
	 * @param params 任务参数  ,long taskId,long userId
	 * @return String
	 */
	@Option(module="任务管理",opDesc="开始任务")
	@RequestMapping(value="/beginTask",method={RequestMethod.POST})
	public R beginTask(@RequestParam("params")String params){
		JSONObject json = JSON.parseObject(params);
		return taskService.beginTask(json.getLong("taskId"), json.getLong("userId"));
	}
	
	/**
	 * 完成任务
	 * @Title: doneTask
	 * @Description: 完成任务
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return String
	 */
	@Option(module="任务管理",opDesc="结束任务")
	@RequestMapping(value="/doneTask",method={RequestMethod.POST})
	public R doneTask(@RequestParam("params")String params){
		JSONObject json = JSON.parseObject(params);
		return taskService.downTask(json.getLong("taskId"), json.getLong("userId"));
	}
	
	/**
	 * 任务执行操作，业务WEB客户端下发任务指令，转发到任务执行系统  ---------展示未用
	 * @Title: taskExecute
	 * @Description: TODO
	 * @author Tangjc
	 * @param taskExecuteParams
	 * @return
	 * @return R
	 */
	@Deprecated
	@Option(module="任务管理",opDesc="执行任务")
	@RequestMapping(value="/taskExecute",method=RequestMethod.POST)
	public R taskExecute(@RequestParam("deviceId")String deviceId,@RequestParam("params")String params){
		try{
			// 调用任务执行接口
			BaseDevice bd = Sets.getDm().getDeviceById(deviceId);
			//判断设备状态，如果设备状态是下线状态，则不然下发指令
//			if(bd.getDeviceStatus() == com.sinux.base.support.common.constants.Constants.DEVICE_OFLINE_STATUS) {
//				return R.error("网络接入设备心跳超时，可能服务已经下线，请联系管理员");
//			}
			Message message = bd.getEntityInterface().sendMessage(params);
			if(message.getCode() == Constants.CODE_FAIL) {
				return R.error("任务下发失败！");
			}else {
				return R.ok("任务下发成功！");
			}
		}catch (Exception e) {
			log.error("调用任务执行接口失败！",e);
			return R.error("调用任务执行接口失败!");
		}
	}
	
	/**
	 * 
	 * <p>Title: taskExecuteSynReport</p>  
	 * <p>Description: 任务执行异步回执接口 ------------暂时未用</p>  
	 * @author yexj  
	 * @date 2019年8月22日  
	 * @param params
	 * @return
	 */
	@Deprecated
	@RequestMapping("/taskExecuteSynReport")
	public R taskExecuteSynReport(@RequestParam("params")String params) {
		JSONObject json = JSON.parseObject(params);
//		temp.convertAndSend(RabbitMqConfig.IM_CONTAINER_TASK, json);
		return R.ok("任务回执成功！");
	}
	
	/**
	 * 
	 * <p>Title: taskAuthentication</p>  
	 * <p>Description: 任务鉴权调度接口，接收到请求后将请求转发到固定站综合管理客户端进行转发到鉴权设备</p>  
	 * @author yexj  
	 * @date 2019年8月21日  
	 * @param params
	 * @return
	 */
	@Option(module="任务管理",opDesc="执行鉴权")
	@RequestMapping("/taskAuthentication")
	public R taskAuthentication(@RequestParam("params")String params) {
		Map<String,Object> body = new HashMap<String,Object>();
		body.put("params", params);
		//发送到固定站综合客户端
		String rs = HttpUtil.post("http://"+imClientIp+":"+imClientPort+"/client/task/taskAuthentication", body);
		return JSON.parseObject(rs, R.class);
	}
	
	/**
	 * 
	 * <p>Title: taskAuthSynReport</p>  
	 * <p>Description: 任务鉴权验证异步回执接口，鉴权设备异步回执管理服务端，通过服务端调用客户端鉴权异步回执接口</p>  
	 * @author yexj  
	 * @date 2019年8月22日  
	 * @param params 回执参数
	 * @return
	 */
	@RequestMapping("/taskAuthSynReport")
	public R taskAuthSynReport(@RequestParam("params")String params) {
		JSONObject json = JSON.parseObject(params);
		//获取发送客户端ip和端口信息
		String client = json.getString("client");
		String clientIp = client.split(",")[0];
		String clientPort = client.split(",")[1];
		Map<String,Object> map = new HashMap<>();
		map.put("param", params);
		//发送消息到对应的客户端
		String rs = HttpUtil.post("http://"+clientIp+":"+clientPort+"/client/task/authTaskSynReceive", map);
		if(null == rs) {
			return R.error("发送设备异步结果到【"+client+"】客户端失败");
		}
		return R.ok("发送设备异步结果到【"+client+"】客户端成功！");
	}
	
	/**
	 * 
	 * <p>Title: reportTask</p>  
	 * <p>Description: 任务线性时间内状态上报</p>  
	 * @author yexj  
	 * @date 2019年8月7日  
	 * @param taskId 任务ID
	 * @param type 状态类型，r：运行中，s：暂停中
	 * @return
	 */
	public R reportTask(@RequestParam(name="taskId")Long taskId,@RequestParam(name="")String type) {
		return taskService.reportTask(taskId, type);
	}
	
	/**
	 * 
	 * <p>Title: forward</p>  
	 * <p>Description: 任务转发，任务执行中终端设备需要调用另一台终端设备，需要综合管理服务端进行转发,期间并不做任何有关业务操作</p>  
	 * @author yexj  
	 * @date 2019年8月5日  
	 * @param query
	 * @return
	 */
	@Option(module="任务管理",opDesc="任务操作终端设备之间调用需由综合管理服务端转发，且不做任何业务操作")
	@RequestMapping("/forward")
	public R forward(@RequestBody Query query) {
		String to = query.get("to").toString();
		try {
			HttpUtil.post("http://"+to+":8081/client/task/forward", query);
		}catch (Exception e) {
			log.error("任务终端设备转发调用综合管理客户端失败！",e);
			return R.error("转发调用失败！");
		}
		return R.ok("转发调用成功！");
	}
	
	/**
	 * 
	 * <p>Title: addTask</p>  
	 * <p>Description: 添加任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param task
	 * @return
	 */
	@Option(module="任务管理",opDesc="添加任务信息")
	@RequestMapping("/addTask")
	public R addTask(@RequestBody TaskParamEntity task) {
		return taskService.addTask(task);
	}
	
	/**
	 * 
	 * <p>Title: updateTask</p>  
	 * <p>Description: 更新任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param task
	 * @return
	 */
	@Option(module="任务管理",opDesc="更新任务信息")
	@RequestMapping("/updateTask")
	public R updateTask(@RequestBody TaskParamEntity task) {
		return taskService.updateTask(task);
	}
	
	/**
	 * 
	 * <p>Title: delTask</p>  
	 * <p>Description: 删除任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskIds
	 * @return
	 */
	@Option(module="任务管理",opDesc="删除任务信息")
	@RequestMapping("/delTask")
	public R delTask(@RequestBody long[] taskIds) {
		return taskService.delTask(taskIds);
	}
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param query
	 * @return 返回任务结果，code==200查询成功，code=500查询失败
	 */
	@RequestMapping("/getList")
	public R getList(@RequestBody Query query) {
		return taskService.getList(query);
	}
	
	/**
	 * 
	 * <p>Title: uploadTaskTarget</p>  
	 * <p>Description: 上传任务目标文件，只接收txt文件，并且文件内容需要以逗号分隔多个目标</p>  
	 * @author yexj  
	 * @date 2019年7月30日  
	 * @param file
	 * @return
	 */
	//@Option(module="任务管理",opDesc="上传任务目标文件")
	@RequestMapping("/uploadTaskTarget")
	public R uploadTaskTarget(@RequestParam("file")MultipartFile file, HttpServletRequest req) {
		return taskService.uploadTaskTarget(file, req);
	}
	
	/**
	 * 
	 * <p>Title: getTaskOpTime</p>  
	 * <p>Description: 查询任务线性时间中，任务操作时间点的状态，并组装图标数据</p>  
	 * @author yexj  
	 * @date 2019年8月2日  
	 * @param taskIds 任务ID集合
	 * @return
	 */
	@RequestMapping("/getTaskOpTime")
	public R getTaskOpTime(@RequestBody long[] taskIds) {
		return taskService.getTaskOpTime(taskIds);
	}
	
	/**
	 * 
	 * <p>Title: getTaskByUserId</p>  
	 * <p>Description: 根据用户ID查询用户任务信息</p>  
	 * @author yexj  
	 * @date 2019年8月23日  
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getTaskByUserId")
	public R getTaskByUserId(@RequestParam("userId")Long userId) {
		List<TaskInfo> tasks = taskService.getListByUserId(userId);
		if(null == tasks) {
			return R.error("根据用户ID查询任务失败！");
		}
		return R.ok("根据用户ID查询任务成功！").put("tasks", tasks);
	}
}
