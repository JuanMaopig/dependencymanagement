package com.sinux.modules.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.util.ExecuteJdbcUtil;

/**
 * 库级别查询
 * @ClassName DataBaseDao
 * @Description: 库级别查询
 * @author Tangjc
 * @date 2019年7月4日 下午2:27:32
 */
@Repository
public class DataBaseDao {
	@Autowired
	ExecuteJdbcUtil jdbcUtil;
	/**
	 * 获取库里所有表
	 * @Title: getTables
	 * @Description: 获取库里所有表
	 * @author Tangjc
	 * @param databaseName 数据库名
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getTables(String databaseName){
		return jdbcUtil.getTables(databaseName);
	}
	
	/**
	 * 获取有关联的所有表
	 * @Title: getRelateTables
	 * @Description: 获取有关联的所有表 
	 * @author Tangjc
	 * @param databaseName 库名
	 * @param tableName 表名
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getRelateTables(String databaseName, String tableName){
		return jdbcUtil.getRelateTables(databaseName, tableName);
	}
}
