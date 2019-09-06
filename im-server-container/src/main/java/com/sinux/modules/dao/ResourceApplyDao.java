package com.sinux.modules.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sinux.base.support.common.base.BaseDao;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.ResourceApply;
/**
 * 资源申请dao
 * @ClassName ResourceApplyDao
 * @Description: 资源申请dao
 * @author Tangjc
 * @date 2019年6月12日 下午3:19:32
 */
@Repository
public class ResourceApplyDao implements BaseDao<ResourceApply>{
	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	
	@Autowired
	private ExecuteJdbcUtil execute;
	/**
	 * 添加资源申请
	 * @Title: addOne
	 * @Description: 添加资源申请
	 * @author Tangjc
	 * @param t 资源申请对象
	 * @return int
	 */
	@Override
	public long addOne(ResourceApply t) { 
		try{
			return executeJdbcUtil.addOneReturnKeys(t);
		}catch(Exception e){
			e.printStackTrace();
			return -1L;
		}
	}

	@Override
	public int[] addMore(List<ResourceApply> ts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceApply> getList(Object...params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount(Object...params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResourceApply getOne(long id) {
		executeJdbcUtil.getOne(ResourceApply.class,id);
		return null;
	}

	@Override
	public int delOne(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] delMore(long[] ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateOne(ResourceApply t) {
		executeJdbcUtil.updateOne(t);
		return 0;
	}

}
