package com.sinux.runner;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sinux.base.support.common.base.BaseThreadFactory;
import com.sinux.heart.Udp;
import com.sinux.modules.server.DeviceServer;
import com.sinux.modules.server.ResourceServer;
import com.sinux.modules.server.TaskServer;
import com.sinux.thread.MessageSynThread;
import com.sinux.thread.RedisSynThread;
import com.sinux.thread.ResourceCheckThread;
import com.sinux.utils.Sets;
/**
 * 初始化心跳线程
 * @ClassName HeartRunner
 * @Description: 初始化心跳线程
 * @author Tangjc
 * @date 2019年6月20日 下午1:55:31
 */
@Component
public class HeartRunner implements ApplicationRunner	{
	/**日志*/
	private Logger log = LoggerFactory.getLogger(HeartRunner.class);
	/** 本机ip */
	@Value("${heart.ip}")
	private String ip;
	/** 本机端口 */
	@Value("${heart.port}")
	private int port;
	/** 心跳超时时间 */
	@Value("${heart.timeout}")
	private long timeout;
	@Autowired
	private TaskServer taskServer;
	@Autowired
	private ResourceServer resourceServer;
	@Autowired
	private DeviceServer deviceServer;
	/**定时调度任务池*/
	private ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(5,new BaseThreadFactory("resourceCheck"),new ThreadPoolExecutor.AbortPolicy());
	/**执行线程池*/
	private ThreadPoolExecutor tpe = new ThreadPoolExecutor(2,5,5000,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),new BaseThreadFactory("synMsg"),new ThreadPoolExecutor.AbortPolicy());
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 初始化心跳
    	new Udp(ip, port, "", 0, timeout, Sets.heartBox);
    	try {
	    	// 启动消息同步线程
	    	tpe.execute(new MessageSynThread(Sets.heartBox, taskServer, resourceServer, deviceServer));
	    	log.info("启动消息同步任务成功");
    	}catch (Exception e) {
    		log.error("启动消息同步任务失败",e);
		}
    	try {
	    	// 启动资源可用检测线程
	    	ses.scheduleWithFixedDelay(new ResourceCheckThread(resourceServer), 10, 10, TimeUnit.SECONDS);
	    	log.info("启动资源可用性检查任务成功");
    	}catch (Exception e) {
    		log.error("启动资源可用性检查任务失败",e);
		}
    	try {
	    	// 启动redis缓存同步到数据库显现，执行任务，第一次延迟时间，每隔多少时间执行一次，时间单位
	    	ses.scheduleWithFixedDelay(new RedisSynThread(deviceServer), 15, 10, TimeUnit.SECONDS);
	    	log.info("启动缓存同步到数据库任务成功");
    	}catch (Exception e) {
    		log.error("启动缓存同步到数据库任务失败",e);
		}
    }
}
