package com.sinux.modules.entity;

import java.io.Serializable;

/**
 * 
* <p>Title: ResourceType</p>  
* <p>Description: 资源类型实体类</p>  
* @author yexj  
* @date 2019年7月11日
 */
public class ResourceType implements Serializable{

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
	private String resourceDesc;
	

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

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((resourceDesc == null) ? 0 : resourceDesc.hashCode());
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
		ResourceType other = (ResourceType) obj;
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
		if (resourceDesc == null) {
			if (other.resourceDesc != null) {
				return false;
			}
		} else if (!resourceDesc.equals(other.resourceDesc)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ResourceType [id=" + id + ", name=" + name + ", resourceDesc=" + resourceDesc + "]";
	}


}
