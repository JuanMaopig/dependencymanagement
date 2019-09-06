package com.sinux.modules.vo;

import com.sinux.modules.entity.SysRole;

/**
 * 
* <p>Title: SysRoleVo</p>  
* <p>Description: 系统角色显示VO实体</p>  
* @author yexj  
* @date 2019年7月1日
 */
public class SysRoleVo extends SysRole{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 权限ID集合，用逗号隔开
	 */
	private String rolePerms;
	
	/**
	 * 权限名称集合，用逗号隔开
	 */
	private String rolePermsName;
	/**
	 * 系统用户名称集合，用逗号隔开
	 */
	private String sysUserName;
	
	public String getRolePerms() {
		return rolePerms;
	}
	public void setRolePerms(String rolePerms) {
		this.rolePerms = rolePerms;
	}
	public String getRolePermsName() {
		return rolePermsName;
	}
	public void setRolePermsName(String rolePermsName) {
		this.rolePermsName = rolePermsName;
	}
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
}
