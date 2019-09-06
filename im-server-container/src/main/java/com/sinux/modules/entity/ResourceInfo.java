package com.sinux.modules.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResourceInfo implements Serializable {
    private Long id;

    /**
     * 资源类型，关联资源类型字典表
     *
     * @mbg.generated
     */
    private Long resourceType;

    /**
     * 资源IP(于资源类型有关系,属于线路，数据性资源有)
     *
     * @mbg.generated
     */
    private String resourceIp;

    /**
     * 资源端口(于资源类型有关系，数据性资源有)
     *
     * @mbg.generated
     */
    private Integer resourcePort;

    /**
     * 资源描述
     *
     * @mbg.generated
     */
    private String resourceDesc;
    
    /**
     * 资源自定义信息
     */
    private String resourceCustom;

    /**
     * 资源状态
     *
     * @mbg.generated
     */
    private Integer resourceStatus;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceType() {
        return resourceType;
    }

    public void setResourceType(Long resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceIp() {
        return resourceIp;
    }

    public void setResourceIp(String resourceIp) {
        this.resourceIp = resourceIp;
    }

    public Integer getResourcePort() {
        return resourcePort;
    }

    public void setResourcePort(Integer resourcePort) {
        this.resourcePort = resourcePort;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public Integer getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(Integer resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getResourceCustom() {
		return resourceCustom;
	}

	public void setResourceCustom(String resourceCustom) {
		this.resourceCustom = resourceCustom;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", resourceType=").append(resourceType);
        sb.append(", resourceIp=").append(resourceIp);
        sb.append(", resourcePort=").append(resourcePort);
        sb.append(", resourceDesc=").append(resourceDesc);
        sb.append(", resourceStatus=").append(resourceStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", resourceCustom=").append(resourceCustom);
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
        ResourceInfo other = (ResourceInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getResourceType() == null ? other.getResourceType() == null : this.getResourceType().equals(other.getResourceType()))
            && (this.getResourceIp() == null ? other.getResourceIp() == null : this.getResourceIp().equals(other.getResourceIp()))
            && (this.getResourcePort() == null ? other.getResourcePort() == null : this.getResourcePort().equals(other.getResourcePort()))
            && (this.getResourceDesc() == null ? other.getResourceDesc() == null : this.getResourceDesc().equals(other.getResourceDesc()))
            && (this.getResourceStatus() == null ? other.getResourceStatus() == null : this.getResourceStatus().equals(other.getResourceStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getResourceCustom() == null ? other.getResourceCustom() == null : this.getResourceCustom().equals(other.getResourceCustom()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getResourceType() == null) ? 0 : getResourceType().hashCode());
        result = prime * result + ((getResourceIp() == null) ? 0 : getResourceIp().hashCode());
        result = prime * result + ((getResourcePort() == null) ? 0 : getResourcePort().hashCode());
        result = prime * result + ((getResourceDesc() == null) ? 0 : getResourceDesc().hashCode());
        result = prime * result + ((getResourceStatus() == null) ? 0 : getResourceStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getResourceCustom() == null) ? 0 : getResourceCustom().hashCode());
        return result;
    }
}