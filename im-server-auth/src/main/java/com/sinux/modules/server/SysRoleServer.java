package com.sinux.modules.server;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.modules.vo.SysRoleVo;

/**
 * 
* <p>Title: SysRoleServer</p>  
* <p>Description: 系统角色服务接口类</p>  
* @author yexj  
* @date 2019年6月17日
 */
public interface SysRoleServer {
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param query 传递参数信息map，分页传递limit，pageNo两个参数
	 * @return
	 */
	public R getList(Query query);

	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 获取所有系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @return code=200查询成功，code=500查询失败
	 */
	public R getAllList();
	
	/**
	 * 
	 * <p>Title: addOne</p>  
	 * <p>Description: 新增角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param vo 传递参数对象
	 * @return
	 */
	public R addOne(SysRoleVo vo);
	
	/**
	 * 
	 * <p>Title: deleteMore</p>  
	 * <p>Description: 删除选中的系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param ids
	 * @return
	 */
	public R deleteMore(long[] ids);
	
	/**
	 * 
	 * <p>Title: updateOne</p>  
	 * <p>Description: 更改系统角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param vo
	 * @return
	 */
	public R updateOne(SysRoleVo vo);
	
	/**
	 * 
	 * <p>Title: getLongUserRoles</p>  
	 * <p>Description: 获取登录用户角色信息</p>  
	 * @author yexj  
	 * @date 2019年7月23日  
	 * @param id
	 * @return
	 */
	public R getLongUserRoles(long userId);
}
