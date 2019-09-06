package com.sinux.modules.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinux.base.support.common.entity.R;
import com.sinux.modules.feign.fallback.ImAuthFeignFallBack;

/**
 * 
* <p>Title: ImAuthFegin</p>  
* <p>Description: 调用im-server-auth服务接口，主要使用springcloud的fegin方式调用，集成ribbn负载均衡和断路器策略，防止发生雪崩式调用</p>  
* @author yexj  
* @date 2019年6月24日
 */
@FeignClient(value="im-server-auth",fallbackFactory=ImAuthFeignFallBack.class)
public interface ImAuthFeign {

	/**
	 * 
	 * <p>Title: getUserByTaskId</p>  
	 * <p>Description: 通过任务ID查询任务人员信息</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/auth/user/getUserByTaskId")
	public R getUserByTaskId(@RequestParam(value="taskId")long taskId);
}
