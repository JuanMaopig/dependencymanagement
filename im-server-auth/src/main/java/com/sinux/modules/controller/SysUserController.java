package com.sinux.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinux.base.support.common.base.BaseController;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.server.SysUserServer;
import com.sinux.modules.vo.SysUserVo;

/**
 * 
* <p>Title: SysUserController</p>  
* <p>Description: 系统用户控制类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@RestController
@RequestMapping("/auth/user")
public class SysUserController extends BaseController<SysUser> {

	@Autowired
	private SysUserServer sysUserServer;
	
	@Option(module="用户管理",opDesc="添加系统用户")
	@RequestMapping("/addUser")
	public R addOne(@RequestBody SysUserVo t) {
		return sysUserServer.addOne(t);
	}
	
	@RequestMapping("/getList")
	@Override
	public R getList(@RequestBody Query query) {
		return sysUserServer.getList(query);
	}

	@RequestMapping("/getUserByRole")
	public R getUserByRole(@RequestBody Query query) {
		return sysUserServer.getUsersByRole(query);
	}

	@RequestMapping("/getUser")
	@Override
	public R getOne(long id) {
		return sysUserServer.getOne(id);
	}

	@Option(module="用户管理",opDesc="删除系统用户")
	@RequestMapping("/delUser")
	@Override
	public R delOne(long id) {
		return sysUserServer.delOne(id);
	}

	@Option(module="用户管理",opDesc="批量删除系统用户")
	@RequestMapping("/delMoreUser")
	@Override
	public R delMore(@RequestBody long[] ids) {
		return sysUserServer.delMore(ids);
	}

	@Option(module="用户管理",opDesc="更新系统用户")
	@RequestMapping("/updateUser")
	public R updateOne(@RequestBody SysUserVo t) {
		return sysUserServer.updateUser(t);
	}
	
	/**
	 * 
	 * <p>Title: getUserByTaskId</p>  
	 * <p>Description: 通过任务ID查询任务人员信息</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param taskId 任务ID
	 * @return 返回查询结果，code == 200查询成功。code==500查询失败
	 */
	@RequestMapping("/getUserByTaskId")
	public R getUserByTaskId(@RequestParam(value="taskId")long taskId) {
		return sysUserServer.getUserByTaskId(taskId);
	}

	/**
	 * 暂时未使用
	 */
	@Deprecated
	@Override
	public R updateOne(SysUser t) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 暂时未使用
	 */
	@Deprecated
	@Override
	public R addOne(SysUser t) {
		// TODO Auto-generated method stub
		return null;
	}

}
