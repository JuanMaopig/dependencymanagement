package com.sinux.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sinux.bai.entity.device.DeviceManager;
import com.sinux.base.support.common.base.BaseThreadFactory;
import com.sinux.heart.HeartBox;
/**
 * 缓存
 * @ClassName Sets
 * @Description: 缓存
 * @author Tangjc
 * @date 2019年6月12日 下午11:36:06
 */
public class Sets {
	private Sets(){}
	/** 心跳相关 */
	public static HeartBox heartBox = new HeartBox();
	/** 资源可用性缓存 */
	//public static Map<Long,Integer> resourceMap = new HashMap<Long,Integer>();
	/** 同步列表 */
	//public static Map<String,Map<String,Map<String,Object>>> synMap = new HashMap<String,Map<String,Map<String,Object>>>();
	/** 设备线程控制类key：设备ID，value：设备线程运行状态 */
	//public static Map<String,Boolean> runStatusMap = new HashMap<String,Boolean>();
	/** 设备控制类 */ 
	private static DeviceManager dm = new DeviceManager();
	/**执行线程池--设备各自监听自己的线程*/
	public static ThreadPoolExecutor tpe = new ThreadPoolExecutor(20,30,5000,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),new BaseThreadFactory("deviceDataReceive"),new ThreadPoolExecutor.AbortPolicy());
	
	public static DeviceManager getDm() {
		return dm;
	}
	public static void setDm(DeviceManager dm) {
		Sets.dm = dm;
	}
}
