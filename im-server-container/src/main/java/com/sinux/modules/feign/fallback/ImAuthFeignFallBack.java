package com.sinux.modules.feign.fallback;

import org.springframework.stereotype.Component;

import com.sinux.base.support.common.entity.R;
import com.sinux.modules.feign.ImAuthFeign;

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
public class ImAuthFeignFallBack implements FallbackFactory<ImAuthFeign> {

	@Override
	public ImAuthFeign create(Throwable cause) {
		return new ImAuthFeign(){

			@Override
			public R getUserByTaskId(long taskId) {
				return R.error(HttpStatus.HTTP_NOT_FOUND,"调用综合管理授权中心失败，调用超时或网络异常！");
			}
			
        };
	}

}
