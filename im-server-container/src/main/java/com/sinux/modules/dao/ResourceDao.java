package com.sinux.modules.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.vo.ResourceInfoVo;

/**
 * 
* <p>Title: ResourceDao</p>  
* <p>Description: 任务资源数据持久层</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Repository
public class ResourceDao{
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	/**
	 * 根据任务id获取资源列表
	 * @Title: getListByTaskId
	 * @Description: 根据任务id获取资源列表
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return
	 * @return getList<ResourceInfo> 资源列表
	 */
	public List<ResourceInfo> getListByTaskId(long taskId){
		return executeJdbcUtil.getList(ResourceInfo.class, "select ri.id id,resource_type,resource_ip,resource_port,resource_desc,resource_status,create_time,update_time from resource_info ri left join task_resource_info tri on ri.id = tri.resource_id where tri.task_id = ?", 
				taskId);
	}
	/**
	 * 获取所有资源
	 * @Title: getList
	 * @Description: 获取所有资源
	 * @author Tangjc
	 * @return
	 * @return List<ResourceInfo>
	 */
	public List<ResourceInfo> getList(){
		return executeJdbcUtil.getList(ResourceInfo.class, "select id,resource_type,resource_ip,resource_port,resource_desc,resource_status,create_time,update_time from resource_info");
	}
	
	/**
	 * 根据资源id获取资源列表
	 * @Title: getOne
	 * @Description: 根据资源id获取资源列表
	 * @author Tangjc
	 * @param resourceId 资源列表
	 * @return
	 * @return ResourceInfo
	 */
	public ResourceInfo getOne(long resourceId){
		return executeJdbcUtil.getOne(ResourceInfo.class, resourceId);
	}
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页获取设备信息列表</p>  
	 * @author yexj  
	 * @date 2019年7月2日   
	 * @param query 传入参数
	 * @return
	 */
	public List<ResourceInfoVo> getListByPageNo(Query query){
		StringBuilder sql = new StringBuilder("SELECT ri.*,rt.name AS resource_type_name FROM resource_info ri LEFT JOIN resource_type rt ON ri.resource_type = rt.id WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and ri.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and ri.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(ri.resource_desc,''),IFNULL(rt.name,''),IFNULL(ri.resource_ip,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY ri.create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		return executeJdbcUtil.getList(ResourceInfoVo.class, sql.toString(), params.toArray());
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
	public int getTotalByPageNo(Query query) {
		StringBuilder sql = new StringBuilder("SELECT count(ri.id) FROM resource_info ri LEFT JOIN resource_type rt ON ri.resource_type = rt.id WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and ri.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and ri.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(ri.resource_desc,''),IFNULL(rt.name,''),IFNULL(ri.resource_ip,'')) like ?");
			params.add("%"+serchText+"%");
		}
		return executeJdbcUtil.getCount(sql.toString(), params.toArray());
	}
}
