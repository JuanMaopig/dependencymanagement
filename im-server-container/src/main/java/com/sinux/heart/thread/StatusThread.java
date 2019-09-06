package com.sinux.heart.thread;

import java.util.Map;
import java.util.Set;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.heart.HeartBox;

/**
 * 修改设备状态
 * @ClassName StatusThread
 * @Description: 修改设备状态
 * @author Tangjc
 * @date 2019年6月11日 下午2:15:51
 */
public class StatusThread implements Runnable{
	/** 心跳信息 */
	private HeartBox heartBox;
	public StatusThread(HeartBox heartBox){
		this.heartBox = heartBox;
	}
	@Override
	public void run() {

		long currentTime = System.currentTimeMillis();
		Set<String> keys = RedisUtil.getKeys(Constants.CLIENT_CACHE_KEY_PRE+"*");
		for(String key : keys) {
			Map<Object, Object> cliMap = RedisUtil.hashMapGet(key);
			long registTime = Long.parseLong(cliMap.get("registTime").toString());
			if(currentTime - registTime > heartBox.getTimeout()) {
				RedisUtil.hashSet(key, "status", Constants.DEVICE_OFLINE_STATUS);
			}
		}
	}

}
