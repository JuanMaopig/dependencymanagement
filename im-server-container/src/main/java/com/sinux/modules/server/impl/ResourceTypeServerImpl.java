package com.sinux.modules.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.ResourceTypeDao;
import com.sinux.modules.entity.ResourceType;
import com.sinux.modules.server.ResourceTypeServer;

/**
 * 
* <p>Title: ResourceTypeServerImpl</p>  
* <p>Description: 资源类型字典接口服务实现类</p>  
* @author yexj  
* @date 2019年6月17日
 */
@Service
public class ResourceTypeServerImpl implements ResourceTypeServer{

	@Autowired
	private ExecuteJdbcUtil execute;
	
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Override
	public R getAllList() {
		List<ResourceType> rts = execute.getList(ResourceType.class, new Query());
		if(null == rts) {
			return R.error("查询资源类型字典数据失败！");
		}
		return R.ok("查询资源类型字典数据成功！").put("rts", rts);
	}
	
	@Override
	public R getList(@RequestBody Query query) {
		List<ResourceType> rts=resourceTypeDao.getList(query);
		if(null ==rts) {
			return R.error("查询资源类型信息失败！");
		}
		int total =resourceTypeDao.getTotal(query);
		return R.ok("查询资源类型信息成功！").put("rows", rts).put("total", total);
	}

}
