package com.sinux.modules.server.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinux.modules.dao.ResourceApplyDao;
import com.sinux.modules.entity.ResourceApply;
import com.sinux.modules.server.ResourceApplyServer;

/**
 * 
* <p>Title: ResourceApplyServerImpl</p>  
* <p>Description: 资源申请服务接口实现类</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Service
public class ResourceApplyServerImpl implements ResourceApplyServer{

	@Autowired
	private ResourceApplyDao resourceApplyDao;
	/**
	 * 添加资源申请表
	 * @Title: addOne
	 * @Description: 添加资源申请表
	 * @author Tangjc
	 * @param ra
	 * @return void
	 */
	@Override
	public long addOne(long resourceId, long taskId){
		ResourceApply ra = new ResourceApply();
		Date date = new Date();
		ra.setApplyTime(date);
		ra.setState(0);
		ra.setrId(resourceId);
		ra.settId(taskId);
		return resourceApplyDao.addOne(ra);
	}
	/**
	 * 修改资源申请表 
	 * @Title: updateOne
	 * @Description: 一般用于修改状态
	 * @author Tangjc
	 * @param ra 资源申请实体对象
	 * @return void
	 */
	@Override
	public void updateOne(ResourceApply ra){
		resourceApplyDao.updateOne(ra);
	}
	@Override
	public ResourceApply getOne(long id) {
		return resourceApplyDao.getOne(id);
	}
}
