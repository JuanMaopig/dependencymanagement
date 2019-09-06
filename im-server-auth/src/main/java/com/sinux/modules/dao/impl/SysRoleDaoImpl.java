package com.sinux.modules.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.vo.SysRoleVo;

/**
 * 
* <p>Title: SysRoleDaoImpl</p>  
* <p>Description: 系统角色信息dao层实现</p>  
* @author yexj  
* @date 2019年7月1日
 */
@Repository
public class SysRoleDaoImpl {
	
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	
	public List<SysRoleVo> getList(Query query) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT role.* FROM ( SELECT sr.*,");
		sql.append("(SELECT GROUP_CONCAT(su.real_name) FROM sys_user_role sur left join sys_user su on sur.user_id = su.id WHERE sur.role_id = sr.id) AS sys_user_name,");
		sql.append("(SELECT GROUP_CONCAT(sm.name) FROM sys_role_menu srm left join sys_menu sm on srm.menu_id = sm.id WHERE srm.role_id = sr.id) AS role_perms_name,");
		sql.append("(SELECT GROUP_CONCAT(sm.id) FROM sys_role_menu srm left join sys_menu sm on srm.menu_id = sm.id WHERE srm.role_id = sr.id) AS role_perms FROM sys_role sr ) AS role WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and role.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and role.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(role.role_name,''),IFNULL(role.remark,''),IFNULL(role.sys_user_name,''),IFNULL(role.role_perms_name,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY role.create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		List<SysRoleVo> sysRoles =  executeJdbcUtil.getList(SysRoleVo.class, sql.toString(), params.toArray());
		return sysRoles;
	}
	
	public int getCount(Query query) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(role.id) FROM ( SELECT sr.*,");
		sql.append("(SELECT GROUP_CONCAT(su.real_name) FROM sys_user_role sur left join sys_user su on sur.user_id = su.id WHERE sur.role_id = sr.id) AS sys_user_name,");
		sql.append("(SELECT GROUP_CONCAT(sm.name) FROM sys_role_menu srm left join sys_menu sm on srm.menu_id = sm.id WHERE srm.role_id = sr.id) AS role_perms_name,");
		sql.append("(SELECT GROUP_CONCAT(sm.id) FROM sys_role_menu srm left join sys_menu sm on srm.menu_id = sm.id WHERE srm.role_id = sr.id) AS role_perms FROM sys_role sr ) AS role WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and role.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and role.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(role.role_name,''),IFNULL(role.remark,''),IFNULL(role.sys_user_name,''),IFNULL(role.role_perms_name,'')) like ?");
			params.add("%"+serchText+"%");
		}
		return executeJdbcUtil.getCount(sql.toString(), params.toArray());
	}
}
