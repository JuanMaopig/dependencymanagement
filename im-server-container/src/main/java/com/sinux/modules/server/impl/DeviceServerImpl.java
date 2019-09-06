package com.sinux.modules.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.entity.SysOptionLog;
import com.sinux.base.support.common.syslog.OptionLogService;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.base.support.common.util.IpUtil;
import com.sinux.modules.dao.DeviceDao;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.entity.DeviceTopoInfo;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.vo.DeviceInfoVo;
import com.sinux.modules.vo.TaskInfoVo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.UUID;

/**
 * 
* <p>Title: DeviceServerImpl</p>  
* <p>Description: 设备信息服务接口实现类</p>  
* @author yexj  
* @date 2019年6月14日
 */
@Service
public class DeviceServerImpl implements DeviceServer{

	@Autowired
	private ExecuteJdbcUtil execute;
	@Autowired
	private DeviceDao deviceDao;
	@Value("${device.image.upload}")
	private String deviceImageUrl;
	@Autowired
	private OptionLogService optionLogService;
	
	@Override
	public R getDevicesByType(Query query) {
		StringBuilder sql = new StringBuilder("SELECT * FROM device_info de where 1=1");
		List<DeviceInfo> des = new ArrayList<>();
		if(null != query && query.size() > 0 && !"0".equals(query.get("deviceType").toString())) {
			List<Object> params = new ArrayList<>();
			if(StringUtils.isNotBlank(query.get("deviceType").toString())) {
				sql.append(" and de.device_type = ?");
				params.add(query.get("deviceType"));
			}
			if(StringUtils.isNotBlank(query.get("deviceName").toString())) {
				sql.append(" and de.device_name like ?");
				params.add("%"+query.get("deviceName")+"%");
			}
			
			des = execute.getList(DeviceInfo.class, sql.toString(), params.toArray());
		
		}else {
			
			des = execute.getList(DeviceInfo.class, sql.toString());
		}
		if(null != des) {
			return R.ok("查询设备信息成功！").put("des", des);
		}
		return R.ok("查询设备信息失败！");
	}
	
	@Override
	public R getList(Query query) {
		List<DeviceInfoVo> divos = deviceDao.getList(query);
		if(null == divos) {
			return R.error("查询设备信息失败！");
		}
		int total = deviceDao.getTotal(query);
		return R.ok("查询设备信息成功！").put("rows", divos).put("total", total);
	}
	

	@Override
	public R getAllList() {
		Query query = new Query();
		List<DeviceInfo> dis = execute.getList(DeviceInfo.class, query);
		if(null == dis) {
			return R.error("获取所有设备信息失败！");
		}
		return R.ok("获取所有设备信息成功！").put("dis", dis);
	}
	

	@Override
	public R getListByDeviceName(Query query) {
		StringBuilder sql = new StringBuilder("SELECT di.* FROM device_info di WHERE 1=1");
		Object[] params = null;
		List<DeviceInfo> dis = null;
		if(null != query && null != query.get("deviceName") && StringUtils.isNotBlank(query.get("deviceName").toString())) {
			sql.append(" AND di.device_name LIKE ?");
			params = new Object[] {"%"+query.get("deviceName")+"%"};
			dis = execute.getList(DeviceInfo.class, sql.toString(), params);
		}else {
			dis = execute.getList(DeviceInfo.class, sql.toString());
		}
		if(null == dis) {
			return R.error("获取所有设备信息失败！");
		}
		return R.ok("获取所有设备信息成功！").put("dis", dis);
	}
	
	@Override
	public R getTop5ListByDeviceId(Query query) {
		StringBuilder sql = new StringBuilder("SELECT ti.*,(SELECT GROUP_CONCAT(su.username) FROM task_user_info tui LEFT JOIN sys_user su ON tui.user_id = su.id WHERE tui.task_id = ti.id) AS task_excute_uesr_names FROM task_info ti LEFT JOIN task_device_info tdi ON ti.id = tdi.task_id LEFT JOIN device_info di ON tdi.device_id = di.id WHERE di.id = ? ORDER BY ti.task_create_time DESC LIMIT 0,?");
		Object[] params = new Object[] {query.get("deviceId"),query.get("limit")};
		List<TaskInfoVo> tks = execute.getList(TaskInfoVo.class, sql.toString(), params);
		if(null == tks) {
			return R.error("查询任务top最新数据失败！");
		}
		return R.ok("查询任务top最新数据成功！").put("tks", tks);
	}

	@Override
	public List<DeviceInfo> getDevicesByTaskId(long taskId) {
		String sql = "SELECT di.* FROM task_device_info tdi LEFT JOIN device_info di ON tdi.device_id = di.id WHERE tdi.task_id = ?";
		List<DeviceInfo> dis = execute.getList(DeviceInfo.class, sql, new Object[] {taskId});
		return dis;
	}

