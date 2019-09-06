package com.sinux.modules.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.bai.entity.Device;
import com.sinux.bai.entity.device.BaseDevice;
import com.sinux.bai.entity.device.interfaces.HttpInterface;
import com.sinux.bai.utils.ConvertDeviceUtil;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.entity.SysOptionLog;
import com.sinux.base.support.common.syslog.OptionLogService;
import com.sinux.base.support.common.util.HeartUtil;
import com.sinux.base.support.common.util.IpUtil;
import com.sinux.modules.server.DataBaseService;
import com.sinux.utils.Sets;

/**
 * 系统业务层
 * @ClassName SystemController
 * @Description: 系统业务层
 * @author Tangjc
 * @date 2019年6月6日 下午3:47:12
 */
@RestController
@RefreshScope
@RequestMapping("system")
public class SystemController {
	
	/**日志*/
	private Logger log = LoggerFactory.getLogger(SystemController.class);
	@Autowired
	private DataBaseService dataBaseService;
	@Autowired
	private OptionLogService optionLogService;
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 下载数据库文件
	 * @Title: downFile
	 * @Description: 下载数据库文件
	 * @author Tangjc
	 * @param tableNames 数据库名
	 * @param response 返回对象
	 * @return void
	 */
	@RequestMapping(value="download")
	@Transactional
//	@Option(module="数据备份管理",opDesc="备份指定表格数据，并下载备份数据")
	public void downFile(String[] tableNames,HttpServletResponse response,HttpServletRequest request){
		FileInputStream in = null;
		ServletOutputStream out = null;
		String path = dataBaseService.dataExport(tableNames);
		String fileName = StringUtils.substringAfterLast(path, "/");
		try {
			// 设置文件mime类型
			response.setContentType(request.getServletContext().getMimeType(fileName));
			response.setHeader("Content-Disposition", "attachment;filename=".concat(URLEncoder.encode(fileName,"UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File downFile = new File(path);
		try{
			in = new FileInputStream(downFile);
	        out = response.getOutputStream();
	        
			int len = 0;
	        byte b[] = new byte[16384];
			
			while((len=in.read(b))!= -1){
	        	out.write(b,0,len);
	            out.flush();
			}
		}catch(Exception e){
			
		}finally{
			try {
				if(in!=null){
					in.close();
				}
				
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//加入日志，由于该方法比较特殊，只能通过现有方法加入日志。
		SysOptionLog sol = new SysOptionLog();
		sol.setModule("数据备份管理");
		sol.setMethod("downFile");
		sol.setActionUrl("/system/download");
		sol.setContent(JSON.toJSONString(tableNames));
		sol.setCreateDate(new Date());
		sol.setOpDesc("备份指定表格数据，并下载备份数据");
		sol.setResult(1);
		sol.setLevel(Constants.LOG_LEVEL_INFO);
		sol.setIp(IpUtil.getWebClientIp(request));
		optionLogService.addOne(sol);
	}
	/**
	 * 获取table列表
	 * @Title: getTableList
	 * @Description: 获取table列表
	 * @author Tangjc
	 * @param pageNo 当前页数
	 * @param pageSize 每页显示数
	 * @return String
	 */
	@RequestMapping(value="/getTableList",method= {RequestMethod.POST})
	public R getTableList(@RequestBody Map<String,Object> params ){
		int pageNo = Integer.parseInt(String.valueOf(params.get("pageNo")));
		int pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
		List<Map<String,Object>> list = dataBaseService.getList();
		return R.ok("成功获取表列表").put("rows", list.subList((pageNo-1)*pageSize, pageNo*pageSize>list.size()?list.size():pageNo*pageSize)).put("total", list.size());
	}
	
	/**
	 * 
	 * <p>Title: deviceHeartReport</p>  
	 * <p>Description: 设备心跳上报</p>  
	 * @author yexj  
	 * @date 2019年8月21日  
	 * @param params
	 * @return
	 */
	@RequestMapping("/deviceHeartReport")
	public R deviceHeartReport(@RequestParam("params")String params) {
		JSONObject json = JSON.parseObject(params);
		if("DeviceHeart_report".equals(json.getString("cmd_type"))) {
			String deviceId = json.getString("device_id");
			String ip = json.getString("ip");
    		Integer port = json.getInteger("port");
	        JSONObject paramsJson = json.getJSONObject("params");
	        BaseDevice bd = Sets.getDm().getDeviceById(deviceId);
	        if(null == bd) {
	        	//判断是否为接入网设备
		        if(deviceId.contains(com.sinux.base.support.common.constants.Constants.DEVICE_CORE_NERWORK_ACCESS_ID)) {
		        	try {
		        		Device di = createDevice(ip, port, deviceId);
		        		//注册设备
		        		Sets.getDm().register(di);
		        		String diJson = HeartUtil.genJson("childDevice", Constants.SYN_MSG_TYPE_ADD, ConvertDeviceUtil.cvDeviceToJsonObject(di), deviceId);
		        		// 发送变化数据到消息队列
//						Sets.heartBox.getReceiveQueue().put();
					} catch (IOException e) {
						log.error("心跳上报创建设备对象失败！",e);
					} catch (Exception e) {
						log.error("心跳上报创建设备对象失败！",e);
					}
		        }
	        }else {
	        	//重置心跳超时倒计时
	        	Sets.getDm().getDeviceById(deviceId).resetSecond();
	        }
		}
		return R.ok("心跳上报成功");
	}
	
	/**
	 * 
	 * <p>Title: createDevice</p>  
	 * <p>Description: 创建设备，并初始化设备</p>  
	 * @author yexj  
	 * @date 2019年8月16日  
	 * @param ip
	 * @param port
	 * @param deviceId
	 * @throws IOException
	 */
	private Device createDevice(String ip, int port, String deviceId) throws IOException {
        if (StringUtils.isNotBlank(ip) && port != 0) {
        	Device device = new Device();
        	device.setDeviceId(deviceId);
        	device.setPort(port);
        	device.setIp(ip);
        	//设备状态
        	device.setDeviceStatus(com.sinux.base.support.common.constants.Constants.DEVICE_ONLINE_STATUS);
        	//设置设备接口
        	device.setEntityInterface(new HttpInterface(applicationContext, device));
        	//开启设备超时线程
        	device.startTimeOutListern();
            return device;
        }
        return null;
    }
}
