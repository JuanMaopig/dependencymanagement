package com.sinux.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinux.base.support.common.base.BaseController;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.ResourceType;
import com.sinux.modules.server.ResourceTypeServer;


/**
 * <p>Title:DeviceTypeController</p>
 * <p>Description:资源类型控制类</p>
 * @author duwb
 * @date 2019年7月11日
 */
@RestController
@RequestMapping("/container/resource/type")
public class ResourceTypeController extends BaseController<ResourceType>{

	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	@Autowired
	private ResourceTypeServer resourceTypeServer;
	
	/**
	 * 添加设备类型信息
	 * @Title: addResourceType
	 * @Description: 添加资源类型信息
	 * @author duwb
	 * @return 
	 */
	@RequestMapping("/addResourceType")
	@Transactional
	@Override
	@Option(module="资源类型管理",opDesc="添加资源类型")
	public R addOne(@RequestBody ResourceType t) {
		int num=executeJdbcUtil.addOne(t);
		if(num<=0) {
			return R.error("保存资源类型信息失败");
		}
		return R.ok("保存资源类型信息成功");
	}
	
	/**
	 * 查询资源类型字典数据
	 * @Title: getList
	 * @Description: 查询资源类型字典数据
	 * @author duwb
	 * @return 
	 */
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	@Override
	public R getList(@RequestBody Query query) {
		return resourceTypeServer.getList(query);
	}
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 查询所有资源类型字典数据</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @return
	 */
	@RequestMapping("/getAllResourceType")
	public R getAllList() {
		return resourceTypeServer.getAllList();
	}

	@Override
	public R getOne(long id) {
		executeJdbcUtil.getOne(ResourceType.class, id);
		return null;
	}

	@Transactional
	@Override
	@Option(module="资源类型管理",opDesc="删除资源类型")
	public R delOne(long id) {
		executeJdbcUtil.delOne(ResourceType.class, id);
		return null;
	}

	/**
	 * 批量删除资源类型字典数据
	 * @Title: deMoreResourceType
	 * @Description: 批量删除资源类型字典数据
	 * @author duwb
	 * @return 
	 */
	@RequestMapping("/deMoreResourceType")
	@Transactional
	@Override
	@Option(module="资源类型管理",opDesc="批量删除资源类型")
	public R delMore(@RequestBody long[] ids) {
		int[] nums = executeJdbcUtil.delMore(ResourceType.class, ids);
		if(null == nums || nums.length == 0) {
			return R.error("删除资源类型信息失败！");
		}
		return R.ok("删除资源类型信息成功！");
	}

	/**
	 *更新资源类型字典数据
	 * @Title: updateResourceType
	 * @Description: 更新资源类型字典数据
	 * @author duwb
	 * @return 
	 */
	@RequestMapping("/updateResourceType")
	@Transactional
	@Override
	@Option(module="资源类型管理",opDesc="更新资源类型")
	public R updateOne(@RequestBody ResourceType t) {
		int num = executeJdbcUtil.updateOne(t);
		if(num <=0) {
			return R.error("更新资源类型信息失败!");
		}
		return R.ok("更新资源类型信息成功!");
		
	}

}

