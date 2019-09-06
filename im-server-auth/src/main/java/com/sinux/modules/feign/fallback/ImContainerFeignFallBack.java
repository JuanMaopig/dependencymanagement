package com.sinux.modules.feign.fallback;

import org.springframework.stereotype.Component;

import com.sinux.base.support.common.entity.R;
import com.sinux.modules.feign.ImContainerFeign;

import cn.hutool.http.HttpStatus;
import feign.hystrix.FallbackFactory;
/**
 * 
* <p>Title: ImAuthFeignFallBack</p>  
* <p>Description: 服务im-server-auth远程feign调用熔断器，防止雪崩式调用发生</p>  
* @author yexj  
* @date 2019年6月24日
 */
@Component
public class ImContainerFeignFallBack implements FallbackFactory<ImContainerFeign> {

	@Override
	public ImContainerFeign create(Throwable cause) {
		return new ImContainerFeign(){

			@Override
			public R getTaskByUserId(Long userId) {
				return R.error(HttpStatus.HTTP_NOT_FOUND,"调用综合管理容器失败，调用超时或网络异常！");
			}

			
			
        };
	}

}
