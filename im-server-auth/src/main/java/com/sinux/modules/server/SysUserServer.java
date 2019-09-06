package com.sinux.modules.server;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.modules.vo.SysUserVo;

public interface SysUserServer {

	/**  
	 * 添加系统用户信息
	 * @author: wqj  
	 * @date:2019年6月13日上午9:51:00
	 * @param t
	 * @return  返回添加数量值 >1 表示成功添加， <=0 添加失败
	 */  
	public R addOne(SysUserVo t);
	
	/**  
	 * 多条件查询用户
	 * @author: wqj
	 * @date:2019年6月13日上午10:50:09
	 * @param query
	 * @return  
	 */  
	public R getList(Query query);
	
	/**
	 * 
	 * <p>Title: getUsersByRole</p>  
	 * <p>Description: 根据角色查找用户信息</p>  
	 * @author yexj  
	 * @date 2019年6月14日  
	 * @param query 传入角色参数
	 * @return 返回code=200查询成功，code=500查询失败
	 */
	public R getUsersByRole(Query query);
	
	/**
	 * 
	 * <p>Title: getOne</p>  
	 * <p>Description: 根据ID获取系统用户信息</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param id 选择数据的ID
	 * @return
	 */
	public R getOne(long id);
	
	/**
	 * 
	 * <p>Title: delOne</p>  
	 * <p>Description: 根据ID删除数据</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param id 选择数据的ID
	 * @return
	 */
	public R delOne(long id);
	
	/**
	 * 
	 * <p>Title: delMore</p>  
	 * <p>Description: 批量删除系统用户</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param ids 需要删除的系统用户ID集合
	 * @return
	 */
	public R delMore(long[] ids);
	
	/**
	 * 
	 * <p>Title: updateUser</p>  
	 * <p>Description: 更新系统用户信息</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param t
	 * @return
	 */
	public R updateUser(SysUserVo t);
	
	/**
	 * 
	 * <p>Title: getUserByTaskId</p>  
	 * <p>Description: 通过任务ID查询任务人员信息</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param taskId
	 * @return
	 */
	public R getUserByTaskId(long taskId);
	
	/**
	 * 
	 * <p>Title: getLongUser</p>  
	 * <p>Description: 获取登录用户信息</p>  
	 * @author yexj  
	 * @date 2019年7月23日  
	 * @param userName
	 * @param password
	 * @return
	 */
	public R getLoginUser(String userName,String password);
}
