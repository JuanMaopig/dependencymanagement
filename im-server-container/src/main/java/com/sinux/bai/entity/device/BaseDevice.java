package com.sinux.bai.entity.device;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicInteger;

import com.sinux.heart.HeartBox;



/**
 * 设备类
 *
 * @author hewei
 * @version 1.0
 * @created 08-7月-2019 14:21:43
 */
public abstract class BaseDevice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 设备超时时间 */
    private int timeout = Integer.parseInt(HeartBox.heartMap.get("device.timeout"));
	/** 设备超时时间(原子) */
    private AtomicInteger timeoutSecond =new AtomicInteger(timeout);
    /**
     * 设备配置
     */
    private BaseConfig config;

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 设备port
     */
    private int port;

    /**
     * 接口
     */
    private EntityInterface entityInterface = null;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备网络状态，0-正常  1-异常
     */
    private int deviceStatus;

    /**
     * 设备类型
     */
    private int type;
    
    /**
     * 是否自动查号
     */
    private boolean isAutoSelect;

    /**
     * 设备检查，初始化
     * @return
     */
    public abstract int initialize();

    /**
     * 设备关闭
     *
     * @return
     */
    public abstract int powerOff();

    /**
     * 设备开启
     *
     * @return
     */
    public abstract int powerOn();
    
    /**
     * 
     * <p>Title: resetSecond</p>  
     * <p>Description: 重置超时时间</p>  
     * @author yexj  
     * @date 2019年8月14日
     */
    public abstract void resetSecond();
    
    /**
     * 
     * <p>Title: restartListener</p>  
     * <p>Description: 重启设备监听</p>  
     * @author yexj  
     * @date 2019年8月20日
     * @param socekt 重新连接的socket
     */
    public abstract void restartListener(DatagramSocket socket);

	public BaseConfig getConfig() {
		return config;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public EntityInterface getEntityInterface() {
		return entityInterface;
	}

	public void setEntityInterface(EntityInterface entityInterface) {
		this.entityInterface = entityInterface;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(int deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public AtomicInteger getTimeoutSecond() {
		return timeoutSecond;
	}

	public void setTimeoutSecond(int timeout) {
		this.timeoutSecond.set(timeout);
	}

	public boolean isAutoSelect() {
		return isAutoSelect;
	}

	public void setAutoSelect(boolean isAutoSelect) {
		this.isAutoSelect = isAutoSelect;
	}
	
}
