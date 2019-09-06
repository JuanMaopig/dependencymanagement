package com.sinux.modules.entity.param;

import com.sinux.modules.entity.SysUser;

/**
 * 
* <p>Title: SysUserParam</p>  
* <p>Description: 系统用户接受参数实体</p>  
* @author yexj  
* @date 2019年6月23日
 */
public class SysUserParam extends SysUser {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 系统角色ID
	 */
	private long roleId;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleIds(long sysRoleId) {
		this.roleId = sysRoleId;
	}

}
