package com.sinux.modules.server.impl;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.util.DefaultRowMapper;
import com.sinux.modules.dao.DeviceTypeDao;
import com.sinux.modules.entity.DeviceType;
import com.sinux.modules.server.DeviceTypeServer;


import cn.hutool.core.lang.Console;

/**
 * 
* <p>Title: DeviceTypeServerImpl</p>  
* <p>Description: 设备类型字典接口服务实现类</p>  
* @author yexj  
* @date 2019年6月17日
 */
@Service
public class DeviceTypeServerImpl implements DeviceTypeServer{

	@Autowired
	private JdbcTemplate jdbcTemplate;
//	@Autowired
//	private ExecuteJdbcUtil execute;
	@Autowired
	private DeviceTypeDao deviceTypeDao;
	
	@Override
	public R getAllList() {
		String sql = "SELECT * FROM device_type";
		List<DeviceType> dts = jdbcTemplate.query(sql, new DefaultRowMapper<DeviceType>(DeviceType.class));		
		if(null == dts) {
			return R.error("查询设备类型字典数据失败！");
		}else {
			return R.ok("查询设备类型字典数据成功！").put("dts", dts);
		}
		
	}
	
	@Override
	public R getList(@RequestBody Query query){	
		List<DeviceType> dts = deviceTypeDao.getList(query);		
		Console.log(dts);
		if(null == dts) {
			return R.error("查询设备类型信息失败！");
		}
		int total = deviceTypeDao.getTotal(query);
		return R.ok("查询设备类型信息成功！").put("rows", dts).put("total", total);
	}
	


}
