package com.sinux.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sinux.base.support.common.constants.Constants;

/**
 * 
* <p>Title: SynTask</p>  
* <p>Description: 同步下发任务AOP注解</p>  
* @author yexj  
* @date 2019年6月24日
 */
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface SynTask {
	/**
	 * 
	 * <p>Title: type</p>  
	 * <p>Description: 定义任务同步类型，add,update.delete------默认add类型</p>  
	 * @author yexj  
	 * @date 2019年6月24日  
	 * @return
	 */
	public Constants.SynTaskEnum type() default Constants.SynTaskEnum.ADD;
}
