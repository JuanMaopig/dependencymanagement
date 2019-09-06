package com.sinux.modules.entity;

import java.io.Serializable;

/**
 * 
* <p>Title: TaskOpTimeInfo</p>  
* <p>Description: 描述任务在线性时间上的操作状态实体</p>  
* @author yexj  
* @date 2019年8月2日
 */
public class TaskOpTimeInfo implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 任务ID
	 */
	private Long taskId;
	/**
	 * 任务线性时间操作状态描述如：r:300,s:400,r:500----》表示该任务在300到400之间时运行状态，400到500之间是暂停状态
	 */
	private String opTimeDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getOpTimeDesc() {
		return opTimeDesc;
	}
	public void setOpTimeDesc(String opTimeDesc) {
		this.opTimeDesc = opTimeDesc;
	}
	@Override
	public String toString() {
		return "TaskOpTimeInfo [id=" + id + ", taskId=" + taskId + ", opTimeDesc=" + opTimeDesc + "]";
	}
	
	
}
