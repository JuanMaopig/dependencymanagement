package com.sinux.modules.vo;

import com.sinux.modules.entity.TaskInfo;

public class TaskInfoVo extends TaskInfo{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务执行人ID集合
	 */
	private String taskExcuteUesrs;
	/**
	 * 任务执行人名字集合
	 */
	private String taskExcuteUesrNames;
	/**
	 * 任务执行设备ID集合
	 */
	private String taskDevices;
	/**
	 * 任务执行设备名字集合
	 */
	private String taskDeviceNames;
	/**
	 * 任务执行资源ID集合
	 */
	private String taskResources;
	/**
	 * 任务执行资源名字集合
	 */
	private String taskResourceNames;
	public String getTaskExcuteUesrs() {
		return taskExcuteUesrs;
	}
	public void setTaskExcuteUesrs(String taskExcuteUesrs) {
		this.taskExcuteUesrs = taskExcuteUesrs;
	}
	public String getTaskExcuteUesrNames() {
		return taskExcuteUesrNames;
	}
	public void setTaskExcuteUesrNames(String taskExcuteUesrNames) {
		this.taskExcuteUesrNames = taskExcuteUesrNames;
	}
	public String getTaskDevices() {
		return taskDevices;
	}
	public void setTaskDevices(String taskDevices) {
		this.taskDevices = taskDevices;
	}
	public String getTaskDeviceNames() {
		return taskDeviceNames;
	}
	public void setTaskDeviceNames(String taskDeviceNames) {
		this.taskDeviceNames = taskDeviceNames;
	}
	public String getTaskResources() {
		return taskResources;
	}
	public void setTaskResources(String taskResources) {
		this.taskResources = taskResources;
	}
	public String getTaskResourceNames() {
		return taskResourceNames;
	}
	public void setTaskResourceNames(String taskResourceNames) {
		this.taskResourceNames = taskResourceNames;
	}
}
