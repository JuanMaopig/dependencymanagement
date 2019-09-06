package com.sinux.modules.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinux.base.support.common.annotation.Ignore;

/**
 * 
* <p>Title: TaskTemplateInfo</p>  
* <p>Description: 任务模版信息实体类</p>  
* @author yexj  
* @date 2019年6月20日
 */
public class TaskTemplateInfo implements Serializable {
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
	 * 任务执行人ID集合
	 */
	private String taskExcuteUesrs;
	/**
	 * 任务执行设备ID集合
	 */
	private String taskDevices;
	/**
	 * 任务执行资源ID集合
	 */
	private String taskResources;
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

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
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
	
    public String getTaskTarget() {
		return taskTarget;
	}

	public void setTaskTarget(String taskTarget) {
		this.taskTarget = taskTarget;
	}

	@Override
	public String toString() {
		return "TaskTemplateInfo [id=" + id + ", taskDesc=" + taskDesc + ", taskStartTime=" + taskStartTime
				+ ", taskEndTime=" + taskEndTime + ", taskExecutePlace=" + taskExecutePlace + ", taskCreateTime="
				+ taskCreateTime + ", taskUpdateTime=" + taskUpdateTime + ", taskName=" + taskName + ", taskType="
				+ taskType + ", securityLevel=" + securityLevel + ", taskExcuteUesrs=" + taskExcuteUesrs
				+ ", taskDevices=" + taskDevices + ", taskResources=" + taskResources + ",taskTarget=" + taskTarget + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((securityLevel == null) ? 0 : securityLevel.hashCode());
		result = prime * result + ((taskCreateTime == null) ? 0 : taskCreateTime.hashCode());
		result = prime * result + ((taskDesc == null) ? 0 : taskDesc.hashCode());
		result = prime * result + ((taskDevices == null) ? 0 : taskDevices.hashCode());
		result = prime * result + ((taskEndTime == null) ? 0 : taskEndTime.hashCode());
		result = prime * result + ((taskExcuteUesrs == null) ? 0 : taskExcuteUesrs.hashCode());
		result = prime * result + ((taskExecutePlace == null) ? 0 : taskExecutePlace.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result + ((taskResources == null) ? 0 : taskResources.hashCode());
		result = prime * result + ((taskStartTime == null) ? 0 : taskStartTime.hashCode());
		result = prime * result + ((taskType == null) ? 0 : taskType.hashCode());
		result = prime * result + ((taskUpdateTime == null) ? 0 : taskUpdateTime.hashCode());
		result = prime * result + ((taskTarget == null) ? 0 : taskTarget.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TaskTemplateInfo other = (TaskTemplateInfo) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (securityLevel == null) {
			if (other.securityLevel != null) {
				return false;
			}
		} else if (!securityLevel.equals(other.securityLevel)) {
			return false;
		}
		if (taskCreateTime == null) {
			if (other.taskCreateTime != null) {
				return false;
			}
		} else if (!taskCreateTime.equals(other.taskCreateTime)) {
			return false;
		}
		if (taskDesc == null) {
			if (other.taskDesc != null) {
				return false;
			}
		} else if (!taskDesc.equals(other.taskDesc)) {
			return false;
		}
		if (taskDevices == null) {
			if (other.taskDevices != null) {
				return false;
			}
		} else if (!taskDevices.equals(other.taskDevices)) {
			return false;
		}
		if (taskEndTime == null) {
			if (other.taskEndTime != null) {
				return false;
			}
		} else if (!taskEndTime.equals(other.taskEndTime)) {
			return false;
		}
		if (taskExcuteUesrs == null) {
			if (other.taskExcuteUesrs != null) {
				return false;
			}
		} else if (!taskExcuteUesrs.equals(other.taskExcuteUesrs)) {
			return false;
		}
		if (taskExecutePlace == null) {
			if (other.taskExecutePlace != null) {
				return false;
			}
		} else if (!taskExecutePlace.equals(other.taskExecutePlace)) {
			return false;
		}
		if (taskName == null) {
			if (other.taskName != null) {
				return false;
			}
		} else if (!taskName.equals(other.taskName)) {
			return false;
		}
		if (taskResources == null) {
			if (other.taskResources != null) {
				return false;
			}
		} else if (!taskResources.equals(other.taskResources)) {
			return false;
		}
		if (taskStartTime == null) {
			if (other.taskStartTime != null) {
				return false;
			}
		} else if (!taskStartTime.equals(other.taskStartTime)) {
			return false;
		}
		if (taskType == null) {
			if (other.taskType != null) {
				return false;
			}
		} else if (!taskType.equals(other.taskType)) {
			return false;
		}
		if (taskUpdateTime == null) {
			if (other.taskUpdateTime != null) {
				return false;
			}
		} else if (!taskUpdateTime.equals(other.taskUpdateTime)) {
			return false;
		}
		if (taskTarget == null) {
			if (other.taskTarget != null) {
				return false;
			}
		} else if (!taskTarget.equals(other.taskTarget)) {
			return false;
		}
		return true;
	}
    
}