package com.sinux.modules.server;

import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 * @ClassName DataBaseService
 * @Description: 数据库操作
 * @author Tangjc
 * @date 2019年7月4日 下午4:20:50
 */
public interface DataBaseService {
	/**
	 * 获取表分页
	 * @Title: getList
	 * @Description: 获取表分页
	 * @author Tangjc
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getList();
	/**
	 * 数据导出
	 * @Title: dataExport
	 * @Description: 数据导出
	 * @author Tangjc
	 * @param tableNames 表名
	 * @return String 数据库文件地址
	 */
	public String dataExport(String[] tableNames);
}
