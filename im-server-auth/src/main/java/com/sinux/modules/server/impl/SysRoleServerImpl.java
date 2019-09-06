package com.sinux.modules.server.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.impl.SysRoleDaoImpl;
import com.sinux.modules.entity.SysRole;
import com.sinux.modules.server.SysRoleServer;
import com.sinux.modules.vo.SysRoleVo;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * 
* <p>Title: SysRoleServerImpl</p>  
* <p>Description: 系统角色服务接口实现类</p>  
* @author yexj  
* @date 2019年6月17日
 */
@Service
public class SysRoleServerImpl implements SysRoleServer{

	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	@Autowired
	private SysRoleDaoImpl sysRoleDaoImpl;
	
	
	@Override
	public R getList(Query query) {
		List<SysRoleVo> sysRoles = sysRoleDaoImpl.getList(query);
		if(null == sysRoles) {
			return R.error("查询系统角色信息失败！");
		}
		int total = sysRoleDaoImpl.getCount(query);
		return R.ok("查询系统角色信息成功！").put("rows", sysRoles).put("total", total);
	}



	@Override
	public R getAllList() {
		String sql = "SELECT * FROM sys_role";
		List<SysRole> srs = executeJdbcUtil.getList(SysRole.class, sql);
		if(null == srs) {
			return R.error("查询系统角色信息失败！");
		}
		return R.ok("查询系统角色信息成功！").put("srs", srs);
	}

	@Transactional
	@Override
	public R addOne(SysRoleVo vo) {
		SysRole role = new SysRole();
		role.setRoleName(vo.getRoleName());
		role.setRemark(vo.getRemark());
		role.setCreateTime(new Date());
		//保存系统角色信息
		long id = executeJdbcUtil.addOneReturnKeys(role);
		if(id == 0) {
			return R.error("保存系统角色信息失败！");
		}
		String rolePerms = vo.getRolePerms();
		if(StringUtils.isNotBlank(rolePerms)) {
			String[] ids = rolePerms.split(",");
			List<Object[]> params = new ArrayList<>();
			for(String idStr : ids) {
				params.add(new Object[] {id,idStr});
			}
			String sql = "insert into sys_role_menu (role_id,menu_id) values (?,?)";
			int[] nums = executeJdbcUtil.executeBatchUpdateSql(sql, params);
			if(null == nums) {
				return R.error("保存系统角色信息中间表数据失败！");
			}
		}
		return R.ok("保存系统角色信息成功！");
	}


	@Transactional
	@Override
	public R deleteMore(long[] ids) {
		int[] nums = executeJdbcUtil.delMore(SysRole.class, ids);
		if(null == nums) {
			return R.error("删除系统角色信息失败！");
		}
		List<Object[]> params = new ArrayList<>();
		for(long id : ids) {
			params.add(new Object[] {id});
		}
		//删除角色和菜单中间表数据
		String sql = "delete from sys_role_menu where role_id = ?";
		nums = executeJdbcUtil.executeBatchUpdateSql(sql, params);
		if(null == nums) {
			return R.error("删除系统角色和系统菜单中间表数据失败！");
		}
		
		//删除角色和用户中间表数据
		sql = "delete from sys_user_role where role_id = ?";
		nums = executeJdbcUtil.executeBatchUpdateSql(sql, params);
		if(null == nums) {
			return R.error("删除系统角色和系统用户中间表数据失败！");
		}
		
		return R.ok("删除系统角色信息成功！");
	}


	@Transactional
	@Override
	public R updateOne(SysRoleVo vo) {
		SysRole role = new SysRole();
		role.setId(vo.getId());
		role.setRoleName(vo.getRoleName());
		role.setRemark(vo.getRemark());
		int num = executeJdbcUtil.updateOne(role);
		if(num == 0) {
			return R.error("更新系统角色信息失败！");
		}
		//删除角色和菜单中间表数据
		String sql = "delete from sys_role_menu where role_id = ?";
		executeJdbcUtil.executeUpdateSql(sql, new Object[] {vo.getId()});

		String rolePerms = vo.getRolePerms();
		if(StringUtils.isNotBlank(rolePerms)) {
			String[] ids = rolePerms.split(",");
			List<Object[]> params = new ArrayList<>();
			for(String idStr : ids) {
				params.add(new Object[] {vo.getId(),idStr});
			}
			sql = "insert into sys_role_menu (role_id,menu_id) values (?,?)";
			int[] nums = executeJdbcUtil.executeBatchUpdateSql(sql, params);
			if(null == nums) {
				return R.error("保存系统角色信息中间表数据失败！");
			}
		}
		return R.ok("更新系统角色信息成功！");
	}

	@Override
	public R getLongUserRoles(long userId) {
		String sql = "SELECT sr.* FROM sys_role sr LEFT JOIN sys_user_role sur ON sr.id=sur.role_id LEFT JOIN sys_user su ON sur.user_id=su.id WHERE su.id = ?";
		List<SysRole> srs = executeJdbcUtil.getList(SysRole.class, sql, new Object[] {userId});
		if(null == srs) {
			return R.error("查询登录人角色信息失败！");
		}
		return R.ok("查询登录人角色信息成功！").put("srs", srs);
	}

	
}
