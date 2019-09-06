package com.sinux.modules.server.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.dao.SysUserDao;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.feign.ImContainerFeign;
import com.sinux.modules.server.SysUserServer;
import com.sinux.modules.vo.SysUserVo;
import com.sinux.utils.Md5Util;

import cn.hutool.crypto.asymmetric.RSA;

/**
 * 
* <p>Title: SysUserServerImpl</p>  
* <p>Description: 系统用户接口实现类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@Service
public class SysUserServerImpl implements SysUserServer {
	/**日志*/
	private Logger log = LoggerFactory.getLogger(SysUserServerImpl.class);
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private ImContainerFeign feign;
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	
	@Override
	public R getUsersByRole(Query query) {
		StringBuilder sql = new StringBuilder("select su.* from sys_user su left join sys_user_role sur on su.id = sur.user_id");
		List<SysUser> users = new ArrayList<>();
		if(null != query && query.size() > 0 && !"0".equals(query.get("roleType").toString())) {
			if("null".equals(query.get("roleType").toString())) {
				sql.append(" where sur.role_id is null");
				users = executeJdbcUtil.getList(SysUser.class, sql.toString());
			}else {
				sql.append(" where sur.role_id = ?");
				Object[] params = new Object[] {query.get("roleType")};
				users = executeJdbcUtil.getList(SysUser.class, sql.toString(), params);
			}
		}else {
			users = executeJdbcUtil.getList(SysUser.class, sql.toString());
		}
		if(null == users) {
			return R.error("查询系统用户信息失败！");
		}
		return R.ok("查询系统用户信息成功！").put("users", users);
	}
	
	@Transactional
	@Override
	public R addOne(SysUserVo t) {
		SysUser user = new SysUser();
		//获取用户私钥和公钥
		RSA rsa=new RSA();
		String privateKey = rsa.getPrivateKeyBase64();
		String publicKey = rsa.getPublicKeyBase64();
		user.setPrivateKey(privateKey);
		user.setPublicKey(publicKey);
		user.setSalt(t.getUsername());
		user.setCreateTime(new Date());
		user.setCertificate(t.getCertificate());
		user.setEmail(t.getEmail());
		user.setMobile(t.getMobile());
		String password = "";
		try {
			password = Md5Util.md5Hex(t.getUsername(),t.getPassword(),5);
		} catch (Exception e) {
			log.error("MD5加密异常",e);
		}
		user.setPassword(password);
		user.setRealName(t.getRealName());
		user.setUsername(t.getUsername());
		user.setStatus(Constants.SYS_USER_ENABLE);
		long id = sysUserDao.addSysUser(user);
		if(id == 0) {
			return R.error("添加系统用户信息失败！");
		}
		long roleId = t.getRoleId();
		if(roleId != 0) {
			String sql = "insert into sys_user_role (user_id,role_id) values (?,?)";
			Object[] param = new Object[] {id,roleId};
			int num = executeJdbcUtil.executeUpdateSql(sql, param);
			if(num == 0) {
				return R.error("保存系统用户，角色中间表信息失败！");
			}
		}
		return R.ok("保存系统用户信息成功！");
	}
	
	@Transactional
	@Override
	public R updateUser(SysUserVo t) {
		SysUser user = new SysUser();
		//获取用户私钥和公钥
		RSA rsa=new RSA();
		String privateKey = rsa.getPrivateKeyBase64();
		String publicKey = rsa.getPublicKeyBase64();
		user.setId(t.getId());
		user.setPrivateKey(privateKey);
		user.setPublicKey(publicKey);
		user.setSalt(t.getUsername());
		user.setCreateTime(new Date());
		user.setCertificate(t.getCertificate());
		user.setEmail(t.getEmail());
		user.setMobile(t.getMobile());
		if(StringUtils.isNotBlank(t.getPassword())) {
			String password = "";
			try {
				password = Md5Util.md5Hex(t.getUsername(),t.getPassword(),5);
			} catch (Exception e) {
				log.error("MD5加密异常",e);
			}
			user.setPassword(password);
		}
		user.setRealName(t.getRealName());
		user.setUsername(t.getUsername());
		user.setStatus(Constants.SYS_USER_ENABLE);
		int num = sysUserDao.updateOne(user);
		if(num == 0) {
			return R.error("更新系统用户信息失败！");
		}
		long roleId = t.getRoleId();
		if(roleId != 0) {
			String sql = "insert into sys_user_role (user_id,role_id) values (?,?)";
			Object[] param = new Object[] {user.getId(),roleId};
			sysUserDao.delSysUserRole(user.getId());
			num = executeJdbcUtil.executeUpdateSql(sql, param);
			if(num == 0) {
				return R.error("保存系统用户，角色中间表信息失败！");
			}
		}
		return R.ok("更新系统用户信息成功！");
	}

	@Override
	public R getList(Query query) {
		List<SysUserVo> suvs = sysUserDao.getQueryList(query);
		int count = sysUserDao.getQueryCount(query);
		if(null == suvs) {
			return R.error("分页查询系统用户失败！");
		}
		return R.ok("分页查询系统用户成功！").put("rows", suvs).put("total", count);
	}
	
	@Override
	public R getOne(long id) {
		String sql = "select su.*,sr.role_name from sys_user su LEFT JOIN sys_user_role sur on su.id=sur.user_id LEFT JOIN sys_role sr on sur.role_id = sr.id where su.id = ?";
		SysUserVo user = executeJdbcUtil.getOne(SysUserVo.class, sql, id);
		if(null == user) {
			return R.error("查询系统用户信息失败！");
		}
		return R.ok("查询系统用户信息成功！").put("user", user);
	}
	
	@Transactional
	@Override
	public R delOne(long id) {
		int num = sysUserDao.delOne(id);
		if(num <= 0) {
			return R.ok("删除系统用户失败！");
		}
		sysUserDao.delSysUserRole(id);
		return R.ok("删除系统用户成功！");
	}
	
	@Transactional
	@Override
	public R delMore(long[] ids) {
		int[] nums = sysUserDao.delMore(ids);
		if(null == nums || nums.length <= 0) {
			return R.ok("批量删除系统用户失败！");
		}
		sysUserDao.delSysUserRole(ids);
		return R.ok("批量删除系统用户成功！");
	}

	@Override
	public R getUserByTaskId(long taskId) {
		String sql = "SELECT su.* FROM task_user_info tui LEFT JOIN sys_user su ON tui.user_id = su.id WHERE tui.task_id = ?";
		List<SysUser> sus = executeJdbcUtil.getList(SysUser.class, sql, new Object[] {taskId});
		if(null == sus) {
			return R.error("根据ID查询任务人员信息失败！");
		}
		return R.ok("根据任务ID查询任务人员信息成功！").put("sus", sus);
	}

	@Override
	public R getLoginUser(String userName, String password) {
		Query query = new Query();
		query.put("username", userName);
		query.put("password", password);
		List<SysUser> users = executeJdbcUtil.getList(SysUser.class, query);
		if(null == users || users.isEmpty()) {
			return R.error("查询登录用户信息异常！");
		}
		R r = feign.getTaskByUserId(users.get(0).getId());
		Object tasks = r.get("tasks");
		StringBuilder ids = new StringBuilder();
		String json = JSON.toJSONString(tasks);
		JSONArray array = JSON.parseArray(json);
		for(int i=0;i<array.size();i++) {
			JSONObject task = array.getJSONObject(i);
			ids.append(task.get("id")).append(",");
		}
		return R.ok("查询登录用户信息成功！").put("user", users.get(0)).put("taskIds", ids.substring(0, ids.lastIndexOf(",")));
	}
	
	
	
}
