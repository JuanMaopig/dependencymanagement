package com.sinux.modules.server;

import java.util.List;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.modules.entity.ResourceInfo;

public interface ResourceServer{
	
	/**
	 * 
	 * <p>Title: getResourcesByType</p>  
	 * <p>Description: 根据资源类型查找资源信息</p>  
	 * @author yexj  
	 * @date 2019年6月14日  
	 * @param query 查询条件类
	 * @return 返回资源集合， 异常返回code=500的编码
	 */
	public R getResourcesByType(Query query);
	
	/**
	 * 
	 * <p>Title: getlistByPageNo</p>  
	 * <p>Description: 分页查询资源信息</p>  
	 * @author yexj  
	 * @date 2019年7月8日  
	 * @param query 传递查询资源参数
	 * @return
	 */
	public R getlistByPageNo(Query query);
	
	/**
	 * 根据任务id获取资源列表
	 * @Title: getListByTaskId
	 * @Description: 根据任务id获取资源列表
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return
	 * @return List<ResourceInfo> 资源列表
	 */
	public List<ResourceInfo> getListByTaskId(long taskId);
	
	/**
	 * 获取所有资源
	 * @Title: getList
	 * @Description: 获取所有资源
	 * @author Tangjc
	 * @return
	 * @return List<ResourceInfo>
	 */
	public List<ResourceInfo> getList();
	/**
	 * 根据资源id获取资源对象
	 * @Title: getOne
	 * @Description: 根据资源id获取资源对象
	 * @author Tangjc
	 * @param resourceId 资源id
	 * @return
	 * @return ResourceInfo
	 */
	public ResourceInfo getOne(long resourceId);
}
