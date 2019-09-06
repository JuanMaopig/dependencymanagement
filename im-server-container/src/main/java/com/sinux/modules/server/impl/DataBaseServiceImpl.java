package com.sinux.modules.server.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sinux.modules.dao.DataBaseDao;
import com.sinux.modules.server.DataBaseService;

/**
 * 库操作实现类
 * @ClassName DataBaseServiceImpl
 * @Description: 库操作实现类
 * @author Tangjc
 * @date 2019年7月4日 下午4:23:12
 */
@Service
public class DataBaseServiceImpl implements DataBaseService{
	/** 数据库名 */
	@Value("${spring.datasource.url}")
	private String databaseUrl;
	/** 用户名 */
	@Value("${spring.datasource.username}")
	private String bakUserName;
	/** 密码 */
	@Value("${spring.datasource.password}")
	private String bakPassword;
	
	private String path = DataBaseServiceImpl.class.getResource("/bak").getPath();
	
	@Autowired
	private DataBaseDao dataBaseDao;
	
	/**
	 * 
	 * <p>Title: getDataBaseIp</p>  
	 * <p>Description: 获取数据库连接IP</p>  
	 * @author yexj  
	 * @date 2019年8月7日  
	 * @return
	 */
	private String getDataBaseIp() {
		return databaseUrl.substring(databaseUrl.indexOf("//")+1, databaseUrl.lastIndexOf(":"));
	}
	
	/**
	 * 
	 * <p>Title: getDataBaseName</p>  
	 * <p>Description: 获取数据库连接名</p>  
	 * @author yexj  
	 * @date 2019年8月7日  
	 * @return
	 */
	private String getDataBaseName() {
		String name = databaseUrl.substring(0, databaseUrl.lastIndexOf("?"));
		name = name.substring(name.lastIndexOf("/")+1, name.length());
		return name;
	}
	
	@Override
	public List<Map<String, Object>> getList() {
		List<Map<String,Object>> list = dataBaseDao.getTables(getDataBaseName());
		return list;
	}

	@Override
	public String dataExport(String[] tableNames) {
		Set<String> set = new HashSet<String>();
		for(String tableName:tableNames){
			getList(set,tableName);
		}
		String tables = set.stream().collect(Collectors.joining(" "));
		String cmd = String.format("cmd /c mysqldump -h %s -u%s -p%s  %s %s > %s", getDataBaseIp(), bakUserName, bakPassword, getDataBaseName(), tables, path.substring(1, path.length()).concat("/data.sql"));
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			if(process.isAlive()){
				process.waitFor();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path.concat("/data.sql");
	}
	/**
	 * 递归循环查找关系表
	 * @Title: relateList
	 * @Description: 递归循环查找关系表
	 * @author Tangjc
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getList(Set<String> set,String tableName){
		set.add(tableName);
		List<Map<String,Object>> relateList = dataBaseDao.getRelateTables(getDataBaseName(), tableName);
		for(Map<String,Object> map:relateList){
			if(map.containsKey("peferenced_table_name") && StringUtils.isNotEmpty(map.get("peferenced_table_name").toString())){
				String tmpTableName = map.get("peferenced_table_name").toString();
				if(!set.contains(tmpTableName)){
					getList(set, tmpTableName);
				}
			}
		}
		return relateList;
	}
}
