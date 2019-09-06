package com.sinux.modules.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinux.modules.entity.TaskOpTimeInfo;

public class TaskOpTimeInfoVo extends TaskOpTimeInfo {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 任务描述
     */
    private String taskDesc;
	
	/**
     * 任务开始时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date taskStartTime;

    /**
     * 任务结束时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date taskEndTime;

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public Date getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public Date getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Date taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
    
}
