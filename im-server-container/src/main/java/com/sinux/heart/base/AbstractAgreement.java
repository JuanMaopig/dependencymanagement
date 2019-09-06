package com.sinux.heart.base;

import com.sinux.heart.HeartBox;

/**
 * 协议抽象类
 * @ClassName AbstractAgreement
 * @Description: 协议抽象类
 * @author Tangjc
 * @date 2019年6月10日 上午10:33:53
 */
public abstract class AbstractAgreement {
	/**
	 * 自动封装参数 还未实现
	 * @param obj
	 * @time 2019年6月10日-上午11:31:30
	 * @todo 自动封装参数 还未实现
	 * @author Tangjc
	 */
	public AbstractAgreement(String ip,int port,String pIp,int pPort,long timeout,HeartBox heartBoxOut){
		this.heartBox = heartBoxOut;
		heartBox.setIp(ip);
		heartBox.setpIp(pIp);
		heartBox.setPort(port);
		heartBox.setpPort(pPort);
		heartBox.setTimeout(timeout);
	}
	/** 心跳盒子 存储心跳相关信息 */
	protected HeartBox heartBox;
	/**
	 * 
	 * <p>Title: begin</p>  
	 * <p>Description: 开始接收UDP心跳消息</p>  
	 * @author yexj  
	 * @date 2019年8月1日
	 */
	public abstract void begin();
	public abstract void close();
}
