package com.sinux.modules.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.ResourceType;

/**
 * Title:ResourceTypeDao
 * 设备类型数据持久层
 * @author duwb
 * @date 2019年7月8
 */
@Repository
public class ResourceTypeDao {

	@Autowired
	private ExecuteJdbcUtil execute;
	
	public List<ResourceType> getList(Query query){
		StringBuilder sql=new StringBuilder("SELECT*FROM resource_type WHERE 1=1");
		
		List<Object> params=new ArrayList<>();
		//判断查询关键字
		if(null!=query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText=query.get("serchText").toString();
			sql.append(" AND CONCAT(IFNULL(name,''),IFNULL(resource_desc,''))like ?");
			params.add("%"+serchText+"%");
		}
		int pageNo=Integer.parseInt(query.get("pageNo").toString());
		int limit=Integer.parseInt(query.get("limit").toString());
		
		//
		int cuurentRecordNum =(pageNo -1)*limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		return execute.getList(ResourceType.class,sql.toString(), params.toArray());
		
	}
	
		public int getTotal(Query query) {
			StringBuilder sql =new StringBuilder("SELECT count(id) FROM resource_type WHERE 1=1");
			List<Object> params =new ArrayList<>();
			
			if(null !=query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
				String serchText =query.get("serchText").toString();
				sql.append(" AND CONCAT(IFNULL(name,''),IFNULL(resource_desc,''))like ?");
				params.add("%"+serchText+"%");
			}
			return execute.getCount(sql.toString(), params.toArray());
					
		}
}
