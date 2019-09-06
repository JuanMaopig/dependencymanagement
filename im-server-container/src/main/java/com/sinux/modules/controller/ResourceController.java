package com.sinux.modules.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.base.BaseController;
import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.base.support.common.util.HeartUtil;
import com.sinux.base.support.common.util.RedisUtil;
import com.sinux.modules.entity.ResourceApply;
import com.sinux.modules.entity.ResourceInfo;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.feign.ImAuthFeign;
import com.sinux.modules.server.ResourceApplyServer;
import com.sinux.modules.server.ResourceServer;
import com.sinux.utils.ClientCacheManager;
import com.sinux.utils.Sets;

import cn.hutool.http.HttpUtil;

/**
 * 
* <p>Title: ResourceController</p>  
* <p>Description: 资源控制类</p>  
* @author yexj  
* @date 2019年6月4日
 */
@RestController
@RequestMapping("/container/resource")
public class ResourceController extends BaseController<ResourceInfo>{
	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private ResourceApplyServer resourceApplyService;
	
	@Autowired
	private ResourceServer resourceService;
	
	@Autowired
	private ExecuteJdbcUtil execute;
	
	@Autowired
	private ImAuthFeign imAuthFeign;
	/**
	 * 申请资源
	 * @Title: askForResource
	 * @Description: 申请资源可用性检查
	 * @author Tangjc
	 * @param params 申请资源参数
	 * @return R
	 */
	@Option(module="资源管理",opDesc="资源申请")
	@RequestMapping(value="/resourceApply")
	public R askForResource(@RequestParam("params")String param){
		try{
			// 调用资源系统端接口
			JSONObject json = JSON.parseObject(param);
			Double resourceId = json.getDouble("resourceId");
			Set<Object> res = RedisUtil.zSetRangeByScore(Constants.RESOURCE_CACHE_KEY, resourceId, resourceId);
			ResourceInfo re = (ResourceInfo)res.iterator().next();
			int status = re.getResourceStatus().intValue();
			if(status == Constants.RESOURCE_USABLE_STATUS) {
				return R.ok("资源可用");
			}else {
				return R.error("资源不可用");
			}
		}catch(Exception e){
			logger.error("申请任务资源异常！");
			return R.error("申请资源可用性失败！");
		}
	}
	/**
	 * 任务资源申请异步回执
	 * @Title: resourceReport
	 * @Description: TODO
	 * @author Tangjc
	 * @param resourceReport
	 * @return
	 * @return R
	 */
	@RequestMapping("/resourceReport")
	@Option(module="资源管理",opDesc="任务资源申请")
	public R resourceReport(@RequestParam("resourceReport")String resourceReport){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceReport", resourceReport);
		HttpUtil.post("http://localhost:8081/resource/resourceReport", params);
		return R.ok();
	}
	
	/**
	 * 添加资源
	 */
	@RequestMapping("/addResource")
	@Override
	@Option(module="资源管理",opDesc="添加资源信息")
	public R addOne(@RequestBody ResourceInfo t) {
		t.setCreateTime(new Date());
		t.setResourceStatus(Constants.RESOURCE_USABLE_STATUS);
		long id = execute.addOneReturnKeys(t);
		if(id == 0) {
			return R.error("添加资源失败！");
		}
		t.setId(id);
		RedisUtil.zSetAdd(Constants.RESOURCE_CACHE_KEY, t, id);
		return R.ok("添加资源成功！");
	}
	
	/**
	 * 
	 * <p>Title: getResourceByType</p>  
	 * <p>Description: 通过资源类型获取资源信息</p>  
	 * @author yexj  
	 * @date 2019年7月8日  
	 * @param query
	 * @return
	 */
	@RequestMapping("/getResourcesByType")
	public R getResourceByType(@RequestBody Query query) {
		return resourceService.getResourcesByType(query);
	}
	
