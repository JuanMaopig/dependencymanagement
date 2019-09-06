package com.sinux.modules.entity;

import java.io.Serializable;
/**
 * 
* <p>Title: DeviceType</p>  
* <p>Description: 设备类型实体类</p>  
* @author yexj  
* @date 2019年7月11日
 */
public class DeviceType implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	/**
	 * 设备类型名
	 */
	private String name;
	/**
	 * 设备类型描述
	 */
	private String deviceDesc;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceDesc() {
		return deviceDesc;
	}

	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceDesc == null) ? 0 : deviceDesc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DeviceType other = (DeviceType) obj;
		if (deviceDesc == null) {
			if (other.deviceDesc != null) {
				return false;
			}
		} else if (!deviceDesc.equals(other.deviceDesc)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DeviceType [id=" + id + ", name=" + name + ", deviceDesc=" + deviceDesc + "]";
	}


}
