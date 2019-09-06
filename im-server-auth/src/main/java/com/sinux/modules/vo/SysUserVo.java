package com.sinux.modules.vo;

import com.sinux.modules.entity.SysUser;
/**
 * 
* <p>Title: SysUserVo</p>  
* <p>Description: 用户view显示实体</p>  
* @author yexj  
* @date 2019年6月21日
 */
public class SysUserVo extends SysUser{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 角色ID
	 */
	private long roleId;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}
