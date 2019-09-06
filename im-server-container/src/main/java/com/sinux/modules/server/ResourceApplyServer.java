package com.sinux.modules.server;

import com.sinux.modules.entity.ResourceApply;

public interface ResourceApplyServer {

	/**
	 * 添加资源申请表
	 * @Title: addOne
	 * @Description: 添加资源申请表
	 * @author Tangjc
	 * @param resourceId
	 * @param taskId
	 * @return int
	 */
	public long addOne(long resourceId, long taskId);
	/**
	 * 修改资源申请表 
	 * @Title: updateOne
	 * @Description: 一般用于修改状态
	 * @author Tangjc
	 * @param ra 资源申请实体对象
	 * @return void
	 */
	public void updateOne(ResourceApply ra);
	
	/**
	 * 获取资源申请实体
	 * @Title: getOne
	 * @Description: 获取资源申请实体
	 * @author Tangjc
	 * @param id
	 * @return ResourceApply
	 */
	public ResourceApply getOne(long id);
}
