package com.sinux.modules.dao;

import java.util.List;

import com.sinux.base.support.common.base.BaseDao;
import com.sinux.base.support.common.entity.Query;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.vo.SysUserVo;

public interface SysUserDao extends BaseDao<SysUser>{
	
	/**
	 * 
	 * <p>Title: addSysUser</p>  
	 * <p>Description: 添加系统用户信息</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param user 系统用户信息数据
	 * @return
	 */
	public long addSysUser(SysUser user);

	/**
	 * 
	 * <p>Title: getQueryList</p>  
	 * <p>Description: 条件查询系统用户</p>  
	 * @author yexj  
	 * @date 2019年6月22日  
	 * @param query
	 * @return
	 */
	public List<SysUserVo> getQueryList(Query query);
	
	/**
	 * 
	 * <p>Title: getQueryCount</p>  
	 * <p>Description: 条件查询系统用户记录数</p>  
	 * @author yexj  
	 * @date 2019年6月22日  
	 * @param query
	 * @return
	 */
	public int getQueryCount(Query query);
	
	/**
	 * 
	 * <p>Title: delSysUserRole</p> 
	 * <p>Description: 删除系统用户，系统角色中间表数据</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param ids 需要删除的系统用户ID集合
	 * @return
	 */
	public int[] delSysUserRole(long...ids);
}
