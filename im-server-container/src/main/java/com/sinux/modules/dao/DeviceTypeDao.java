package com.sinux.modules.dao;


import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.DeviceType;

/*
 * Title:DeviceTypeDao
 * 设备类型数据持久层
*@author duwb
*@date 2019年7月4
*/
@Repository
public class DeviceTypeDao {
	

	@Autowired
	private ExecuteJdbcUtil execute;
	
	public List<DeviceType> getList(Query query){
		StringBuilder sql=new StringBuilder("SELECT * FROM device_type WHERE 1=1");
		
		List<Object> params=new ArrayList<>();
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" AND CONCAT(IFNULL(name,''),IFNULL(device_desc,'')) like ?");
			params.add("%"+serchText+"%");
		}
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		return execute.getList(DeviceType.class, sql.toString(), params.toArray());
		
}
	
	public int getTotal(Query query) {
		StringBuilder sql = new StringBuilder("SELECT count(id) FROM device_type WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(name,''),IFNULL(device_desc,'')) like ?");
			params.add("%"+serchText+"%");
		}
		return execute.getCount(sql.toString(), params.toArray());
	}
	


}
