package com.sinux.modules.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.modules.entity.DeviceInfo;
import com.sinux.modules.entity.DeviceTopoInfo;

public interface DeviceServer{
	
	/**
	 * 
	 * <p>Title: getDevices</p>  
	 * <p>Description: 根据参数条件查询设备信息</p>  
	 * @author yexj  
	 * @date 2019年6月14日  
	 * @param query 参数条件
	 * @return
	 */
	public R getDevicesByType(Query query);
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 查询设备信息，分页查询</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param query 查询参数，分页需要带limit，pageNo两个参数
	 * @return
	 */
	public R getList(Query query);
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 获取所有设备信息</p>  
	 * @author yexj  
	 * @date 2019年7月3日  
	 * @return
	 */
	public R getAllList();
	
	/**
	 * 
	 * <p>Title: getListByDeviceName</p>  
	 * <p>Description: 通过设备名称查询设备信息</p>  
	 * @author yexj  
	 * @date 2019年7月21日  
	 * @param query
	 * @return
	 */
	public R getListByDeviceName(Query query);
	
	/**
	 * 
	 * <p>Title: getTop5ListByDeviceId</p>  
	 * <p>Description: 通过设备ID查找设备top任务信息</p>  
	 * @author yexj  
	 * @date 2019年7月21日  
	 * @param query
	 * @return
	 */
	public R getTop5ListByDeviceId(Query query);
	
	/**
	 * 
	 * <p>Title: getDeviceTopoJsonData</p>  
	 * <p>Description: 获取设备topo数据</p>  
	 * @author yexj  
	 * @date 2019年7月4日  
	 * @return
	 */
	public R getDeviceTopoJsonData();
	
	/**
	 * 
	 * <p>Title: getDevicesByTaskId</p>  
	 * <p>Description: 通过任务ID查询关联设备信息</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param taskId 任务ID
	 * @return 返回任务信息
	 */
	public List<DeviceInfo> getDevicesByTaskId(long taskId);
	
	/**
	 * 
	 * <p>Title: uploadMenuImage</p>  
	 * <p>Description: 上传设备图片</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param file
	 * @return
	 */
	public R uploadMenuImage(MultipartFile file, HttpServletRequest req);
	
	/**
	 * 
	 * <p>Title: getMenuImageFile</p>  
	 * <p>Description: 读取设备图片</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param imageName 图片名称
	 * @param req 请求实咧
	 * @param res 相应实咧
	 */
	public void getMenuImageFile(String imageName,HttpServletRequest req,HttpServletResponse res);
	
	/**
	 * 
	 * <p>Title: getDeviceStatusStatistics</p>  
	 * <p>Description: 获取设备状态统计数据</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @return
	 */
	public R getDeviceStatusStatistics();
	
	/**
	 * 
	 * <p>Title: getDeviceTaskStatistics</p>  
	 * <p>Description: 获取设备任务数统计数据</p>  
	 * @author yexj  
	 * @date 2019年7月2日  
	 * @param query
	 * @return
	 */
	public R getDeviceTaskStatistics(Query query);
	
	/**
	 * 
	 * <p>Title: saveOrUpdateTopoInfo</p>  
	 * <p>Description: 新增或修改设备topo信息</p>  
	 * @author yexj  
	 * @date 2019年7月4日  
	 * @param topo
	 * @return
	 */
	public R saveOrUpdateTopoInfo(DeviceTopoInfo topo);
	
	/**
	 * 
	 * <p>Title: getDeviceByHeartIp</p>  
	 * <p>Description: 通过心跳上报IP查找数据注册设备信息</p>  
	 * @author yexj  
	 * @date 2019年8月9日  
	 * @param ip
	 * @return
	 */
	public R getDeviceByHeartIp(String ip);
	
	/**
	 * 
	 * <p>Title: autoRegisterDevice</p>  
	 * <p>Description: 自动注册设备信息，在注册过程中如果已经注册过则更新设备信息，如果没有则添加设备信息</p>  
	 * @author yexj  
	 * @date 2019年8月13日  
	 * @param deviceId 设备ID，可能与数据库ID不一致，需注意
	 * @param di 设备信息
	 * @return
	 */
	public R autoRegisterDevice(String deviceId,DeviceInfo di);
}
