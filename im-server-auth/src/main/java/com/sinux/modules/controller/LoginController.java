package com.sinux.modules.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.base.support.common.entity.Message;
import com.sinux.base.support.common.entity.R;
import com.sinux.base.support.common.syslog.option.Option;
import com.sinux.base.support.common.util.VerifyUtil;
import com.sinux.modules.entity.SysMenu;
import com.sinux.modules.entity.SysRole;
import com.sinux.modules.entity.SysUser;
import com.sinux.modules.server.SysMenuServer;
import com.sinux.modules.server.SysRoleServer;
import com.sinux.modules.server.SysUserServer;

import cn.hutool.http.HttpStatus;

/**
 * 
* <p>Title: LoginController</p>  
* <p>Description: 登录控制类</p>  
* @author yexj  
* @date 2019年5月30日
 */
@RestController
@RequestMapping("/auth")
public class LoginController {
	
	private Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private SysUserServer sysUserServer;
	@Autowired
	private SysRoleServer sysRoleServer;
	@Autowired
	private SysMenuServer sysMenuServer;
	
	/**
	 * 
	 * <p>Title: login</p>  
	 * <p>Description: 登录操作</p>  
	 * @author yexj  
	 * @date 2019年6月4日  
	 * @param userName 用户名
	 * @param password  用户密码
	 * @param verificationCode 登录验证码
	 * @return 返回验证是否通过，和用户权限，角色信息
	 */
	@SuppressWarnings("unchecked")
	@Option(module="登录管理",opDesc="综合管理服务端通过用户名密码登录系统")
	@RequestMapping("/passwordLogin")
	public R passwordLogin(@RequestBody SysUser sup,
						   @RequestParam(name="verificationCode",required=false)String verificationCode) {
		R r = sysUserServer.getLoginUser(sup.getUsername(), sup.getPassword());
		if(Integer.parseInt(r.get("code").toString()) == HttpStatus.HTTP_INTERNAL_ERROR) {
			return r;
		}
		SysUser su = (SysUser) r.get("user");
		r = sysRoleServer.getLongUserRoles(su.getId());
		if(Integer.parseInt(r.get("code").toString()) == HttpStatus.HTTP_INTERNAL_ERROR) {
			return r;
		}
		List<SysRole> srs = (List<SysRole>) r.get("srs");
		r = sysMenuServer.getLongUserMenu(su.getId());
		if(Integer.parseInt(r.get("code").toString()) == HttpStatus.HTTP_INTERNAL_ERROR) {
			return r;
		}
		List<SysMenu> sms = (List<SysMenu>) r.get("sms");
		return R.ok("登录成功！").put("user", su).put("srs", srs).put("sms", sms);
	}
	
	/**
	 * 
	 * <p>Title: verifyUser</p>  
	 * <p>Description: 通过usbkey登录验证</p>  
	 * @author yexj  
	 * @date 2019年7月5日  
	 * @param voucher  发送凭证，需要通过制定加解密方式解密，解密成功后到数据库匹配人员证书
	 * @return
	 */
	@Option(module="登录管理",opDesc="综合管理服务端通过用户KEY登录系统")
	@RequestMapping(value = "/usbKeyLong")
    public Message usbKeyLong(String voucher) {
        // 将传入的数据进行拆分
        Boolean flag = false;
        String usbkeyNumber = null;
        String webIndex = null;
        Message message;
        if (voucher != null) {
            try {
                String[] receiveInfo = voucher.trim().split(",");
                String type = receiveInfo[0];
                String webIndex0 = receiveInfo[1];
                String certificateNumber = receiveInfo[2];
                String remoteAddr = receiveInfo[3];
                String signData = receiveInfo[4];
                // 加密后的数据
                String signStr = signData.trim().replace(" ", "+");
                // 原始数据
                String verStr = type.trim() + "," + webIndex0.trim() + "," + certificateNumber.trim() + "," + remoteAddr.trim();
                flag = VerifyUtil.verify(signStr, verStr);
                // 将验证的usbKey信息读取出来
                JSONObject jsonObject = JSON.parseObject(verStr);
                // 获取验证的usbKey
                usbkeyNumber = jsonObject.getString("usbkey_number");
                webIndex = jsonObject.getString("webIndex");
            } catch (Exception e) {
                flag = false;
                log.error(e.getMessage());
            }
        }
        if (flag) {
            message = new Message(1000, webIndex, usbkeyNumber);
        } else {
            message = new Message(1001, webIndex, "");
        }
        return message;
    }
	
	/**
	 * 
	 * <p>Title: clientPwLogin</p>  
	 * <p>Description: 客户端登录接口</p>  
	 * @author yexj  
	 * @date 2019年8月6日  
	 * @param sup
	 * @return
	 */
	@Option(module="登录管理",opDesc="综合管理客户端通过用户密码登录系统")
	@RequestMapping("/clientPwLogin")
	public R clientPwLogin(@RequestParam("username")String username,@RequestParam("password")String password) {
		return sysUserServer.getLoginUser(username, password);
	}
}
