package com.sinux.modules.dao.impl;

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

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.SysUserDao;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.vo.SysUserVo;

/**
 * 
* <p>Title: SysUserDaoImpl</p>  
* <p>Description: 系统用户dao实现类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@Repository
public class SysUserDaoImpl implements SysUserDao {

	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public long addOne(SysUser t) {
		return executeJdbcUtil.addOne(t);
	}
	
	@Override
	public long addSysUser(SysUser user) {
		final String sql = "insert into sys_user (username,real_name,password,salt,email,mobile,status,create_time,private_key,public_key,certificate) values (?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getRealName());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getSalt());
                ps.setString(5, user.getEmail());
                ps.setString(6, user.getMobile());
                ps.setInt(7, user.getStatus());
                ps.setTimestamp(8, new Timestamp(user.getCreateTime().getTime()));
                ps.setString(9, user.getPrivateKey());
                ps.setString(10, user.getPublicKey());
                ps.setString(11, user.getCertificate());
				return ps;
			}
		},keyHolder);
		
		return keyHolder.getKey().longValue();
	}



	@Override
	public int[] addMore(List<SysUser> ts) {
		return executeJdbcUtil.addMore(ts);
	}

	@Override
	public List<SysUser> getList(Object...params) {
		StringBuilder sql = new StringBuilder("select su.*,sr.role_name from sys_user su LEFT JOIN sys_user_role sur on su.id=sur.user_id LEFT JOIN sys_role sr on sur.role_id = sr.id ");
		sql.append("");
		List<SysUser> list = executeJdbcUtil.getList(SysUser.class,sql.toString(),params);
		return list;
	}

	@Override
	public int getCount(Object...params) {
		String sql = "select count(1) from sys_user";
		return executeJdbcUtil.getCount(sql, params);
	}
	
	@Override
	public List<SysUserVo> getQueryList(Query query){
		StringBuilder sql = new StringBuilder("select su.*,sr.role_name,IFNULL(sr.id,0) AS role_id from sys_user su LEFT JOIN sys_user_role sur on su.id=sur.user_id LEFT JOIN sys_role sr on sur.role_id = sr.id where 1=1 ");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and su.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and su.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(su.username,''),IFNULL(su.real_name,''),IFNULL(sr.role_name,''),IFNULL(su.email,''),IFNULL(su.mobile,'')) like ?");
			params.add("%"+serchText+"%");
		}
		sql.append(" ORDER BY su.create_time DESC");
		int pageNo = Integer.parseInt(query.get("pageNo").toString());
		int limit = Integer.parseInt(query.get("limit").toString());
		//根据页数查询当前需要跳转的记录数
		int cuurentRecordNum = (pageNo - 1) * limit;
		sql.append(" LIMIT ?,?");
		params.add(cuurentRecordNum);
		params.add(limit);
		List<SysUserVo> list = executeJdbcUtil.getList(SysUserVo.class,sql.toString(),params.toArray());
		return list;
	}
	
	@Override
	public int getQueryCount(Query query) {
		StringBuilder sql = new StringBuilder("select count(su.id) from sys_user su LEFT JOIN sys_user_role sur on su.id=sur.user_id LEFT JOIN sys_role sr on sur.role_id = sr.id where 1=1 ");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and su.create_time >= ?");
			params.add(beginTime);
		}
		//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and su.create_time <= ?");
			params.add(endTime);
		}
		//判断查询关键字
		if(null != query.get("serchText") && StringUtils.isNotBlank(query.get("serchText").toString())) {
			String serchText = query.get("serchText").toString();
			sql.append(" and CONCAT(IFNULL(su.username,''),IFNULL(su.real_name,''),IFNULL(sr.role_name,''),IFNULL(su.email,''),IFNULL(su.mobile,'')) like ?");
			params.add("%"+serchText+"%");
		}
		return executeJdbcUtil.getCount(sql.toString(), params.toArray(new Object[params.size()]));
	}

	@Override
	public SysUser getOne(long id) {
		return executeJdbcUtil.getOne(SysUser.class, id);
	}

	@Override
	public int delOne(long id) {
		return executeJdbcUtil.delOne(SysUser.class, id);
	}

	@Override
	public int[] delMore(long[] ids) {
		return executeJdbcUtil.delMore(SysUser.class, ids);
	}
	
	/**
	 * 
	 * <p>Title: delSysUserRole</p>  
	 * <p>Description: 删除用户和角色中间表数据</p>  
	 * @author yexj  
	 * @date 2019年6月23日  
	 * @param ids 需要删除的用户ID集合
	 * @return
	 */
	@Override
	public int[] delSysUserRole(long...ids) {
		String sql = "DELETE FROM sys_user_role WHERE user_id = ?";
		List<Object[]> params = new ArrayList<>();
		for(long id : ids) {
			Object[] param = new Object[] {id};
			params.add(param);
		}
		return executeJdbcUtil.executeBatchUpdateSql(sql, params);
	}

	@Override
	public int updateOne(SysUser t) {
		return executeJdbcUtil.updateOne(t);
	}

}
