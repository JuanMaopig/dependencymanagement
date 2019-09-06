package com.sinux.modules.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.base.BaseController;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.modules.dao.SysMenuDao;
import com.sinux.modules.entity.SysMenu;
import com.sinux.modules.server.SysMenuServer;

/**
 * 
* <p>Title: SysMenuController</p>  
* <p>Description: 系统菜单控制类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@RestController
@RequestMapping("/auth/menu")
public class SysMenuController extends BaseController<SysMenu>{

	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysMenuServer sysMenuServer;
	
	@Option(module="菜单管理",opDesc="添加系统菜单")
	@Transactional
	@RequestMapping("/addMenu")
	@Override
	public R addOne(@RequestBody SysMenu t) {
		long num = sysMenuDao.addOne(t);
		if(num <= 0) {
			return R.ok("添加系统菜单失败！");
		}else {
			return R.ok("添加系统菜单成功！");
		}
	}
	
	/**
	 * 
	 * <p>Title: getMenuImageFile</p>  
	 * <p>Description: 用于获取系统菜单图片</p>  
	 * @author yexj  
	 * @date 2019年6月26日  
	 * @param imageName
	 */
	@RequestMapping("/getImage/{imageName}")
	public void getMenuImageFile(@PathVariable("imageName")String imageName,HttpServletRequest req,HttpServletResponse res) {
		sysMenuServer.getMenuImageFile(imageName, req, res);
	}

	/**
	 * 
	 * <p>Title: uploadMenuImage</p>  
	 * <p>Description: 上传系统菜单图片</p>  
	 * @author yexj  
	 * @date 2019年6月25日  
	 * @param type 上传图片属于那种菜单类型
	 * @param file 上传文件
	 * @return
	 */
	//@Option(module="菜单管理",opDesc="上传系统菜单图片")
	@RequestMapping("/uploadMenuImage")
	public R uploadMenuImage(@RequestParam("file")MultipartFile file,HttpServletRequest req) {
		return sysMenuServer.uploadMenuImage(file, req);
	}
	
	@RequestMapping("/getList")
	@Override
	public R getList(@RequestBody Query query) {
		return sysMenuServer.getList(query);
	}
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 递归查询所有关联关系的菜单信息</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @return
	 */
	@RequestMapping("/getAllList")
	public R getAllList() {
		return sysMenuServer.getAllList();
	}
	

	@RequestMapping("/getMenu")
	@Override
	public R getOne(long id) {
		SysMenu menu = sysMenuDao.getOne(id);
		if(null == menu) {
			return R.ok("查询菜单信息失败！");
		}else {
			return R.ok("查询菜单信息成功！").put("menu", menu);
		}
	}

	@Option(module="菜单管理",opDesc="删除系统菜单")
	@Transactional
	@RequestMapping("/delMenu")
	@Override
	public R delOne(long id) {
		int num = sysMenuDao.delOne(id);
		if(num <= 0) {
			return R.ok("删除系统菜单信息失败！");
		}else {
			return R.ok("删除系统菜单信息成功！");
		}
	}

	@Option(module="菜单管理",opDesc="批量删除系统菜单")
	@RequestMapping("/delMoreMenu")
	@Override
	public R delMore(@RequestBody long[] ids) {
		return sysMenuServer.delMore(ids);
	}

	@Option(module="菜单管理",opDesc="更新系统菜单")
	@Transactional
	@RequestMapping("/updateMenu")
	@Override
	public R updateOne(@RequestBody SysMenu t) {
		int num = sysMenuDao.updateOne(t);
		if(num <= 0) {
			return R.ok("更新系统菜单信息失败！");
		}else {
			return R.ok("更新系统菜单信息成功！");
		}
	}

}
