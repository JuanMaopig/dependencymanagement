package com.sinux.modules.controller;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.base.BaseController;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.entity.DeviceTopoInfo;
import com.sinux.modules.server.DeviceServer;
import com.sinux.utils.ClientCacheManager;

/**
 * 
* <p>Title: DeviceController</p>  
* <p>Description: 设备控制类</p>  
* @author yexj  
* @date 2019年6月4日
 */
@RestController
@RequestMapping("/container/device")
public class DeviceController extends BaseController<DeviceInfo>{

	@Autowired
	private ExecuteJdbcUtil execute;
	@Autowired
	private DeviceServer deviceServer;
	
	/**
	 * 
	 * <p>Title: uploadMenuImage</p>  
	 * <p>Description: 上传设备图片</p>  
	 * @author yexj  
	 * @date 2019年6月25日  
	 * @param file 上传文件
	 * @return
	 */
	//@Option(opDesc="上传设备图片",module="设备管理")
	@RequestMapping("/uploadMenuImage")
	public R uploadMenuImage(@RequestParam("file")MultipartFile file, HttpServletRequest req) {
		return deviceServer.uploadMenuImage(file, req);
	}
	
	/**
	 * 
	 * <p>Title: getMenuImageFile</p>  
	 * <p>Description: 用于获取设备图片</p>  
	 * @author yexj  
	 * @date 2019年6月26日  
	 * @param imageName
	 */
	@RequestMapping("/getImage/{imageName}")
	public void getMenuImageFile(@PathVariable("imageName")String imageName,HttpServletRequest req,HttpServletResponse res) {
		deviceServer.getMenuImageFile(imageName, req, res);
	}
	
	@RequestMapping("/addDevice")
	@Transactional
	@Override
	@Option(opDesc="添加设备",module="设备管理")
	public R addOne(@RequestBody DeviceInfo t) {
		t.setDeviceStatus(Constants.DEVICE_ONLINE_STATUS);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		int num = execute.addOne(t);
		if(num <= 0) {
			return R.error("保存设备信息失败！");
		}
		return R.ok("保存设备信息成功！");
	}

	/**
	 * 
	 * <p>Title: getDevicesByType</p>  
	 * <p>Description: 通过设备类型获取设备信息</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param query 传递传输
	 * @return
	 */
	@RequestMapping("/getDevicesByType")
	public R getDevicesByType(@RequestBody Query query) {
		return deviceServer.getDevicesByType(query);
	}
	
	@RequestMapping("/getList")
	@Override
	public R getList(@RequestBody Query query) {
		return deviceServer.getList(query);
	}

	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 获取所有设备信息</p>  
	 * @author yexj  
	 * @date 2019年7月3日  
	 * @return
	 */
	@RequestMapping("/getAllList")
	public R getAllList() {
		return deviceServer.getAllList();
	}
	
	/**
	 * 
	 * <p>Title: getListByDeviceName</p>  
	 * <p>Description: 通过设备名获取设备信息</p>  
	 * @author yexj  
	 * @date 2019年7月21日  
	 * @param query
	 * @return
	 */
	@RequestMapping("/getListByDeviceName")
	public R getListByDeviceName(@RequestBody Query query) {
		return deviceServer.getListByDeviceName(query);
	}
	
	/**
	 * 
	 * <p>Title: getTop5ListByDeviceId</p>  
	 * <p>Description: 获取设备最新top任务信息</p>  
	 * @author yexj  
	 * @date 2019年7月21日  
	 * @param query
	 * @return
	 */
	@RequestMapping("/getTop5ListByDeviceId")
	public R getTop5ListByDeviceId(@RequestBody Query query) {
		return deviceServer.getTop5ListByDeviceId(query);
	}
	
	@RequestMapping("/getOneDevice")
	@Override
	public R getOne(long id) {
		DeviceInfo de = execute.getOne(DeviceInfo.class, id);
		if(null == de) {
			return R.error("获取设备信息失败！");
		}
		return R.ok("获取设备信息成功！").put("device", de);
	}

