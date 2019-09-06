package com.sinux.thread;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.server.ResourceServer;

/**
 * 资源可用性检测线程
 * @ClassName ResourceCheckThread
 * @Description: 资源可用性检测线程
 * @author Tangjc
 * @date 2019年6月25日 下午2:18:46
 */
public class ResourceCheckThread implements Runnable{
	
	/**日志服务*/
	private Logger log = LoggerFactory.getLogger(ResourceCheckThread.class);
	private ResourceServer resourceServer;
	
	public ResourceCheckThread(ResourceServer resourceServer) {
		this.resourceServer = resourceServer;
	}
	@Override
	public void run() {
		try {
			//获取缓存所有资源列表
			Set<Object> resList = RedisUtil.zSetGet(Constants.RESOURCE_CACHE_KEY, 0, -1);
			// 循环资源
			resList.forEach(r->{
				ResourceInfo rr = (ResourceInfo)r;
				//调用资源可用性检查接口
			});
		}catch (Exception e) {
			log.error("资源可用性检测出错",e);
		}
	}
}