	@RequestMapping("/getList")
	@Override
	public R getList(@RequestBody Query query) {
		return resourceService.getlistByPageNo(query);
	}
	
	@RequestMapping("/getResource")
	@Override
	public R getOne(long id) {
		Set<Object> res = RedisUtil.zSetRangeByScore(Constants.RESOURCE_CACHE_KEY, id, id);
		ResourceInfo ri = null;
		if(null == res) {
			ri = execute.getOne(ResourceInfo.class, id);
		}else {
			ri = (ResourceInfo)res.iterator().next();
		}
		if(null == ri) {
			return R.error("获取资源信息失败！");
		}
		return R.ok("获取资源信息成功！").put("resource", ri);
	}
	
	@RequestMapping("/delResorce")
	@Override
	@Option(module="资源管理",opDesc="删除资源信息")
	public R delOne(long id) {
		int num = execute.delOne(ResourceInfo.class, id);
		if(num == 0) {
			return R.error("删除资源信息失败！");
		}
		//删除有序集合数据
		RedisUtil.zSetRemByScore(Constants.RESOURCE_CACHE_KEY, id, id);
		return R.ok("删除资源信息成功！");
	}
	
	@Transactional
	@RequestMapping("/delMoreResource")
	@Override
	@Option(module="资源管理",opDesc="批量删除资源信息")
	public R delMore(@RequestBody long[] ids) {
		int[] nums = execute.delMore(ResourceInfo.class, ids);
		if(null == nums || nums.length == 0) {
			return R.error("批量删除资源信息失败！");
		}
		//对数据IDs进行排序,升序排列
		Arrays.sort(ids);
		//删除有序集合数据
		RedisUtil.zSetRemByScore(Constants.RESOURCE_CACHE_KEY, ids[0], ids[ids.length-1]);
		return R.ok("批量删除资源信息成功！");
	}
	
	@RequestMapping("/updateResource")
	@Override
	@Option(module="资源管理",opDesc="更新资源信息")
	public R updateOne(@RequestBody ResourceInfo t) {
		t.setUpdateTime(new Date());
		int num = execute.updateOne(t);
		if(num == 0) {
			return R.error("更新资源信息失败！");
		}
		RedisUtil.zSetAdd(Constants.RESOURCE_CACHE_KEY, t, t.getId());
		return R.ok("更新资源信息成功！");
	}
	/**
	 * 异步接收资源端的回执
	 * @Title: ResourceApplyUnsyn
	 * @Description: 异步接收资源端的回执
	 * @author Tangjc
	 * @return void
	 */
	@Option(module="资源管理",opDesc="资源申请审核结果回执")
	@RequestMapping("/resourceApplyUnsyn")
	public void resourceApplyUnsyn(long resourceId,long taskId,Integer state){
		if(state==1){//通过审核
			ResourceApply ra = resourceApplyService.getOne(resourceId);
			ra.setState(state);
			// 修改流水
			resourceApplyService.updateOne(ra);
			
			// 修改任务 资源表
			//taskService.addTaskResource(taskId, resourceId);
			
			// 获取用户列表
			R r = imAuthFeign.getUserByTaskId(taskId);
			List<SysUser> list = (List<SysUser>) r.get("sus");
			// 同步资源
			ResourceInfo ri = resourceService.getOne(resourceId);
//			TaskInfo ti = taskService.getOne(resourceId);
			list.forEach(user->{
				// 获取同步ip
//				String ip = String.valueOf(Sets.synMap.get("user").get(String.valueOf(user.getId())).get("ip"));
				String ip = ClientCacheManager.getCliIpByLoginUserId(user.getId()+"");
				if(StringUtils.isNotEmpty(ip)){
					try {
						Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson("resource", "add", ri, String.valueOf(ri.getId())));
//						Sets.heartBox.getMsgQueueMap().get(ip).put(HeartUtil.genJson("task", "update", ti, String.valueOf(ti.getId())));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
