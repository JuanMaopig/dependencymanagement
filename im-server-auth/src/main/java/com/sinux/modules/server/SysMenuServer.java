package com.sinux.modules.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;

/**
 * 
* <p>Title: SysMenuServer</p>  
* <p>Description: 系统菜单管理接口服务</p>  
* @author yexj  
* @date 2019年6月25日
 */
public interface SysMenuServer {

	/**
	 * 
	 * <p>Title: uploadMenuImage</p>  
	 * <p>Description: 上传系统菜单图片</p>  
	 * @author yexj  
	 * @date 2019年6月25日  
	 * @param file 上传文件
	 * @return
	 */
	public R uploadMenuImage(MultipartFile file, HttpServletRequest req);
	
	/**
	 * 
	 * <p>Title: getList</p>  
	 * <p>Description: 条件查询系统菜单信息</p>  
	 * @author yexj  
	 * @date 2019年6月26日  
	 * @param query 条件参数
	 * @return
	 */
	public R getList(Query query);
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 获取所有菜单数据</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @return
	 */
	public R getAllList();
	
	/**
	 * 
	 * <p>Title: delMore</p>  
	 * <p>Description: 删除系统菜单信息，删除父级菜单会将子级菜单一同删除</p>  
	 * @author yexj  
	 * @date 2019年6月26日  
	 * @param ids 系统菜单ID集合
	 * @return
	 */
	public R delMore(long[] ids);
	
	/**
	 * 
	 * <p>Title: getMenuImageFile</p>  
	 * <p>Description: 获取系统菜单图片数据</p>  
	 * @author yexj  
	 * @date 2019年6月26日  
	 * @param imageName
	 * @param req
	 * @param res
	 */
	public void getMenuImageFile(String imageName, HttpServletRequest req, HttpServletResponse res);
	
	/**
	 * 
	 * <p>Title: getLongUserMenu</p>  
	 * <p>Description: 获取登录人权限菜单</p>  
	 * @author yexj  
	 * @date 2019年7月23日  
	 * @param userId
	 * @return
	 */
	public R getLongUserMenu(long userId);
}
