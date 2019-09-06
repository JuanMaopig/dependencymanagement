package com.sinux.modules.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.aop.SynTask;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.entity.SysOptionLog;
import com.sinux.base.support.common.syslog.OptionLogService;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.base.support.common.util.HeartUtil;
import com.sinux.base.support.common.util.IpUtil;
import com.sinux.modules.dao.TaskDao;
import com.sinux.modules.entity.TaskInfo;
import com.sinux.modules.entity.TaskOpTimeInfo;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.server.TaskServer;
import com.sinux.modules.vo.TaskInfoVo;
import com.sinux.modules.vo.TaskOpTimeInfoVo;
import com.sinux.utils.ClientCacheManager;
import com.sinux.utils.Sets;

/**
 * 
* <p>Title: TaskServerImpl</p>  
* <p>Description: 任务服务接口实现类</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Service
public class TaskServerImpl implements TaskServer{
	/**
	 * 日志
	 */
	private Logger log = LoggerFactory.getLogger(TaskServerImpl.class);
			
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	@Autowired
	private OptionLogService optionLogService;

	/**
	 * 开始任务
	 * @Title: beginTask
	 * @Description: 开始任务
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return void
	 */
	@Override
	public R beginTask(Long taskId,Long userId){
		// 获取任务对象
		TaskInfo ti = taskDao.getOne(taskId);
		// 判断任务时间
		Date startDate = ti.getTaskStartTime();
		Date endDate = ti.getTaskEndTime();
		long now = System.currentTimeMillis();
		if(startDate.getTime()<now && now<endDate.getTime()){
			// 开始任务
			ti.setTaskStatus(Constants.TASK_RUNING_STATUS);
			
			int num = taskDao.updateTask(ti);
			// 获取同步ip
//			String ip = String.valueOf(Sets.synMap.get("user").get(String.valueOf(userId)).get("ip"));
			String ip = ClientCacheManager.getCliIpByLoginUserId(userId+"");
			try {
				// 同步任务
				Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson("task", "update", ti, String.valueOf(ti.getId())));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(num <= 0) {
				return R.error("任务开始失败！");
			}
			return R.ok("任务开始成功");
		}else{
			return R.ok("任务开始失败");
		}
	}
	/**
	 * 完成任务
	 * @Title: downTask
	 * @Description: 完成任务
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return void
	 */
	@Override
	public R downTask(Long taskId,Long userId){
		// 获取任务对象
		TaskInfo ti = taskDao.getOne(taskId);
		
		//完成任务
		ti.setTaskStatus(Constants.TASK_COMPLETE_STATUS);
		
		int num = taskDao.updateTask(ti);
		// 获取同步ip
//		String ip = String.valueOf(Sets.synMap.get("user").get(String.valueOf(userId)).get("ip"));
		String ip = ClientCacheManager.getCliIpByLoginUserId(userId+"");
		try {
			// 同步消息
			Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson("task", "update", ti, String.valueOf(ti.getId())));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(num <= 0) {
			return R.error("任务完成失败！");
		}
		return R.ok("任务完成成功！");
	}
	
	@Override
	public R reportTask(Long taskId, String type) {
		TaskOpTimeInfo opt = taskDao.getTaskOpTime(taskId);
		int num = 0;
		//判断太任务是否有历史上报数据
		if(null == opt) {
			opt = new TaskOpTimeInfo();
			opt.setTaskId(taskId);
			opt.setOpTimeDesc(type+":"+System.currentTimeMillis());
			num = executeJdbcUtil.addOne(opt);
		}else {
			opt.setOpTimeDesc(opt.getOpTimeDesc()+","+type+":"+System.currentTimeMillis());
			num = executeJdbcUtil.updateOne(opt);
		}
		if(num > 0) {
			return R.ok("任务状态上报成功！");
		}
		return R.error("任务状态上报失败！");
	}
	/**
	 * 同步任务，同步类型为add
	 */
	@SynTask(type=com.sinux.base.support.common.constants.Constants.SynTaskEnum.ADD)
	@Transactional
	@Override
	public R addTask(TaskParamEntity task) {
		//组装任务信息
		TaskInfo tk = new TaskInfo();
		tk.setTaskStartTime(task.getTaskStartTime());
		tk.setTaskEndTime(task.getTaskEndTime());
		tk.setTaskExecutePlace(task.getTaskExecutePlace());
		tk.setTaskCreateTime(new Date());
		tk.setTaskUpdateTime(new Date());
		tk.setTaskName(task.getTaskName());
		tk.setSecurityLevel(task.getSecurityLevel());
		tk.setTaskStatus(Constants.TASK_NO_RUNING_STATUS);
		tk.setTaskDesc(task.getTaskDesc());
		tk.setTaskTarget(task.getTaskTarget());
		//保存任务基本信息
		//long taskId = taskDao.addTask(tk);
		long taskId = executeJdbcUtil.addOneReturnKeys(tk);
		if(taskId <= 0) {
			return R.error("保存任务基本信息失败！");
		}
		
		//获取任务执行人信息
		String users = task.getTaskExcuteUesrs();
		if(StringUtils.isNotBlank(users)) {
			boolean flag = addTaskUser(taskId,users);
			if(!flag) {
				return R.error("保存任务执行人信息失败！");
			}
		}
		
		//获取任务执行设备信息
		String devices = task.getTaskDevices();
		if(StringUtils.isNotBlank(devices)) {
			boolean flag = addTaskDevice(taskId,devices);
			if(!flag) {
				return R.error("保存任务执行资源信息失败！");
			}
		}
		
		//获取任务执行资源信息
		String resources = task.getTaskResources();
		if(StringUtils.isNotBlank(resources)) {
			boolean flag = addTaskResource(taskId,resources);
			if(!flag) {
				return R.error("保存任务执行设备信息失败！");
			}
		}
		
		return R.ok("保存任务信息成功！").put("taskId", taskId);
	}
	
	/**
	 * 
	 * <p>Title: addTaskUser</p>  
	 * <p>Description: 添加任务执行人</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskId 任务ID
	 * @param users 任务人员ID集合
	 * @return 返回true or false
	 */
	private boolean addTaskUser(long taskId,String users) {
		String[] userIds = users.split(",");
		String sql = "INSERT INTO task_user_info (task_id,user_id) VALUES (?,?)";
		List<Object[]> params = new ArrayList<>();
		for(String id : userIds) {
			Object[] param = new Object[] {taskId,id};
			params.add(param);
		}
		//批量添加任务用户信息
		int[] tuis = executeJdbcUtil.executeBatchUpdateSql(sql, params);
		if(null == tuis) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * <p>Title: addTaskDevice</p>  
	 * <p>Description: 保存任务设备信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskId 任务ID
	 * @param devices 设备ID集合
	 * @return 返回true or false
	 */
	private boolean addTaskDevice(long taskId,String devices) {
		String[] deviceIds = devices.split(",");
		String sql = "INSERT INTO task_device_info (task_id,device_id) VALUES (?,?)";
		List<Object[]> params = new ArrayList<>();
		for(String id : deviceIds) {
			Object[] param = new Object[] {taskId,id};
			params.add(param);
		}
		//批量添加任务设备信息
		int[] tdis = executeJdbcUtil.executeBatchUpdateSql(sql, params);
		if(null == tdis) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * <p>Title: addTaskResource</p>  
	 * <p>Description: 保存任务资源信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskId 任务ID
	 * @param resources 资源ID集合
	 * @return 返回true or false
	 */
	private boolean addTaskResource(long taskId,String resources) {
		String[] resourceIds = resources.split(",");
		String sql = "INSERT INTO task_resource_info (task_id,resource_id) VALUES (?,?)";
		List<Object[]> params = new ArrayList<>();
		for(String id : resourceIds) {
			Object[] param = new Object[] {taskId,id};
			params.add(param);
		}
		//批量添加任务资源信息
		int[] tris = executeJdbcUtil.executeBatchUpdateSql(sql, params);
		if(null == tris) {
			return false;
		}
		return true;
	}
	
	/**
	 * 同步任务，同步类型为update
	 */
	@SynTask(type=com.sinux.base.support.common.constants.Constants.SynTaskEnum.UPDATE)
	@Transactional
	@Override
	public R updateTask(TaskParamEntity task) {
		//判断任务状态
		if(task.getTaskStatus() != Constants.TASK_NO_RUNING_STATUS) {
			return R.error("更新任务失败，当前任务正在进行中，不能进行更新操作！");
		}
		taskDao.getOne(task.getId());
		TaskInfo oldTk = taskDao.getOne(task.getId());
		if(null == oldTk) {
			return R.error("更新任务失败，当前任务已经被管理人员清除！");
		}
		if(oldTk.getTaskStatus() != Constants.TASK_NO_RUNING_STATUS) {
			return R.error("更新任务失败，当前任务正在进行中，不能进行更新操作！");
		}
		//组装任务信息
		TaskInfo tk = new TaskInfo();
		tk.setId(task.getId());
		tk.setTaskStartTime(task.getTaskStartTime());
		tk.setTaskEndTime(task.getTaskEndTime());
		tk.setTaskExecutePlace(task.getTaskExecutePlace());
		tk.setTaskCreateTime(task.getTaskCreateTime());
		tk.setTaskUpdateTime(new Date());
		tk.setTaskName(task.getTaskName());
		tk.setSecurityLevel(task.getSecurityLevel());
		tk.setTaskStatus(task.getTaskStatus());
		tk.setTaskDesc(task.getTaskDesc());
		tk.setTaskTarget(task.getTaskTarget());
		//更新任务
		int num = executeJdbcUtil.updateOne(tk);
		if(num <= 0) {
			return R.error("更新任务信息失败！");
		}
		
		List<Object[]> params = new ArrayList<>();
		Object[] param = new Object[] {tk.getId()};
		params.add(param);
		//批量删除任务用户
		int[] userNums = delTaskUser(params);
		if(null == userNums || userNums.length <= 0) {
			return R.error("删除任务用户信息失败！");
		}
		//批量删除任务设备
		int[] deviceNums = delTaskDevice(params);
		if(null == deviceNums || deviceNums.length <= 0) {
			return R.error("删除任务设备信息失败！");
		}
		//批量删除任务资源
		int[] resourceNums = delTaskResource(params);
		if(null == resourceNums || resourceNums.length <= 0) {
			return R.error("删除任务资源信息失败！");
		}
		
		//获取任务执行人信息
		String users = task.getTaskExcuteUesrs();
		if(StringUtils.isNotBlank(users)) {
			boolean flag = addTaskUser(task.getId(),users);
			if(!flag) {
				return R.error("保存任务执行人信息失败！");
			}
		}
		
		//获取任务执行设备信息
		String devices = task.getTaskDevices();
		if(StringUtils.isNotBlank(devices)) {
			boolean flag = addTaskDevice(task.getId(),devices);
			if(!flag) {
				return R.error("保存任务执行资源信息失败！");
			}
		}
		
		//获取任务执行资源信息
		String resources = task.getTaskResources();
		if(StringUtils.isNotBlank(resources)) {
			boolean flag = addTaskResource(task.getId(),resources);
			if(!flag) {
				return R.error("保存任务执行设备信息失败！");
			}
		}
		return R.ok("更新任务信息成功！");
	}
	
	/**
	 * 同步任务，同步类型为delete
	 */
	@SynTask(type=com.sinux.base.support.common.constants.Constants.SynTaskEnum.DELETE)
	@Transactional
	@Override
	public R delTask(long[] taskIds) {
		String sql = "SELECT ti.* FROM task_info ti WHERE ti.id IN ( ? )";
		String idStr = splitLongToStr(taskIds,",");
		List<TaskInfo> tis = executeJdbcUtil.getList(TaskInfo.class, sql, new Object[] {idStr});
		List<Long> idList = new ArrayList<>();
		List<Object[]> params = new ArrayList<>();
		//接收任务状态不为未开始状态的任务描述，并以逗号隔开
		StringBuilder taskDesc = new StringBuilder();
		//判断获取到的任务信息列表中状态为未开始的任务，并将其ID存入集合中，以便删除操作
		for(TaskInfo ti : tis) {
			//判断任务状态
			if(ti.getTaskStatus() != Constants.TASK_RUNING_STATUS) {
				idList.add(ti.getId());
				Object[] param = new Object[] {ti.getId()};
				params.add(param);
			}else {
				taskDesc.append(ti.getTaskDesc()+",");
			}
		}
		//批量删除任务
		long[] idArray = idList.parallelStream().mapToLong(id -> id.longValue()).toArray();
		if(null ==idArray || idArray.length == 0) {
			return R.ok("删除部分任务信息成功，其中任务"+taskDesc.toString()+"的状态为运行中，故不能对其进行删除！");
		}
		//查询任务设备信息ID,并将ID之间用逗号隔开
		String deviceIds = getDeiviceIdBySplit(idArray);
		//查询任务资源信息,并将ID之间用逗号隔开
		String resourceIds = getResourceIdBySplit(idArray);
		int[] delNums = executeJdbcUtil.delMore(TaskInfo.class, idArray);
		if(null == delNums|| delNums.length <= 0) {
			return R.error("删除任务信息失败！");
		}
		//批量删除任务用户
		int[] userNums = delTaskUser(params);
		//批量删除任务设备
		int[] deviceNums = delTaskDevice(params);
		//批量删除任务资源
		int[] resourceNums = delTaskResource(params);
		log.info("删除任务数："+delNums.length+",删除任务用户数:"+userNums.length+",删除任务设备数:"+deviceNums.length+",删除任务资源数："+resourceNums.length);
		if(taskDesc.length() != 0) {
			return R.ok("删除部分任务信息成功，其中任务"+taskDesc.toString()+"的状态为运行中，故不能对其进行删除！").put("deviceIds", deviceIds).put("resourceIds", resourceIds);
		}
		return R.ok("删除任务信息成功！").put("deviceIds", deviceIds).put("resourceIds", resourceIds);
	}
	
	/**
	 * 
	 * <p>Title: getDeiviceIdBySplit</p>  
	 * <p>Description: 根据任务ID集合查询任务设备ID集合，并以逗号隔开</p>  
	 * @author yexj  
	 * @date 2019年6月25日  
	 * @param ids 任务ID集合
	 * @return 返回任务设备ID字符串，以逗号隔开
	 */
	private String getDeiviceIdBySplit(long[] ids) {
		String sql = "SELECT GROUP_CONCAT(tdi.device_id) AS deviceIds FROM task_device_info tdi LEFT JOIN task_info ti ON tdi.task_id = ti.id WHERE ti.id IN ( ? )";
		String idStr = splitLongToStr(ids,",");
		Map<String,Object> map = executeJdbcUtil.executeQuerySql(sql, new Object[] {idStr});
		String deviceIds = map.get("deviceIds") == null ? "" : map.get("deviceIds").toString();
		return deviceIds;
	}
	
	/**
	 * 
	 * <p>Title: getResourceIdBySplit</p>  
	 * <p>Description: 根据任务ID查询任务资源ID集合，并以逗号隔开</p>  
	 * @author yexj  
	 * @date 2019年6月25日  
	 * @param ids 任务ID集合
	 * @return 返回任务资源ID字符串，以逗号隔开
	 */
	private String getResourceIdBySplit(long[] ids) {
		String sql = "SELECT GROUP_CONCAT(tri.resource_id) AS resourceIds FROM task_resource_info tri LEFT JOIN task_info ti ON tri.task_id = ti.id WHERE ti.id IN ( ? )";
		String idStr = splitLongToStr(ids,",");
		Map<String,Object> map = executeJdbcUtil.executeQuerySql(sql, new Object[] {idStr});
		String resourceIds = map.get("resourceIds") == null ? "" : map.get("resourceIds").toString();
		return resourceIds;
	}
	
	/**
	 * 
	 * <p>Title: splitLongToStr</p>  
	 * <p>Description: 将long数组装换为字符串，并以执行分隔符隔开</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param ids id数组
	 * @param split 分隔符
	 * @return
	 */
	private String splitLongToStr(long[] ids, String split) {
		StringBuilder idStr = new StringBuilder();
		boolean first = true;
		for(long id : ids) {
			if(first) {
				idStr.append(id);
				first = false;
			}else {
				idStr.append(split+id);
			}
		}
		return idStr.toString();
	}
	
	/**
	 * 
	 * <p>Title: delTaskUser</p>  
	 * <p>Description: 删除任务用户</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param params 任务ID集合
	 * @return 返回操作记录数
	 */
	private int[] delTaskUser(List<Object[]> params) {
		//批量删除任务用户
		String userSql = "DELETE FROM task_user_info WHERE task_id = ?";
		int[] userNums = executeJdbcUtil.executeBatchUpdateSql(userSql, params);
		return userNums;
	}
	
	/**
	 * 
	 * <p>Title: delTaskDevice</p>  
	 * <p>Description: 删除任务设备</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param params 任务ID集合
	 * @return 返回操作记录数
	 */
	private int[] delTaskDevice(List<Object[]> params) {
		String deviceSql = "DELETE FROM task_device_info WHERE task_id = ?";
		int[] deviceNums = executeJdbcUtil.executeBatchUpdateSql(deviceSql, params);
		return deviceNums;
	}
	
	/**
	 * 
	 * <p>Title: delTaskResource</p>  
	 * <p>Description: 删除任务资源</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param params 任务ID集合
	 * @return 返回操作记录数
	 */
	private int[] delTaskResource(List<Object[]> params) {
		String resourceSql = "DELETE FROM task_resource_info WHERE task_id = ?";
		int[] resourceNums = executeJdbcUtil.executeBatchUpdateSql(resourceSql, params);
		return resourceNums;
	}
	
	@Override
	public R getList(Query query) {
		//查询任务信息
		List<TaskInfoVo> tivs = taskDao.getList(query,"task_info");
		//查询任务信息总记录数
		int total = taskDao.getListNum(query,"task_info");
		if(null == tivs) {
			return R.error("分页查询任务信息失败！");
		}
		return R.ok("分页查询任务信息成功！").put("rows", tivs).put("total", total);
	}
	@Override
	public List<TaskInfo> getListByUserId(long userId) {
		return taskDao.getListByUserId(userId);
	}
	@Override
	public int updateTaskInfo(TaskInfo taskInfo) {
		return taskDao.updateTask(taskInfo);
	}
	@Override
	public void addTaskResource(long taskId, long resourceId) {
		taskDao.addTaskResource(taskId, resourceId);
	}
	@Override
	public TaskInfo getOne(long taskId) {
		return taskDao.getOne(taskId);
	}
	
	@Transactional
	@Override
	public R uploadTaskTarget(MultipartFile file, HttpServletRequest req) {
		StringBuilder result = new StringBuilder("");
		InputStream in = null;
		File tempFile = null;
		try {
			in = file.getInputStream();
			tempFile = File.createTempFile("taskTarget", ".txt");
			FileUtils.copyInputStreamToFile(in, tempFile);
			List<String> lines = FileUtils.readLines(tempFile, "UTF-8");
			for(String str : lines) {
				if(str.endsWith(",")) {
					str = str.substring(0, str.length()-1);
				}
				result.append(str).append(",");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(null != in) {
				IOUtils.closeQuietly(in);
			}
			if(null != tempFile) {
				tempFile.deleteOnExit();
			}
		}
		//加入日志，由于该方法比较特殊，只能通过现有方法加入日志。
		SysOptionLog sol = new SysOptionLog();
		sol.setModule("任务管理");
		sol.setMethod("uploadMenuImage");
		sol.setActionUrl("/container/task/uploadTaskTarget");
		sol.setContent("{\"file\":\""+file.getOriginalFilename()+"\"}");
		sol.setCreateDate(new Date());
		sol.setOpDesc("上传任务目标文件");
		sol.setResult(1);
		sol.setLevel(Constants.LOG_LEVEL_INFO);
		sol.setIp(IpUtil.getWebClientIp(req));
		optionLogService.addOne(sol);
		return R.ok("任务目标文件上传成功！").put("taskTarget", result.substring(0, result.length()-1));
	}
	@Override
	public R getTaskOpTime(long[] taskIds) {
		List<TaskOpTimeInfoVo> totis = taskDao.getTaskOpTime(taskIds);
		List<String> yValue = new ArrayList<>();
		List<Object> series = new ArrayList<>();
		String[] colors = new String[] {"#7937dd","#00c1cd","#09cf6b","#e09e01","#f68040","#ff5c5c","#62c123"};
		int maxOpNum = 0;
		long minOpTime = System.currentTimeMillis();
		for(TaskOpTimeInfoVo tk : totis){
			yValue.add(tk.getTaskDesc());
			long startTime = tk.getTaskStartTime().getTime();
			long endTime = tk.getTaskEndTime().getTime();
			tk.getOpTimeDesc().split(",");
			StringBuilder opTimeBilder = new StringBuilder("s:").append(startTime).append(",").append(tk.getOpTimeDesc());
			String[] lastOp = opTimeBilder.substring(opTimeBilder.lastIndexOf(",")+1, opTimeBilder.length()).split(":");
			//判断任务最后操作状态是运行状态还是停止状态，如果是运行状态则将任务结束时间作为终止时间，
			if("r".equals(lastOp[0])) {
				opTimeBilder.append(",s:").append(endTime);
			}
			//计算任务操作步骤数
			int opNum = opTimeBilder.toString().split(",").length;
			if(opNum > maxOpNum) {
				maxOpNum = opNum;
			}
			//计算任务最小时间点
			if(startTime < minOpTime) {
				minOpTime = startTime;
			}
			tk.setOpTimeDesc(opTimeBilder.toString());
		}
		for(int i=0;i<maxOpNum;i++) {
			Map<String,Object> serieMap= new HashMap<>();
			serieMap.put("type", "bar");
			serieMap.put("stack", "总量");
			Map<String,Object> normalMap= new HashMap<>();
			normalMap.put("show", true);
			normalMap.put("position", "insideRight");
			if(i % 2 == 0) {
				serieMap.put("name", "未进行");
				normalMap.put("formatter", "已暂停");
			}else {
				serieMap.put("name", "进行中");
				normalMap.put("formatter", "进行中");
			}
			Map<String,Object> outLabelMap= new HashMap<>();
			outLabelMap.put("normal", normalMap);
			serieMap.put("label", outLabelMap);
			List<Object> data = new ArrayList<>();
			for(TaskOpTimeInfoVo tk : totis) {
				String[] opTimes = tk.getOpTimeDesc().split(",");
				if(opTimes.length > i) {
					Map<String,Object> dataMap= new HashMap<>();
					//获取任务制定操作时间
					long opTimeLong = Long.parseLong(opTimes[i].split(":")[1]);
					dataMap.put("name", tk.getTaskDesc());
					dataMap.put("value", (opTimeLong - minOpTime) / 1000000);
					Map<String,Object> innerLabelMap= new HashMap<>();
					innerLabelMap.put("show", true);
					dataMap.put("label", innerLabelMap);
					Random randomColor = new Random();
					Map<String,Object> itemStyleMap= new HashMap<>();
					itemStyleMap.put("color", i % 2 != 0 ? colors[randomColor.nextInt(7)] : "#c1c5cb");
					dataMap.put("itemStyle", itemStyleMap);
					dataMap.put("id", tk.getTaskId());
					data.add(dataMap);
				}
			}
			serieMap.put("data", data);
			series.add(serieMap);
		}
		return R.ok("查询任务时间线性操作状态成功！").put("yValue", yValue).put("series", series).put("minOpTime", minOpTime);
	}
	
	public static void main(String[] args) {
		
	}
	
}
