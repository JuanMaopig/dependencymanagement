package com.sinux.modules.vo;

import com.sinux.modules.entity.DeviceInfo;

public class DeviceInfoVo extends DeviceInfo{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String deviceTypeName;

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	
}
