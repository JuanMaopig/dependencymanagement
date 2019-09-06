package com.sinux.heart;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinux.base.support.common.base.BaseThreadFactory;
import com.sinux.heart.base.AbstractAgreement;
import com.sinux.heart.thread.Receive;
import com.sinux.heart.thread.Send;
import com.sinux.heart.thread.StatusThread;
/**
 * 
* <p>Title: Udp</p>  
* <p>Description: udp协议构造类，用于接收心跳信息，并发送数据到客户端</p>  
* @author yexj  
* @date 2019年7月11日
 */
public class Udp extends AbstractAgreement{
	/**日志*/
	private Logger log = LoggerFactory.getLogger(Udp.class);
	/**声明一个UDP服务端socket监听*/
	private DatagramSocket socket;
	/**声明一个定时任务*/
	private ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(2,new BaseThreadFactory("deviceStatusCheck"),new ThreadPoolExecutor.DiscardPolicy());
	/**执行线程池*/
	private ThreadPoolExecutor tpe = new ThreadPoolExecutor(2,5,5000,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),new BaseThreadFactory("udpReceive"),new ThreadPoolExecutor.AbortPolicy());
	/**
	 * 初始化构造函数
	 * @param ip 本机ip
	 * @param port 本机端口
	 * @param pIp 父级ip
	 * @param pPort 父级端口
	 * @param upper 上报超时时间
	 * @param lower 下发超时时间
	 * @time 2019年6月10日-上午11:54:59
	 * @author Tangjc
	 */
	public Udp(String ip,int port,String pIp,int pPort,long timeout,HeartBox heartBoxOut){
		// 调用父类初始化参数
		super(ip,port,pIp,pPort,timeout,heartBoxOut);
		log.info("初始化UDP协议监听参数成功");
		// 初始化参数
		try {
			SocketAddress addr = new InetSocketAddress(ip, port);
			socket = new DatagramSocket(addr);
			// 将连接存入heartBox
			heartBox.setSocket(socket);
			// 启动线程监听
			begin();
		} catch (SocketException e) {
			log.error("启动服务端心跳监听失败！",e);
		}
	}
	
	@Override
	public void begin() {
		
		//如果父级ip有值 需要上报心跳
		if(StringUtils.isNotEmpty(heartBox.getpIp())){
			try {
				// 任务执行完后 等待xms再次执行
				ses.scheduleWithFixedDelay(new Send(socket, heartBox), heartBox.getTimeout(), heartBox.getTimeout(), TimeUnit.MILLISECONDS);
				log.info("启动消息上报任务成功");
			}catch (Exception e) {
				log.error("启动消息上报任务失败",e);
			}
		}
		try {
			//执行修改设备状态的任务线程
			ses.scheduleWithFixedDelay(new StatusThread(heartBox), 15, heartBox.getTimeout(), TimeUnit.MILLISECONDS);
			log.info("启动任务状态监听任务成功");
		}catch (Exception e) {
			log.error("启动任务状态监听任务失败",e);
		}
		try {
			// 启动接收
			tpe.execute(new Receive(socket, heartBox));
			log.info("启动心跳接收监听成功");
		}catch (Exception e) {
			log.info("启动心跳接收监听失败",e);
		}
	}

	@Override
	public void close() {
		
	}

}
