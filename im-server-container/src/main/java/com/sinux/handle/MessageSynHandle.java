package com.sinux.handle;

import java.util.HashMap;

/**
 * 数据缓存
 * @ClassName MessageSynHandle
 * @Description: 数据同步操作，暂时未用
 * @author Tangjc
 * @date 2019年6月12日 下午7:54:07
 */
@Deprecated
public class MessageSynHandle {
	/**
	 * 添加操作
	 * @Title: add
	 * @Description: 添加操作
	 * @author Tangjc
	 * @param synMap 同步集合
	 * @param dataMap 待同步数据
	 * @return void
	 */
	public void add(HashMap<String,Object> synMap,HashMap<String,Object> dataMap){
		synMap.putAll(dataMap);
	}
	/**
	 * 修改操作
	 * @Title: add
	 * @Description: 修改操作
	 * @author Tangjc
	 * @param synMap 同步集合
	 * @param dataMap 待同步数据
	 * @return void
	 */
	public void update(HashMap<String,Object> synMap,HashMap<String,Object> dataMap){
		synMap.putAll(dataMap);
	}
	/**
	 * 修改操作
	 * @Title: add
	 * @Description: 修改操作
	 * @author Tangjc
	 * @param synMap 同步集合
	 * @param dataMap 待同步数据
	 * @return void
	 */
	public void del(HashMap<String,Object> synMap,HashMap<String,Object> dataMap){
		dataMap.forEach((key,value)->{
			synMap.remove(key);
		});
	}
}
