package com.sinux.modules.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DeviceInfo implements Serializable {
    private Long id;

    /**
     * 设备IP
     *
     * @mbg.generated
     */
    private String deviceIp;

    /**
     * 设备名
     *
     * @mbg.generated
     */
    private String deviceName;

    /**
     * 设备状态
     *
     * @mbg.generated
     */
    private Integer deviceStatus;

    /**
     * 设备经度
     *
     * @mbg.generated
     */
    private Double deviceLongitude;

    /**
     * 设备纬度
     *
     * @mbg.generated
     */
    private Double deviceLatitude;
    /**
     * 设备图形
     */
    private String deviceImage;
    
    /**
     * 设备是否可以分享0:不共享，1：共享
     */
    private Integer deviceShare;
    /**
     * 设备类型
     */
    private Long deviceType;

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

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(Double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public Double getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(Double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
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

    public String getDeviceImage() {
		return deviceImage;
	}

	public void setDeviceImage(String deviceImage) {
		this.deviceImage = deviceImage;
	}
	public Integer getDeviceShare() {
		return deviceShare;
	}

	public void setDeviceShare(Integer deviceShare) {
		this.deviceShare = deviceShare;
	}
	public Long getDeviceType() {
		return deviceType;
	}
	
	public void setDeviceType(Long deviceType) {
		this.deviceType = deviceType;
	}
	

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceIp=").append(deviceIp);
        sb.append(", deviceName=").append(deviceName);
        sb.append(", deviceStatus=").append(deviceStatus);
        sb.append(", deviceLongitude=").append(deviceLongitude);
        sb.append(", deviceLatitude=").append(deviceLatitude);
        sb.append(", deviceShare=").append(deviceShare);
        sb.append(", deviceImage=").append(deviceImage);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        DeviceInfo other = (DeviceInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceIp() == null ? other.getDeviceIp() == null : this.getDeviceIp().equals(other.getDeviceIp()))
            && (this.getDeviceName() == null ? other.getDeviceName() == null : this.getDeviceName().equals(other.getDeviceName()))
            && (this.getDeviceStatus() == null ? other.getDeviceStatus() == null : this.getDeviceStatus().equals(other.getDeviceStatus()))
            && (this.getDeviceLongitude() == null ? other.getDeviceLongitude() == null : this.getDeviceLongitude().equals(other.getDeviceLongitude()))
            && (this.getDeviceLatitude() == null ? other.getDeviceLatitude() == null : this.getDeviceLatitude().equals(other.getDeviceLatitude()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeviceImage() == null ? other.getDeviceImage() == null : this.getDeviceImage().equals(other.getDeviceImage()))
            && (this.getDeviceShare() == null ? other.getDeviceShare() == null : this.getDeviceShare().equals(other.getDeviceShare()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceIp() == null) ? 0 : getDeviceIp().hashCode());
        result = prime * result + ((getDeviceName() == null) ? 0 : getDeviceName().hashCode());
        result = prime * result + ((getDeviceStatus() == null) ? 0 : getDeviceStatus().hashCode());
        result = prime * result + ((getDeviceLongitude() == null) ? 0 : getDeviceLongitude().hashCode());
        result = prime * result + ((getDeviceLatitude() == null) ? 0 : getDeviceLatitude().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeviceImage() == null) ? 0 : getDeviceImage().hashCode());
        result = prime * result + ((getDeviceShare() == null) ? 0 : getDeviceShare().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        return result;
    }
}