package com.sinux.modules.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.vo.DeviceInfoVo;



/**
 * 
* <p>Title: DeviceDao</p>  
* <p>Description: 设备数据持久层</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Repository
public class DeviceDao{

	@Autowired
	private ExecuteJdbcUtil execute;
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页获取设备信息列表</p>  
	 * @author yexj  
	 * @date 2019年7月2日   
	 * @param query 传入参数
	 * @return
	 */
	public List<DeviceInfoVo> getList(Query query){
		StringBuilder sql = new StringBuilder("SELECT di.*,dt.name AS device_type_name FROM device_info di LEFT JOIN device_type dt on di.device_type = dt.id WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and di.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and di.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(di.device_name,''),IFNULL(dt.name,''),IFNULL(di.device_ip,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY di.create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
	
		return execute.getList(DeviceInfoVo.class, sql.toString(), params.toArray());
	}
	
	/**
	 * 
	 * <p>Title: getTotal</p>  
	 * <p>Description: 分页获取总记录数</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param query 传入参数
	 * @return
	 */
	public int getTotal(Query query) {
		StringBuilder sql = new StringBuilder("SELECT count(di.id) FROM device_info di LEFT JOIN device_type dt on di.device_type = dt.id WHERE 1=1");
		
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and di.create_time >= ?"); 
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and di.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(di.device_name,''),IFNULL(dt.name,''),IFNULL(di.device_ip,'')) like ?");
			params.add("%"+serchText+"%");
		}
		return execute.getCount(sql.toString(), params.toArray());
	}
	
	/**
	 * 
	 * <p>Title: getDeviceByIp</p>  
	 * <p>Description: 通过设备IP查找设备</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param ip
	 * @return
	 */
	public DeviceInfo getDeviceByIp(String ip) {
		String sql = "SELECT * FROM device_info di WHERE di.device_ip = ?";
		return execute.getOne(DeviceInfo.class, sql, new Object[] {ip});
	}
}
