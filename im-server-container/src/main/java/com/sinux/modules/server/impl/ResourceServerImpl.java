package com.sinux.modules.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.ResourceDao;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.server.ResourceServer;
import com.sinux.modules.vo.ResourceInfoVo;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * 
* <p>Title: ResourceServerImpl</p>  
* <p>Description: 资源服务接口实现类</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Service
public class ResourceServerImpl implements ResourceServer{

	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ExecuteJdbcUtil excute;
	
	@Override
	public R getResourcesByType(Query query) {
		StringBuilder sql = new StringBuilder("SELECT * FROM resource_info ri where 1=1");
		List<ResourceInfo> ris = new ArrayList<>();
		if(null != query && query.size() > 0 && !"0".equals(query.get("resourceType").toString())) {
			List<Object> params = new ArrayList<>();
			if(StringUtils.isNotBlank(query.get("resourceType").toString())) {
				sql.append(" and ri.resource_type = ?");
				params.add(query.get("resourceType"));
			}
			
			if(StringUtils.isNotBlank(query.get("resourceName").toString())) {
				sql.append(" and ri.resource_desc like ?");
				params.add("%"+query.get("resourceName")+"%");
			}
			ris = excute.getList(ResourceInfo.class, sql.toString(), params.toArray());
		}else {
			ris = excute.getList(ResourceInfo.class, sql.toString());
		}
		if(null == ris) {
			return R.error("查询资源信息失败！");
		}
		return R.ok("查询资源信息成功！").put("ris", ris);
	}
	
	@Override
	public R getlistByPageNo(Query query) {
		List<ResourceInfoVo> rivs = resourceDao.getListByPageNo(query);
		if(null == rivs) {
			return R.error("查询资源信息失败！");
		}
		int num = resourceDao.getTotalByPageNo(query);
		return R.ok("查询资源数据成功！").put("rows", rivs).put("total", num);
	}


	@Override
	public List<ResourceInfo> getListByTaskId(long taskId) {
		return resourceDao.getListByTaskId(taskId);
	}

	@Override
	public List<ResourceInfo> getList() {
		return resourceDao.getList();
	}

	@Override
	public ResourceInfo getOne(long resourceId) {
		return resourceDao.getOne(resourceId);
	}
}
