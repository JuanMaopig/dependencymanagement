package com.sinux.modules.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinux.base.support.common.annotation.Ignore;

public class TaskInfo implements Serializable {
    private Long id;
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

    /**
     * 任务执行地点
     *
     * @mbg.generated
     */
    private String taskExecutePlace;

    /**
     * 任务创建时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date taskCreateTime;

    /**
     * 任务更新时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date taskUpdateTime;

    /**
     * 任务名称
     *
     * @mbg.generated
     */
    private String taskName;

    /**
     * 任务类型（暂定任务没有类型）
     *
     * @mbg.generated
     */
    @Ignore
    private Long taskType;
    
    /**
     * 任务安全等级
     */
    private Integer securityLevel;
    
    /**
     * 任务状态(0:未开始，1：进行中，2完成)
     */
    private Integer taskStatus;
    
    /**
     * 任务目标
     */
    private String taskTarget;
    
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTaskExecutePlace() {
        return taskExecutePlace;
    }

    public void setTaskExecutePlace(String taskExecutePlace) {
        this.taskExecutePlace = taskExecutePlace;
    }

    public Date getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public Date getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(Date taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskType() {
        return taskType;
    }

    public void setTaskType(Long taskType) {
        this.taskType = taskType;
    }
    
    public Integer getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(Integer securityLevel) {
		this.securityLevel = securityLevel;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskTarget() {
		return taskTarget;
	}

	public void setTaskTarget(String taskTarget) {
		this.taskTarget = taskTarget;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskStartTime=").append(taskStartTime);
        sb.append(", taskEndTime=").append(taskEndTime);
        sb.append(", taskExecutePlace=").append(taskExecutePlace);
        sb.append(", taskCreateTime=").append(taskCreateTime);
        sb.append(", taskUpdateTime=").append(taskUpdateTime);
        sb.append(", taskName=").append(taskName);
        sb.append(", taskType=").append(taskType);
        sb.append(", securityLevel=").append(securityLevel);
        sb.append(", taskStatus=").append(taskStatus);
        sb.append(", taskDesc=").append(taskDesc);
        sb.append(", taskTarget=").append(taskTarget);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TaskInfo other = (TaskInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskStartTime() == null ? other.getTaskStartTime() == null : this.getTaskStartTime().equals(other.getTaskStartTime()))
            && (this.getTaskEndTime() == null ? other.getTaskEndTime() == null : this.getTaskEndTime().equals(other.getTaskEndTime()))
            && (this.getTaskExecutePlace() == null ? other.getTaskExecutePlace() == null : this.getTaskExecutePlace().equals(other.getTaskExecutePlace()))
            && (this.getTaskCreateTime() == null ? other.getTaskCreateTime() == null : this.getTaskCreateTime().equals(other.getTaskCreateTime()))
            && (this.getTaskUpdateTime() == null ? other.getTaskUpdateTime() == null : this.getTaskUpdateTime().equals(other.getTaskUpdateTime()))
            && (this.getTaskName() == null ? other.getTaskName() == null : this.getTaskName().equals(other.getTaskName()))
            && (this.getTaskType() == null ? other.getTaskType() == null : this.getTaskType().equals(other.getTaskType()))
            && (this.getSecurityLevel()== null ? other.getSecurityLevel() == null : this.getSecurityLevel().equals(other.getSecurityLevel()))
            && (this.getTaskStatus()== null ? other.getTaskStatus() == null : this.getTaskStatus().equals(other.getTaskStatus()))
            && (this.getTaskDesc()== null ? other.getTaskDesc() == null : this.getTaskDesc().equals(other.getTaskDesc()))
            && (this.getTaskTarget()== null ? other.getTaskTarget() == null : this.getTaskTarget().equals(other.getTaskTarget()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskStartTime() == null) ? 0 : getTaskStartTime().hashCode());
        result = prime * result + ((getTaskEndTime() == null) ? 0 : getTaskEndTime().hashCode());
        result = prime * result + ((getTaskExecutePlace() == null) ? 0 : getTaskExecutePlace().hashCode());
        result = prime * result + ((getTaskCreateTime() == null) ? 0 : getTaskCreateTime().hashCode());
        result = prime * result + ((getTaskUpdateTime() == null) ? 0 : getTaskUpdateTime().hashCode());
        result = prime * result + ((getTaskName() == null) ? 0 : getTaskName().hashCode());
        result = prime * result + ((getTaskType() == null) ? 0 : getTaskType().hashCode());
        result = prime * result + ((getSecurityLevel() == null) ? 0 : getSecurityLevel().hashCode());
        result = prime * result + ((getTaskStatus() == null) ? 0 : getTaskStatus().hashCode());
        result = prime * result + ((getTaskDesc() == null) ? 0 : getTaskDesc().hashCode());
        result = prime * result + ((getTaskTarget() == null) ? 0 : getTaskTarget().hashCode());
        return result;
    }
}