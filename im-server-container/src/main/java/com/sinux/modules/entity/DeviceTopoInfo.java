package com.sinux.modules.entity;

import java.io.Serializable;

/**
 * 
* <p>Title: DeviceTopoInfo</p>  
* <p>Description: 设备topo信息数据实体</p>  
* @author yexj  
* @date 2019年7月4日
 */
public class DeviceTopoInfo implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 设备节点信息
	 */
	private String deviceNode;
	/**
	 * 设备节点之间的连线信息
	 */
	private String deviceLink;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDeviceNode() {
		return deviceNode;
	}

	public void setDeviceNode(String deviceNode) {
		this.deviceNode = deviceNode;
	}

	public String getDeviceLink() {
		return deviceLink;
	}

	public void setDeviceLink(String deviceLink) {
		this.deviceLink = deviceLink;
	}

	@Override
	public String toString() {
		return "DeviceTopoInfo [id=" + id + ", userId=" + userId + ", deviceNode=" + deviceNode + ", deviceLink="
				+ deviceLink + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceLink == null) ? 0 : deviceLink.hashCode());
		result = prime * result + ((deviceNode == null) ? 0 : deviceNode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		DeviceTopoInfo other = (DeviceTopoInfo) obj;
		if (deviceLink == null) {
			if (other.deviceLink != null) {
				return false;
			}
		} else if (!deviceLink.equals(other.deviceLink)) {
			return false;
		}
		if (deviceNode == null) {
			if (other.deviceNode != null) {
				return false;
			}
		} else if (!deviceNode.equals(other.deviceNode)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

}
