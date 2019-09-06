package com.sinux.modules.entity.param;

import com.sinux.modules.entity.TaskInfo;

public class TaskParamEntity extends TaskInfo{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务执行人
	 */
	private String taskExcuteUesrs;
	/**
	 * 任务执行所需设备
	 */
	private String taskDevices;
	/**
	 * 任务执行所需资源
	 */
	private String taskResources;
	
	public String getTaskExcuteUesrs() {
		return taskExcuteUesrs;
	}
	public void setTaskExcuteUesrs(String taskExcuteUesrs) {
		this.taskExcuteUesrs = taskExcuteUesrs;
	}
	public String getTaskDevices() {
		return taskDevices;
	}
	public void setTaskDevices(String taskDevices) {
		this.taskDevices = taskDevices;
	}
	public String getTaskResources() {
		return taskResources;
	}
	public void setTaskResources(String taskResources) {
		this.taskResources = taskResources;
	}
}