	@RequestMapping("/delDevice")
	@Transactional
	@Override
	@Option(opDesc="删除设备",module="设备管理")
	public R delOne(long id) {
		int num = execute.delOne(DeviceInfo.class, id);
		if(num <= 0) {
			return R.error("删除设备信息失败！");
		}
		//删除缓存数据
		Set<String> childrenIds = ClientCacheManager.delDeviceByIds(id+"");
		if(!childrenIds.isEmpty()) {
			long[] ids = new long[childrenIds.size()];
			int index = 0;
			for(String idStr : childrenIds) {
				ids[index] = Long.parseLong(idStr);
				index++;
				
			}
			//批量删除客户端下关联的目标设备
			execute.delMore(DeviceInfo.class, ids);
		}
		return R.ok("删除设备信息成功！");
	}

	@RequestMapping("/deMoreDevice")
	@Transactional
	@Override
	@Option(opDesc="批量删除设备",module="设备管理")
	public R delMore(@RequestBody long[] ids) {
		int[] nums = execute.delMore(DeviceInfo.class, ids);
		if(null == nums || nums.length == 0) {
			return R.error("删除设备信息失败！");
		}
		String[] idStrs = new String[ids.length];
		for(int i=0;i<ids.length;i++) {
			idStrs[i] = ids[i]+"";
		}
		//删除缓存数据
		Set<String> childrenIds = ClientCacheManager.delDeviceByIds(idStrs);
		if(!childrenIds.isEmpty()) {
			long[] idds = new long[childrenIds.size()];
			int index = 0;
			for(String idStr : childrenIds) {
				idds[index] = Long.parseLong(idStr);
				index++;
				
			}
			//批量删除客户端下关联的目标设备
			execute.delMore(DeviceInfo.class, idds);
		}
		return R.ok("删除设备信息成功！");
	}

	@RequestMapping("/updateDevice")
	@Transactional
	@Override
	@Option(opDesc="更新设备信息",module="设备管理")
	public R updateOne(@RequestBody DeviceInfo t) {
		t.setUpdateTime(new Date());
		int num = execute.updateOne(t);
		if(num <= 0) {
			return R.error("更新设备信息失败！");
		}
		//更新缓存
		ClientCacheManager.updateDevice(t);
		return R.ok("更新设备信息成功！");
	}
	
	/**
	 * 
	 * <p>Title: getDeviceStatusStatistics</p>  
	 * <p>Description: 获取设备在线离线统计数据</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @return
	 */
	@RequestMapping("/getDeviceStatusStatistics")
	public R getDeviceStatusStatistics() {
		return deviceServer.getDeviceStatusStatistics();
	}
	
	/**
	 * 
	 * <p>Title: getDeviceTasksStatistics</p>  
	 * <p>Description: 获取设备任务数统计数据</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @return
	 */
	@RequestMapping("/getDeviceTasksStatistics")
	public R getDeviceTasksStatistics(@RequestBody Query query) {
		return deviceServer.getDeviceTaskStatistics(query);
	}
	
	/**
	 * 
	 * <p>Title: saveOrUpdateTopoInfo</p>  
	 * <p>Description: 保存或更新设备topo信息</p>  
	 * @author yexj  
	 * @date 2019年7月4日  
	 * @param topo
	 * @return
	 */
	@Option(opDesc="保存设备topo图信息数据",module="设备管理")
	@RequestMapping("/saveOrUpdateTopoInfo")
	public R saveOrUpdateTopoInfo(@RequestBody DeviceTopoInfo topo) {
		return deviceServer.saveOrUpdateTopoInfo(topo);
	}
	
	/**
	 * 
	 * <p>Title: getDeviceTopoJsonData</p>  
	 * <p>Description: 获取设备topo图json数据</p>  
	 * @author yexj  
	 * @date 2019年7月3日  
	 * @return
	 */
	@RequestMapping("/getDeviceTopoJsonData")
	public R getDeviceTopoJsonData() {
		return deviceServer.getDeviceTopoJsonData();
	}
	
}
