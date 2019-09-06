package com.sinux.modules.vo;

import com.sinux.modules.entity.ResourceInfo;

public class ResourceInfoVo extends ResourceInfo{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 资源类型名称
	 */
	private String resourceTypeName;
	/**
	 * 资源使用量
	 */
	private long resourceNum;

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public long getResourceNum() {
		return resourceNum;
	}

	public void setResourceNum(long resourceNum) {
		this.resourceNum = resourceNum;
	}
	
}
