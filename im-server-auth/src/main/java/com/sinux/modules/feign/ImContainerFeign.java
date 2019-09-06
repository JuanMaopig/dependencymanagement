package com.sinux.modules.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinux.base.support.common.entity.R;
import com.sinux.modules.feign.fallback.ImContainerFeignFallBack;

/**
 * 
* <p>Title: ImContainerFeign</p>  
* <p>Description: 综合管理服务端container容器feign，用于远程调用容器中的接口</p>  
* @author yexj  
* @date 2019年8月23日
 */
@FeignClient(value="im-server-container",fallbackFactory=ImContainerFeignFallBack.class)
public interface ImContainerFeign {

	/**
	 * 
	 * <p>Title: getTaskByUserId</p>  
	 * <p>Description: 通过用户ID查询用户关联任务</p>  
	 * @author yexj  
	 * @date 2019年8月23日  
	 * @param userId
	 * @return
	 */
	@RequestMapping("/container/task/getTaskByUserId")
	public R getTaskByUserId(@RequestParam("userId")Long userId);
}
