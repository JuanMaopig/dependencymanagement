package com.sinux.modules.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.DefaultRowMapper;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.TaskInfo;
import com.sinux.modules.entity.TaskOpTimeInfo;
import com.sinux.modules.entity.TaskTemplateInfo;
import com.sinux.modules.vo.TaskInfoVo;
import com.sinux.modules.vo.TaskOpTimeInfoVo;

/**
 * 
* <p>Title: TaskDao</p>  
* <p>Description: 任务数据持久层</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Repository
public class TaskDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	
	/**
	 * 
	 * <p>Title: addTask</p>  
	 * <p>Description: 保存任务</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param tk
	 * @return 返回保存任务主键
	 */
	public Long addTask(TaskInfo tk) {
		final String sql = "insert into task_info (task_start_time,task_end_time,task_execute_place,task_create_time,task_name,task_target,task_desc,task_status) values (?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setTimestamp(1, new Timestamp(tk.getTaskStartTime().getTime()));
				ps.setTimestamp(2, new Timestamp(tk.getTaskEndTime().getTime()));
                ps.setString(3, tk.getTaskExecutePlace());
                ps.setTimestamp(4, new Timestamp(tk.getTaskCreateTime().getTime()));
                ps.setString(5, tk.getTaskName());
                ps.setString(6, tk.getTaskTarget());
                ps.setString(7, tk.getTaskDesc());
                ps.setInt(8, tk.getTaskStatus());
				return ps;
			}
		},keyHolder);
		
		return keyHolder.getKey().longValue();
	}
	
	public Long addTaskTemplate(TaskTemplateInfo tk) {
		final String sql = "insert into task_template_info (task_start_time,task_end_time,task_execute_place,task_create_time,task_name,task_target,task_desc,task_resources,task_devices,task_excute_uesrs) values (?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setTimestamp(1, new Timestamp(tk.getTaskStartTime().getTime()));
				ps.setTimestamp(2, new Timestamp(tk.getTaskEndTime().getTime()));
                ps.setString(3, tk.getTaskExecutePlace());
                ps.setTimestamp(4, new Timestamp(tk.getTaskCreateTime().getTime()));
                ps.setString(5, tk.getTaskName());
                ps.setString(6, tk.getTaskTarget());
                ps.setString(7, tk.getTaskDesc());
                ps.setString(8, tk.getTaskResources());
                ps.setString(9, tk.getTaskDevices());
                ps.setString(10, tk.getTaskExcuteUesrs());
				return ps;
			}
		},keyHolder);
		
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询任务信息</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param query 查询参数
	 * @param tableName 查询表名
	 * @return 返回查询结果集合
	 */
	public List<TaskInfoVo> getList(Query query, String tableName){
		StringBuilder sql = new StringBuilder("SELECT task.* FROM ( SELECT ti.*,");
		sql.append("(SELECT GROUP_CONCAT(tri.resource_id) FROM task_resource_info tri WHERE tri.task_id = ti.id) AS task_resources,");
		sql.append("(SELECT GROUP_CONCAT(ri.resource_desc) FROM task_resource_info tri LEFT JOIN resource_info ri ON tri.resource_id = ri.id WHERE tri.task_id = ti.id) AS task_resource_names,");
		sql.append("(SELECT GROUP_CONCAT(tdi.device_id) FROM task_device_info tdi WHERE tdi.task_id = ti.id) AS task_devices,");
		sql.append("(SELECT GROUP_CONCAT(di.device_name) FROM task_device_info tdi LEFT JOIN device_info di ON tdi.device_id = di.id WHERE tdi.task_id = ti.id) AS task_device_names,");
		sql.append("(SELECT GROUP_CONCAT(tui.user_id) FROM task_user_info tui WHERE tui.task_id = ti.id) AS task_excute_uesrs,");
		sql.append("(SELECT GROUP_CONCAT(su.username) FROM task_user_info tui LEFT JOIN sys_user su ON tui.user_id = su.id WHERE tui.task_id = ti.id) AS task_excute_uesr_names");
		sql.append(" FROM "+tableName+" ti ) AS task WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and task.task_create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and task.task_create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(task.task_desc,''),IFNULL(task.task_execute_place,''),IFNULL(task.task_resource_names,''),IFNULL(task.task_device_names,''),IFNULL(task.task_excute_uesr_names,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY task.task_create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		List<TaskInfoVo> tivs = jdbcTemplate.query(sql.toString(),new DefaultRowMapper<TaskInfoVo>(TaskInfoVo.class),params.toArray(new Object[params.size()]));
		return tivs;
	}
	
	/**
	 * 
	 * <p>Title: getListNum</p>  
	 * <p>Description: 分页查询总记录数据</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @param query 查询参数
	 * @param tableName 查询表名
	 * @return 返回查询结果记录总数
	 */
	public int getListNum(Query query, String tableName) {
		StringBuilder sql = new StringBuilder("SELECT count(task.id) FROM ( SELECT ti.*,");
		sql.append("(SELECT GROUP_CONCAT(tri.resource_id) FROM task_resource_info tri WHERE tri.task_id = ti.id) AS task_resources,");
		sql.append("(SELECT GROUP_CONCAT(ri.resource_desc) FROM task_resource_info tri LEFT JOIN resource_info ri ON tri.resource_id = ri.id WHERE tri.task_id = ti.id) AS task_resource_names,");
		sql.append("(SELECT GROUP_CONCAT(tdi.device_id) FROM task_device_info tdi WHERE tdi.task_id = ti.id) AS task_devices,");
		sql.append("(SELECT GROUP_CONCAT(di.device_name) FROM task_device_info tdi LEFT JOIN device_info di ON tdi.device_id = di.id WHERE tdi.task_id = ti.id) AS task_device_names,");
		sql.append("(SELECT GROUP_CONCAT(tui.user_id) FROM task_user_info tui WHERE tui.task_id = ti.id) AS task_excute_uesrs,");
		sql.append("(SELECT GROUP_CONCAT(su.username) FROM task_user_info tui LEFT JOIN sys_user su ON tui.user_id = su.id WHERE tui.task_id = ti.id) AS task_excute_uesr_names");
		sql.append(" FROM "+tableName+" ti ) AS task WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and task.task_create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and task.task_create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(task.task_desc,''),IFNULL(task.task_execute_place,''),IFNULL(task.task_resource_names,''),IFNULL(task.task_device_names,''),IFNULL(task.task_excute_uesr_names,'')) like ?");
			params.add("%"+serchText+"%");
		}
		int count = jdbcTemplate.queryForObject(sql.toString(), params.toArray(new Object[params.size()]), Integer.class);
		return count;
	}
	
	/**
	 * 
	 * <p>Title: getTemplateList</p>  
	 * <p>Description: 分页查询任务模版数据</p>  
	 * @author yexj  
	 * @date 2019年6月21日  
	 * @param query 查询条件
	 * @return
	 */
	public List<TaskInfoVo> getTemplateList(Query query){
		StringBuilder sql = new StringBuilder("SELECT task.* FROM ( SELECT ti.*,");
		sql.append("(SELECT GROUP_CONCAT(ri.resource_desc) FROM resource_info ri WHERE FIND_IN_SET(ri.id,ti.task_resources) ) AS task_resource_names,");
		sql.append("(SELECT GROUP_CONCAT(di.device_name) FROM device_info di WHERE FIND_IN_SET(di.id,ti.task_devices) ) AS task_device_names,");
		sql.append("(SELECT GROUP_CONCAT(su.username) FROM sys_user su WHERE FIND_IN_SET(su.id,ti.task_excute_uesrs) ) AS task_excute_uesr_names");
		sql.append(" FROM task_template_info ti ) AS task WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and task.task_create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and task.task_create_time <= ?");
			params.add(endTime);
		}
		//判断关键字查询
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(task.task_execute_place,''),IFNULL(task.task_resource_names,''),IFNULL(task.task_device_names,''),IFNULL(task.task_excute_uesr_names,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY task.task_create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数，计算当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		List<TaskInfoVo> tivs = jdbcTemplate.query(sql.toString(),new DefaultRowMapper<TaskInfoVo>(TaskInfoVo.class),params.toArray(new Object[params.size()]));
		return tivs;
	}
	
	/**
	 * 
	 * <p>Title: getTemplateListNum</p>  
	 * <p>Description: 查询任务模版数据记录数</p>  
	 * @author yexj  
	 * @date 2019年6月21日  
	 * @param query 查询条件
	 * @return 
	 */
	public int getTemplateListNum(Query query) {
		StringBuilder sql = new StringBuilder("SELECT count(task.id) FROM ( SELECT ti.*,");
		sql.append("(SELECT GROUP_CONCAT(ri.resource_desc) FROM resource_info ri WHERE FIND_IN_SET(ri.id,ti.task_resources) ) AS task_resource_names,");
		sql.append("(SELECT GROUP_CONCAT(di.device_name) FROM device_info di WHERE FIND_IN_SET(di.id,ti.task_devices) ) AS task_device_names,");
		sql.append("(SELECT GROUP_CONCAT(su.username) FROM sys_user su WHERE FIND_IN_SET(su.id,ti.task_excute_uesrs) ) AS task_excute_uesr_names");
		sql.append(" FROM task_template_info ti ) AS task WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and task.task_create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and task.task_create_time <= ?");
			params.add(endTime);
		}
		//判断关键字查询
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(task.task_execute_place,''),IFNULL(task.task_resource_names,''),IFNULL(task.task_device_names,''),IFNULL(task.task_excute_uesr_names,'')) like ?");
			params.add("%"+serchText+"%");
		}
		int count = jdbcTemplate.queryForObject(sql.toString(), params.toArray(new Object[params.size()]), Integer.class);
		return count;
	}
	/**
	 * 根据用户id获取任务列表
	 * @Title: getListByUserId
	 * @Description: 根据用户id获取任务列表
	 * @author Tangjc
	 * @param userId 用户id
	 * @return
	 * @return List<TaskInfo> 任务列表
	 */
	public List<TaskInfo> getListByUserId(long userId){
		List<TaskInfo> list = executeJdbcUtil.getList(TaskInfo.class, "select ti.id id,task_start_time,task_end_time,task_execute_place,task_create_time,task_update_time,task_name,task_type,task_status from task_info ti left join task_user_info tui on ti.id = tui.task_id where ti.task_status != "+Constants.TASK_COMPLETE_STATUS+" and tui.user_id = ?", 
				userId);
		return list;
	}
	/**
	 * 修改任务
	 * @Title: updateTask
	 * @Description: 修改任务
	 * @author Tangjc
	 * @param taskInfo 任务对象
	 * @return
	 * @return int
	 */
	public int updateTask(TaskInfo taskInfo){
		return executeJdbcUtil.updateOne(taskInfo);
	}
	/**
	 * 根据任务id获取实体
	 * @Title: getOne
	 * @Description: 根据任务id获取实体
	 * @author Tangjc
	 * @param taskId 任务id
	 * @return
	 * @return TaskInfo
	 */
	public TaskInfo getOne(long taskId){
		List<TaskInfo> list = executeJdbcUtil.getList(TaskInfo.class, "select id,task_start_time,task_end_time,task_execute_place,task_create_time,task_update_time,task_name,task_type,task_status from task_info where id = ?", taskId);
		return list.get(0);
	}
	/**
	 * 添加任务资源中间表
	 * @Title: addTaskResource
	 * @Description: 添加任务资源中间表
	 * @author Tangjc
	 * @param taskId 任务id
	 * @param ResourceId 资源id
	 * @return void
	 */
	public void addTaskResource(long taskId, long resourceId){
		executeJdbcUtil.executeUpdateSql("insert into task_resource_info(null,?,?);", taskId, resourceId);
	}
	
	/**
	 * 
	 * <p>Title: getTaskOpTime</p>  
	 * <p>Description: 查询任务线性时间上操作状态</p>  
	 * @author yexj  
	 * @date 2019年8月2日  
	 * @param taskIds
	 * @return
	 */
	public List<TaskOpTimeInfoVo> getTaskOpTime(long[] taskIds) {
		List<TaskOpTimeInfoVo> tifs = new ArrayList<>();
		for(long id : taskIds) {
			String sql = "SELECT toti.task_id,ti.task_desc,ti.task_start_time,ti.task_end_time,toti.op_time_desc FROM task_op_time_info toti LEFT JOIN task_info ti ON toti.task_id = ti.id WHERE ti.id = ?";
			TaskOpTimeInfoVo vo = executeJdbcUtil.getOne(TaskOpTimeInfoVo.class, sql, new Object[] {id});
			if(null != vo) {
				tifs.add(vo);
			}
		}
		return tifs;
	}
	
	/**
	 * 
	 * <p>Title: getTaskOpTime</p>  
	 * <p>Description: 通过任务ID查找该任务在线性时间内操作状态</p>  
	 * @author yexj  
	 * @date 2019年8月7日  
	 * @param taskId 任务ID
	 * @return
	 */
	public TaskOpTimeInfo getTaskOpTime(Long taskId) {
		String sql = "SELECT * FROM task_op_time_info WHERE task_id = ?";
		return executeJdbcUtil.getOne(TaskOpTimeInfo.class, sql, new Object[] {taskId});
	}
}
