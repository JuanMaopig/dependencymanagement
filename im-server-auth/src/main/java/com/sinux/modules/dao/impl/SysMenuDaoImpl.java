package com.sinux.modules.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.SysMenuDao;
import com.sinux.modules.entity.SysMenu;

/**
 * 
* <p>Title: SysMenuDaoImpl</p>  
* <p>Description: 系统菜单dao实现类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@Repository
public class SysMenuDaoImpl implements SysMenuDao {
	
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;

	@Override
	public long addOne(SysMenu t) {
		return executeJdbcUtil.addOne(t);
	}

	@Override
	public int[] addMore(List<SysMenu> ts) {
		return executeJdbcUtil.addMore(ts);
	}

	@Override
	public List<SysMenu> getList(Object...params) {
		String sql = "select * from sys_menu";
		return executeJdbcUtil.getList(SysMenu.class, sql, params);
	}

	@Override
	public int getCount(Object...params) {
		String sql = "select count(id) from sys_menu";
		return executeJdbcUtil.getCount(sql, params);
	}

	@Override
	public SysMenu getOne(long id) {
		return executeJdbcUtil.getOne(SysMenu.class, id);
	}

	@Override
	public int delOne(long id) {
		return executeJdbcUtil.delOne(SysMenu.class, id);
	}

	@Override
	public int[] delMore(long[] ids) {
		return executeJdbcUtil.delMore(SysMenu.class, ids);
	}

	@Override
	public int updateOne(SysMenu t) {
		return executeJdbcUtil.updateOne(t);
	}

}