	@Override
	public R uploadMenuImage(MultipartFile file, HttpServletRequest req) {
		//拼接目标地址
		String imageUrl = deviceImageUrl+File.separator+"device/";
		//创建目标文件夹
		FileUtil.mkdir(imageUrl);
		//获取目标文件名
		String fileName = file.getOriginalFilename();
		//获取文件后缀名称
		String suffixName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		//生成新的随机文件名
		fileName = UUID.fastUUID().toString()+suffixName;
		File destFile = FileUtil.newFile(imageUrl+fileName);
		try {
			FileUtil.writeFromStream(file.getInputStream(), destFile);
			return R.ok("上传系统菜单图片成功！").put("imageName", fileName);
		} catch (IORuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加入日志，由于该方法比较特殊，只能通过现有方法加入日志。
		SysOptionLog sol = new SysOptionLog();
		sol.setModule("设备管理");
		sol.setMethod("uploadMenuImage");
		sol.setActionUrl("/container/device/uploadMenuImage");
		sol.setContent("{\"file\":\""+fileName+"\"}");
		sol.setCreateDate(new Date());
		sol.setOpDesc("上传设备图片");
		sol.setResult(1);
		sol.setLevel(Constants.LOG_LEVEL_INFO);
		sol.setIp(IpUtil.getWebClientIp(req));
		optionLogService.addOne(sol);
		return R.error("上传系统菜单图片失败！");
	}

	@Override
	public void getMenuImageFile(String imageName, HttpServletRequest req, HttpServletResponse res) {
		File file = new File(deviceImageUrl+File.separator+"device/"+imageName);
		byte[] bt = FileUtil.readBytes(file);
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			os.write(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(null != os) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public R getDeviceStatusStatistics() {
		String sql = "SELECT di.device_status AS name,COUNT(di.id) AS value FROM device_info di GROUP BY di.device_status";
		List<Map<String, Object>> list = execute.executeQuerySqlForList(sql);
		if(null == list) {
			return R.error("查询设备状态统计信息失败！");
		}
		for(Map<String, Object> map : list) {
			Map<String,Object> itemStyleMap = new HashMap<>();
			//判断获取的数据是否为在线状态，如果是则添加相应的颜色
			if(null != map.get("name") && Integer.parseInt(map.get("name").toString()) == Constants.DEVICE_ONLINE_STATUS) {
				itemStyleMap.put("color", "#3477ff");
				map.put("name", "在线");
			}else {
				itemStyleMap.put("color", "#bdcdd6");
				map.put("name", "离线");
			}
			map.put("itemStyle", itemStyleMap);
		}
		return R.ok("查询设备状态统计信息成功！").put("status", list);
	}

	@Override
	public R getDeviceTaskStatistics(Query query) {
		StringBuilder sql = new StringBuilder("SELECT di.device_name AS name,COUNT(di.id) AS value FROM device_info di RIGHT JOIN task_device_info tdi ON di.id = tdi.device_id LEFT JOIN task_info ti ON tdi.task_id = ti.id WHERE 1=1");
		List<Object> params = new ArrayList<>();
		//判断开始时间
		if(null != query.get("beginTime") && StringUtils.isNotBlank(query.get("beginTime").toString())) {
			Date beginTime = new Date(Long.parseLong(query.get("beginTime").toString()));
			sql.append(" and ti.task_create_time >= ?");
			params.add(beginTime);
		}
				//判断结束时间
		if(null != query.get("endTime") && StringUtils.isNotBlank(query.get("endTime").toString())) {
			Date endTime = new Date(Long.parseLong(query.get("endTime").toString()));
			sql.append(" and ti.task_create_time <= ?");
			params.add(endTime);
		}
		sql.append(" GROUP BY di.id,di.device_name ORDER BY di.id");
		List<Map<String, Object>> list = execute.executeQuerySqlForList(sql.toString(),params.toArray());
		if(null == list) {
			return R.error("查询设备任务统计信息失败！");
		}
		List<String> xAxisList = new ArrayList<>();
		List<Integer> valueList = new ArrayList<>();
		list.parallelStream().forEach(map -> {
			String name = map.get("name") == null ? "" : map.get("name").toString();
			int value = map.get("value") == null ? 0 : Integer.parseInt(map.get("value").toString());
			xAxisList.add(name);
			valueList.add(value);
		});
		return R.ok("查询设备任务统计信息成功！").put("xAxisList", xAxisList).put("valueList", valueList);
	}

	@Override
	public R getDeviceTopoJsonData() {
		Query query = new Query();
		R r = getAllList();
		List<DeviceTopoInfo> dtis = execute.getList(DeviceTopoInfo.class, query);
		if(null == dtis || dtis.isEmpty()) {
			return r;
		}
		return r.put("topo", dtis.get(0));
	}

	@Override
	public R saveOrUpdateTopoInfo(DeviceTopoInfo topo) {
		DeviceTopoInfo dti = execute.getOne(DeviceTopoInfo.class, 1);
		if(null == dti) {
			int num = execute.addOne(topo);
			if(num == 0) {
				return R.error("保存设备topo信息失败！");
			}
		}else {
			topo.setId(1L);
			int num = execute.updateOne(topo);
			if(num == 0) {
				return R.error("更新设备topo信息失败！");
			}
		}
		return R.ok("保存设备topo信息成功！");
	}

	@Override
	public R getDeviceByHeartIp(String ip) {
		DeviceInfo device = deviceDao.getDeviceByIp(ip);
		if(null == device) {
			device = new DeviceInfo();
			device.setCreateTime(new Date());
			device.setDeviceIp(ip);
			device.setDeviceType(Constants.DEVICE_TYPE_CLIENT);
			device.setDeviceStatus(Constants.DEVICE_ONLINE_STATUS);
			device.setDeviceName("客户端"+System.currentTimeMillis());
			device.setDeviceShare(Constants.DEVICE_SHARE_FALSE);
			long id = execute.addOneReturnKeys(device);
			device.setId(id);
		}
		return R.ok("通过IP查询设备成功！").put("device", device);
	}

	@Override
	public R autoRegisterDevice(String deviceId, DeviceInfo di) {
		//注意设备ID是否与数据库设备ID一致
		DeviceInfo diOld = execute.getOne(DeviceInfo.class, Long.parseLong(deviceId));
		if(null == diOld) {
			di.setCreateTime(new Date());
			execute.addOne(di);
		}else {
			di.setUpdateTime(new Date());
			execute.updateOne(di);
		}
		return R.ok("自动注册设备信息成功！");
	}
}
