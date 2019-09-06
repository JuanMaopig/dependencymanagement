package com.sinux.bai.entity.device;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sinux.bai.common.message.Message;
import com.sinux.bai.entity.device.BaseConfig;
import com.sinux.bai.common.message.Message;

/**
 * @author hewei
 * @version 1.0
 * @created 08-7月-2019 14:21:43
 */
public abstract class EntityInterface implements Serializable {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 接口配置
     */
    private BaseConfig config;
    /**
     * 接口描述
     */
    private String desc;
    /**
     * 接口ID
     */
    private int id;
    /**
     * 接口状态:0-表示初始化成功，正在运行，1-表示故障，2-表示正在重启。
     */
    private int status;
    /**
     * 接口类型：0-lte
     */
    private int type;

    /**
     * 线程状态：true-表示正在运行，false-表示停止
     */
    public AtomicBoolean threadStatus = new AtomicBoolean(true);
    
    public BaseConfig getConfig() {
		return config;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getThreadStatus() {
		return threadStatus.get();
	}

	public void setThreadStatus(boolean threadStatus) {
		this.threadStatus.set(threadStatus);
	}

	/**
     * 接口初始化
     *
     * @return 初始化结果
     */
    public int initialize() {
        return 0;
    }
    
    /**
     * 
     * <p>Title: startReceiveDataThread</p>  
     * <p>Description: 开启接收线程监听事件</p>  
     * @author yexj  
     * @date 2019年8月19日
     */
    public void startReceiveDataThread() {}

    /**
     * Description: 发送消息到设备
     * @param json
     * @return com.mds.site.common.message.Message
     * @auther Sopp
     * @date: 2019/7/17 18:43
     * @throws
     */
    public abstract Message sendMessage(String json);

    /**
     * Description: 从设备接收消息，并转发到mq中，交由其他程序处理。
     * @param
     * @return boolean
     * @auther Sopp
     * @date: 2019/7/17 18:44
     * @throws
     */
    public abstract boolean receiveMessage();
    /**
     * 
     * <p>Title: shutDownThreadAndSocket</p>  
     * <p>Description: 关闭设备监听线程和监听端口</p>  
     * @author yexj  
     * @date 2019年8月14日  
     * @return
     */
    public abstract boolean shutDownThreadAndSocket();

}
