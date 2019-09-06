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
import com.sinux.modules.entity.DeviceType;
import com.sinux.modules.server.DeviceTypeServer;



@RestController
@RequestMapping("/container/device/type")
public class DeviceTypeController extends BaseController<DeviceType>{

	@Autowired
	private ExecuteJdbcUtil executeJdbcUtil;
	@Autowired
	private DeviceTypeServer deviceTypeServer;
	
	@RequestMapping("/addDevice")
	@Transactional
	@Override
	@Option(module="设备类型管理",opDesc="添加设备类型")
	public R addOne(@RequestBody DeviceType t) {

		int num=executeJdbcUtil.addOne(t);
		if(num<=0) {
			return R.error("保存设备类型信息失败");
		}
		return R.ok("保存设备类型信息成功");
	}
	
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	@Override
	public R getList(@RequestBody Query query) {
		return deviceTypeServer.getList(query);
	}

	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 查询所有设备类型字典数据</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @return
	 */
	@RequestMapping("/getAllDeviceType")
	public R getAllList() {
		return deviceTypeServer.getAllList();
	}

	@Override
	public R getOne(long id) {
		executeJdbcUtil.getOne(DeviceType.class, id);
		return null;
	}

	@Transactional
	@Override
	@Option(module="设备类型管理",opDesc="删除设备类型")
	public R delOne(long id) {
		executeJdbcUtil.delOne(DeviceType.class, id);
		return null;
	}


	@RequestMapping("/deMoreDevice")
	@Transactional
	@Override
	@Option(module="设备类型管理",opDesc="批量删除设备类型")
	public R delMore(@RequestBody long[] ids) {
		int[] nums = executeJdbcUtil.delMore(DeviceType.class, ids);
		if(null == nums || nums.length == 0) {
			return R.error("删除设备类型信息失败！");
		}
		return R.ok("删除设备类型信息成功！");
	}
	
	@RequestMapping("/updateDevice")
	@Transactional
	@Override
	@Option(module="设备类型管理",opDesc="更新设备类型信息")
	public R updateOne(@RequestBody DeviceType t) {
		int num = executeJdbcUtil.updateOne(t);
		if(num <= 0) {
			return R.error("更新设备类型信息失败！");
		}
		return R.ok("更新设备类型信息成功！");
	}

}
