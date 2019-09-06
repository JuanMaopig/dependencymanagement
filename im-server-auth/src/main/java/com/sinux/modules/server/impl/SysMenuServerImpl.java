package com.sinux.modules.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.entity.SysOptionLog;
import com.sinux.base.support.common.syslog.OptionLogService;
import com.sinux.base.support.common.util.ExecuteJdbcUtil;
import com.sinux.base.support.common.util.IpUtil;
import com.sinux.modules.entity.SysMenu;
import com.sinux.modules.server.SysMenuServer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.UUID;

/**
 * 
* <p>Title: SysMenuServerImpl</p>  
* <p>Description: 系统菜单接收实现类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@Service
public class SysMenuServerImpl implements SysMenuServer {
	
	@Value("${system.meun.upload}")
	private String menuImageUrl;
	@Autowired
	private ExecuteJdbcUtil execute;
	@Autowired
	private OptionLogService optionLogService;

	@Transactional
	@Override
	public R uploadMenuImage(MultipartFile file, HttpServletRequest req) {
		//拼接目标地址
		String imageUrl = menuImageUrl+File.separator+"menu/";
		//创建目标文件夹
		FileUtil.mkdir(imageUrl);
		//获取目标文件名
		String fileName = file.getOriginalFilename();
		//获取文件后缀名称
		String suffixName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		//生成新的随机文件名
		fileName = UUID.fastUUID().toString()+suffixName;
		File destFile = FileUtil.newFile(imageUrl+fileName);
		try {
			FileUtil.writeFromStream(file.getInputStream(), destFile);
			return R.ok("上传系统菜单图片成功！").put("imageName", fileName);
		} catch (IORuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加入日志，由于该方法比较特殊，只能通过现有方法加入日志。
		SysOptionLog sol = new SysOptionLog();
		sol.setModule("菜单管理");
		sol.setMethod("downFile");
		sol.setActionUrl("/auth/menu/uploadMenuImage");
		sol.setContent("{\"file\":\""+fileName+"\"}");
		sol.setCreateDate(new Date());
		sol.setOpDesc("上传系统菜单图片");
		sol.setResult(1);
		sol.setLevel(Constants.LOG_LEVEL_INFO);
		sol.setIp(IpUtil.getWebClientIp(req));
		optionLogService.addOne(sol);
		return R.error("上传系统菜单图片失败！");
	}

	@Override
	public R getList(Query query) {
		List<SysMenu> menus = execute.getList(SysMenu.class, query);
		if(null == menus) {
			R.error("条件查询系统菜单信息失败！");
		}
		for(SysMenu menu : menus) {
			query.put("parent_id", menu.getId());
			List<SysMenu> childrenMenus = execute.getList(SysMenu.class, query);
			if(null != childrenMenus && childrenMenus.size() > 0) {
				menu.setHasChildren(true);
			}else {
				menu.setHasChildren(false);
			}
		}
		return R.ok("条件查询系统菜单信息成功！").put("rows", menus);
	}
	
	@Override
	public R getAllList() {
		Query query = new Query();
		query.put("parent_id", 0);
		List<SysMenu> menus = execute.getList(SysMenu.class, query);
		if(null == menus) {
			R.error("查询所有系统菜单信息失败！");
		}
		getAllChildrenMenu(menus);
		return R.ok("查询所有系统菜单信息成功！").put("rows", menus);
	}
	
	/**
	 * 
	 * <p>Title: getAllChildrenMenu</p>  
	 * <p>Description: 递归查询所有菜单信息，子级父级关系关联查询</p>  
	 * @author yexj  
	 * @date 2019年7月1日  
	 * @param menus 第一级菜单信息集合
	 */
	private void getAllChildrenMenu(List<SysMenu> menus) {
		for(SysMenu menu : menus) {
			Query query = new Query();
			query.put("parent_id", menu.getId());
			List<SysMenu> childrenMenus = execute.getList(SysMenu.class, query);
			if(null != childrenMenus && childrenMenus.size() > 0) {
				menu.setHasChildren(true);
				menu.setChildren(childrenMenus);
				getAllChildrenMenu(childrenMenus);
			}else {
				menu.setHasChildren(false);
			}
		}
	}

	@Transactional
	@Override
	public R delMore(long[] ids) {
		List<Long> idss = new ArrayList<>();
		List<Object[]> params = new ArrayList<>();
		for(long id : ids) {
			if(id == 1) {
				return R.error("删除系统菜单信息失败，选择的系统菜单中包含根目录！");
			}
			params.add(new Object[] {id});
			idss.add(id);
		}
		int[] nums = execute.delMore(SysMenu.class, ids);
		if(null == nums) {
			return R.error("删除系统菜单信息失败！");
		}
//		String sql = "DELETE FROM sys_menu WHERE parent_id = ?";
//		nums = execute.executeBatchUpdateSql(sql, params);
//		if(null == nums) {
//			return R.error("删除系统菜单子级菜单信息失败！");
//		}
		deleteMenu(idss);
		return R.ok("删除系统菜单信息成功！");
	}
	
	/**
	 * 
	 * <p>Title: deleteMenu</p>  
	 * <p>Description: 递归删除菜单数据，选中了父级菜单，会把子级菜单全部删除</p>  
	 * @author yexj  
	 * @date 2019年7月23日  
	 * @param ids
	 */
	private void deleteMenu(List<Long> ids) {
		for(Long id : ids) {
			String sql = "SELECT id FROM sys_menu WHERE parent_id = ?";
			List<Map<String, Object>> childMaps = execute.executeQuerySqlForList(sql, new Object[] {id});
			long[] childIdsArray = new long[childMaps.size()];
			List<Long> childIds = new ArrayList<>();
			for(int i=0; i<childMaps.size();i++) {
				Long childrenId = Long.parseLong(childMaps.get(i).get("id").toString());
				childIdsArray[i] = childrenId;
				childIds.add(childrenId);
			}
			execute.delMore(SysMenu.class, childIdsArray);
			deleteMenu(childIds);
		}
	}

	@Override
	public void getMenuImageFile(String imageName, HttpServletRequest req, HttpServletResponse res) {
		File file = new File(menuImageUrl+File.separator+"menu/"+imageName);
		byte[] bt = FileUtil.readBytes(file);
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			os.write(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(null != os) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public R getLongUserMenu(long userId) {
		String sql = "SELECT sm.* FROM sys_menu sm LEFT JOIN sys_role_menu srm ON sm.id=srm.menu_id LEFT JOIN sys_role sr ON srm.role_id=sr.id LEFT JOIN sys_user_role sur ON sr.id=sur.role_id LEFT JOIN sys_user su ON sur.user_id=su.id WHERE su.id=?";
		List<SysMenu> sms = execute.getList(SysMenu.class, sql, new Object[] {userId});
		if(null == sms) {
			return R.error("查询登录用户权限菜单失败！");
		}
		return R.ok("查询登录用户权限菜单成功！").put("sms", sms);
	}
	
	
}