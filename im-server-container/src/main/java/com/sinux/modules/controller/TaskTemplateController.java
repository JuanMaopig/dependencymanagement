package com.sinux.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.server.impl.TaskTemplateServerImpl;

/**
 * 
* <p>Title: TaskTemplateController</p>  
* <p>Description: 任务模版控制类</p>  
* @author yexj  
* @date 2019年6月20日
 */
@RestController
@RequestMapping("/container/task/template")
public class TaskTemplateController {
	
	@Autowired
	private TaskTemplateServerImpl taskTemplate;
	
	/**
	 * 
	 * <p>Title: addTask</p>  
	 * <p>Description: 添加任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param task
	 * @return
	 */
	@Option(module="任务模版管理",opDesc="添加任务模版信息")
	@RequestMapping("/addTaskTemplate")
	public R addTask(@RequestBody TaskParamEntity task) {
		return taskTemplate.addTaskTemplate(task);
	}
	
	/**
	 * 
	 * <p>Title: updateTask</p>  
	 * <p>Description: 更新任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param task
	 * @return
	 */
	@Option(module="任务模版管理",opDesc="更新任务模版信息")
	@RequestMapping("/updateTaskTemplate")
	public R updateTask(@RequestBody TaskParamEntity task) {
		return taskTemplate.updateTaskTemplate(task);
	}
	
	/**
	 * 
	 * <p>Title: delTask</p>  
	 * <p>Description: 删除任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskIds
	 * @return
	 */
	@Option(module="任务模版管理",opDesc="删除任务模版信息")
	@RequestMapping("/delTaskTemplate")
	public R delTask(@RequestBody long[] taskIds) {
		return taskTemplate.delTaskTemplate(taskIds);
	}
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询任务模版信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param query
	 * @return 返回任务结果，code==200查询成功，code=500查询失败
	 */
	@RequestMapping("/getList")
	public R getList(@RequestBody Query query) {
		return taskTemplate.getList(query);
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
	@Option(module="任务模版管理",opDesc="上传任务模版目标文件")
	@RequestMapping("/uploadTaskTarget")
	public R uploadTaskTarget(@RequestParam("file")MultipartFile file) {
		return taskTemplate.uploadTaskTarget(file);
	}
	
}
