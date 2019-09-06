package com.sinux.modules.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.modules.entity.param.TaskParamEntity;
import com.sinux.modules.entity.TaskInfo;

/**
 * 任务service
 * @ClassName TaskService
 * @Description: 任务service
 * @author Tangjc
 * @date 2019年6月12日 下午11:18:51
 */

public interface TaskServer {
	/**
	 * 开始任务
	 * @Title: beginTask
	 * @Description: 开始任务
	 * @author Tangjc
	 * @param taskId 任务id
	 * @param userId
	 * @return void
	 * @exception Exception
	 */
	public R beginTask(Long taskId,Long userId);
	/**
	 * 完成任务
	 * @Title: downTask
	 * @Description: 完成任务
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return void
	 */
	public R downTask(Long taskId,Long userId);
	
	/**
	 * 
	 * <p>Title: reportTask</p>  
	 * <p>Description: 任务线性时间内状态上报</p>  
	 * @author yexj  
	 * @date 2019年8月7日  
	 * @param taskId 任务ID
	 * @param type 任务状态类型，r：运行中，s：暂停中
	 * @return
	 */
	public R reportTask(Long taskId,String type);
	
	/**
	 * 
	 * <p>Title: addTask</p>  
	 * <p>Description: 保存任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月14日  
	 * @param task 任务参数 (0:未开始，1：进行中，2完成)
	 * @return 保存成功返回code=200的编码，失败返回code=500编码
	 */
	public R addTask(TaskParamEntity task);
	
	/**
	 * 
	 * <p>Title: updateTask</p>  
	 * <p>Description: 更新任务</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param task 新任务信息
	 * @return
	 */
	public R updateTask(TaskParamEntity task);
	
	/**
	 * 
	 * <p>Title: delTask</p>  
	 * <p>Description: 删除任务数据</p>  
	 * @author yexj  
	 * @date 2019年6月19日  
	 * @param taskIds 需要删除的任务ID集合
	 * @return 删除成功code==200，删除失败code=500
	 */
	public R delTask(long[] taskIds);
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询任务记录信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param query 查询任务记录信息封装参数
	 * @return
	 */
	public R getList(Query query);
	/**
	 * 根据用户id获取任务列表
	 * @Title: getListByUserId
	 * @Description: 根据用户id获取任务列表
	 * @author Tangjc
	 * @param userId 用户id
	 * @return List<TaskInfo> 任务列表
	 */
	public List<TaskInfo> getListByUserId(long userId);
	/**
	 * 修改任务参数
	 * @Title: updateTaskInfo
	 * @Description: 修改任务参数
	 * @author Tangjc
	 * @param taskInfo 任务对象
	 * @return
	 * @return int 影响行数 ==1为成功
	 */
	public int updateTaskInfo(TaskInfo taskInfo);
	/**
	 * 添加任务资源中间表
	 * @Title: addTaskResource
	 * @Description: 添加任务资源中间表
	 * @author Tangjc
	 * @param taskId 任务id
	 * @param resourceId 资源id
	 * @return void
	 */
	public void addTaskResource(long taskId, long resourceId);
	/**
	 * 根据任务id获取任务对象
	 * @Title: getOne
	 * @Description: 根据任务id获取任务对象
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return
	 * @return TaskInfo
	 */
	public TaskInfo getOne(long taskId);
	
	/**
	 * 
	 * <p>Title: uploadTaskTarget</p>  
	 * <p>Description: 上传任务目标文件</p>  
	 * @author yexj  
	 * @date 2019年7月30日  
	 * @param file
	 * @return
	 */
	public R uploadTaskTarget(MultipartFile file, HttpServletRequest req);
	
	/**
	 * 
	 * <p>Title: getTaskOpTime</p>  
	 * <p>Description: 查询任务线性时间中，任务操作时间点的状态，并组装图标数据</p>  
	 * @author yexj  
	 * @date 2019年8月2日  
	 * @param taskIds 任务ID集合
	 * @return
	 */
	public R getTaskOpTime(long[] taskIds);
}
