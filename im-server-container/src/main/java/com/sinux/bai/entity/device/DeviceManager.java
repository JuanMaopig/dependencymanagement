package com.sinux.bai.entity.device;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 设备管理
 *
 * @author hewei
 * @version 1.0
 * @created 08-7月-2019 14:21:43
 */
public class DeviceManager {
	
	/**目标设备缓存列表*/
    private Map<String,BaseDevice> devices = new ConcurrentHashMap<>();

    public DeviceManager() {

    }

    /**
     * 根据ID获取设备
     *
     * @param ids 设备的ID
     * @return 设备集合
     */
    public List<BaseDevice> getDeviceByIds(String[] ids) {
    	List<BaseDevice> deviceList = new LinkedList<BaseDevice>();
    	for(String str:ids){
			BaseDevice bd = this.devices.get(str);
			if(null != bd) {
				deviceList.add(bd);
			}
		}
        return deviceList;
    }
    
    /**
     * 
     * <p>Title: getDeviceById</p>  
     * <p>Description: 通过ID获取设备</p>  
     * @author yexj  
     * @date 2019年8月14日  
     * @param id
     * @return
     */
    public BaseDevice getDeviceById(String id) {
    	return devices.get(id);
    }
    
    /**
     * 
     * <p>Title: getDevice</p>  
     * <p>Description: </p>  
     * @author yexj  
     * @date 2019年8月14日  
     * @return
     */
    public List<BaseDevice> getDevice(){
    	List<BaseDevice> deviceList = new LinkedList<BaseDevice>();
    	for(BaseDevice db : devices.values()) {
    		deviceList.add(db);
    	}
    	return deviceList;
    }

    /**
     * 根据类型获取设备
     *
     * @param type 设备类型
     * @return 设备集合
     */
    public List<BaseDevice> getDeviceByType(int type) {
        List<BaseDevice> list = new ArrayList<>();
        for(BaseDevice db : devices.values()) {
        	if(db.getType() == type) {
        		list.add(db);
        	}
        }
        return list;
    }
    /**
     * 修改设备状态
     * @Title: UpdateDeviceStatus
     * @Description: 修改设备状态
     * @author Tangjc
     * @param deviceId 设备id
     * @param status 设备状态
     * @return
     * @return boolean
     */
    public boolean updateDeviceStatus(String deviceId,int status){
    	try{
    		devices.get(deviceId).setDeviceStatus(status);
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    	
    }
    /**
     * 根据设备id删除设备
     * @Title: delById
     * @Description: 根据设备id删除设备
     * @author Tangjc
     * @param deviceId 设备id
     * @return
     * @return boolean
     */
    public boolean delById(String deviceId){
    	try{
    		BaseDevice bd = devices.remove(deviceId);
    		bd.getEntityInterface().setThreadStatus(false);
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    	
    }
    
    /**
     * 
     * <p>Title: isHasDevice</p>  
     * <p>Description: 判断缓存中是否有该设备</p>  
     * @author yexj  
     * @date 2019年8月14日  
     * @param deviceId
     * @return
     */
    public boolean isHasDevice(String deviceId) {
    	return devices.containsKey(deviceId);
    }
    
    /**
     * 注册设备
     *
     * @param device 设备类
     * @return 注册结果
     */
    public Boolean register(BaseDevice device) {
    	try {
    		devices.put(device.getDeviceId(), device);
    		return true;
    	}catch (Exception e) {
			return false;
		}
    }

    /**
     * @param device 设备类
     * @return 注销设备结果
     */
    public Boolean unregister(BaseDevice device) {
    	try {
    		BaseDevice bd = devices.remove(device.getDeviceId());
    		bd.getEntityInterface().setThreadStatus(false);
	    	return true;
    	}catch (Exception e) {
			return false;
		}
    }
    
}
