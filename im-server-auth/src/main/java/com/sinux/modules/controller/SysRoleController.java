package com.sinux.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.modules.server.SysRoleServer;
import com.sinux.modules.vo.SysRoleVo;

@RestController
@RequestMapping("/auth/role")
public class SysRoleController {

	@Autowired
	private SysRoleServer sysRoleServer;
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param query 参数实体
	 * @return
	 */
	@RequestMapping("/getlist")
	public R getList(@RequestBody Query query) {
		return sysRoleServer.getList(query);
	}
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 查询所有系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @return
	 */
	@RequestMapping("/getAllSysRole")
	public R getAllList() {
		return sysRoleServer.getAllList();
	}
	
	/**
	 * 
	 * <p>Title: addOne</p>  
	 * <p>Description: 保存系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param vo 传递参数对象
	 * @return
	 */
	@Option(module="角色管理",opDesc="添加系统角色")
	@RequestMapping("/addOne")
	public R addOne(@RequestBody SysRoleVo vo) {
		return sysRoleServer.addOne(vo);
	}
	
	/**
	 * 
	 * <p>Title: deleteMore</p>  
	 * <p>Description: 批量删除系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param ids
	 * @return
	 */
	@Option(module="角色管理",opDesc="批量删除系统角色")
	@RequestMapping("/delMoreRole")
	public R delMoreRole(@RequestBody long[] ids) {
		return sysRoleServer.deleteMore(ids);
	}
	
	/**
	 * 
	 * <p>Title: updateOne</p>  
	 * <p>Description: 系统系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param vo
	 * @return
	 */
	@Option(module="角色管理",opDesc="更新系统角色")
	@RequestMapping("/updateOne")
	public R updateOne(@RequestBody SysRoleVo vo) {
		return sysRoleServer.updateOne(vo);
	}
}
