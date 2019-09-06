package com.sinux.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.OptionLogService;

/**
 * 
* <p>Title: SysOptionLogController</p>  
* <p>Description: 系统日志管理控制层</p>  
* @author yexj  
* @date 2019年8月5日
 */
@RestController
@RequestMapping("/auth/log")
public class SysOptionLogController {
	
	@Autowired
	private OptionLogService optionLogService;
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 分页查询操作日志信息</p>  
	 * @author yexj  
	 * @date 2019年8月5日  
	 * @param query
	 * @return
	 */
	@RequestMapping("/getList")
	public R getList(@RequestBody Query query) {
		return optionLogService.getList(query);
	}
}
