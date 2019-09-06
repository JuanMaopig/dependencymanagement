package com.sinux.modules.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.TaskDao;
import com.sinux.modules.entity.TaskTemplateInfo;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.vo.TaskInfoVo;

/**
 * 
* <p>Title: TaskServerImpl</p>  
* <p>Description: 任务模版服务接口实现类</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Service
public class TaskTemplateServerImpl{
	/**
	 * 日志
	 */
	private Logger log = LoggerFactory.getLogger(TaskTemplateServerImpl.class);
			
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;

	/**
	 * 
	 * <p>Title: addTask</p>  
	 * <p>Description: 保存任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月20日  
	 * @param task 任务模版信息
	 * @return 
	 */
	@Transactional
	public R addTaskTemplate(TaskParamEntity task) {
		//组装任务信息
		TaskTemplateInfo tk = new TaskTemplateInfo();
		tk.setTaskStartTime(task.getTaskStartTime());
		tk.setTaskEndTime(task.getTaskEndTime());
		tk.setTaskExecutePlace(task.getTaskExecutePlace());
		tk.setTaskCreateTime(new Date());
		tk.setTaskUpdateTime(new Date());
		tk.setTaskName(task.getTaskName());
		tk.setSecurityLevel(task.getSecurityLevel());
		tk.setTaskDesc(task.getTaskDesc());
		tk.setTaskTarget(task.getTaskTarget());
		//判断字符串是否是从逗号开头的，如果是则将其去掉,
		//以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskDevices().startsWith(",")) {
			tk.setTaskDevices(task.getTaskDevices().replaceFirst(",", ""));
		}else {
			tk.setTaskDevices(task.getTaskDevices());
		}
		//判断字符串是否是从逗号开头的，如果是则将其去掉,
		//以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskExcuteUesrs().startsWith(",")) {
			tk.setTaskExcuteUesrs(task.getTaskExcuteUesrs().replaceFirst(",", ""));
		}else {
			tk.setTaskExcuteUesrs(task.getTaskExcuteUesrs());
		}
		//判断字符串是否是从逗号开头的，如果是则将其去掉,
		//以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskResources().startsWith(",")) {
			tk.setTaskResources(task.getTaskResources().replaceFirst(",", ""));
		}else {
			tk.setTaskResources(task.getTaskResources());
		}
		//保存任务模版基本信息
		long taskId = taskDao.addTaskTemplate(tk);
		if(taskId <= 0) {
			return R.error("保存任务模版基本信息失败！");
		}
		return R.ok("保存任务模版信息成功！");
	}
	
	/**
	 * 
	 * <p>Title: updateTaskTemplate</p>  
	 * <p>Description: 更新任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月20日  
	 * @param task 任务模版信息
	 * @return
	 */
	@Transactional
	public R updateTaskTemplate(TaskParamEntity task) {
		//组装任务信息
		TaskTemplateInfo tk = new TaskTemplateInfo();
		tk.setId(task.getId());
		tk.setTaskStartTime(task.getTaskStartTime());
		tk.setTaskEndTime(task.getTaskEndTime());
		tk.setTaskExecutePlace(task.getTaskExecutePlace());
		tk.setTaskCreateTime(task.getTaskCreateTime());
		tk.setTaskUpdateTime(new Date());
		tk.setTaskName(task.getTaskName());
		tk.setSecurityLevel(task.getSecurityLevel());
		tk.setTaskDesc(task.getTaskDesc());
		tk.setTaskTarget(task.getTaskTarget());
		//判断字符串是否是从逗号开头的，如果是则将其去掉
		//,以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskDevices().startsWith(",")) {
			tk.setTaskDevices(task.getTaskDevices().replaceFirst(",", ""));
		}else {
			tk.setTaskDevices(task.getTaskDevices());
		}
		//判断字符串是否是从逗号开头的，如果是则将其去掉
		//,以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskExcuteUesrs().startsWith(",")) {
			tk.setTaskExcuteUesrs(task.getTaskExcuteUesrs().replaceFirst(",", ""));
		}else {
			tk.setTaskExcuteUesrs(task.getTaskExcuteUesrs());
		}
		//判断字符串是否是从逗号开头的，如果是则将其去掉
		//,以防止入库后将其作为条件来查询会有异常结果
		if(task.getTaskResources().startsWith(",")) {
			tk.setTaskResources(task.getTaskResources().replaceFirst(",", ""));
		}else {
			tk.setTaskResources(task.getTaskResources());
		}
		//更新任务
		int num = executeJdbcUtil.updateOne(tk);
		if(num <= 0) {
			return R.error("更新任务模版信息失败！");
		}
		return R.ok("更新任务模版信息成功！");
	}
	
	/**
	 * 
	 * <p>Title: delTaskTemplate</p>  
	 * <p>Description: 删除任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月20日  
	 * @param taskIds 任务模版ID集合
	 * @return
	 */
	@Transactional
	public R delTaskTemplate(long[] taskIds) {
		//批量删除任务
		int[] delNums = executeJdbcUtil.delMore(TaskTemplateInfo.class, taskIds);
		if(null == delNums|| delNums.length <= 0) {
			return R.error("删除任务模版信息失败！");
		}
		return R.ok("删除任务模版信息成功！");
	}
	
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询任务模版数据</p>  
	 * @author yexj  
	 * @date 2019年6月20日  
	 * @param query
	 * @return
	 */
	public R getList(Query query) {
		//查询任务信息
		List<TaskInfoVo> tivs = taskDao.getTemplateList(query);
		//查询任务信息总记录数
		int total = taskDao.getTemplateListNum(query);
		if(null == tivs) {
			return R.error("分页查询任务模版信息失败！");
		}
		return R.ok("分页查询任务模版信息成功！").put("rows", tivs).put("total", total);
	}
	
	/**
	 * 
	 * <p>Title: uploadTaskTarget</p>  
	 * <p>Description: 接收解析任务目标上传文件，并将内容返回界面</p>  
	 * @author yexj  
	 * @date 2019年7月30日  
	 * @param file
	 * @return
	 */
	public R uploadTaskTarget(MultipartFile file) {
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
		return R.ok("任务目标文件上传成功！").put("taskTarget", result.substring(0, result.length()-1));
	}
}
