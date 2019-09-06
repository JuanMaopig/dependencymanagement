package com.sinux.modules.entity;

import java.util.Date;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 系统操作日志
 * @ClassName SysOptionLog
 * @Description: 系统操作日志
 * @author Tangjc
 * @date 2019年7月22日 上午10:34:46
 */
public class SysOptionLog {
	
	
	/** 操作日志id */
	private long id;
	/** 操作人id */
	private long userId;
	/** 操作人名称 */
	private String userName;
	/** 操作模块 */
	private String module;
	/** 执行方法描述 */
	private String method;
	/** 传入参数 */
	private String content;
	/** 访问路径 */
	private String actionUrl;
	/** ip */
	private String ip;
	/** 操作时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
	/** 执行结果 */
	private byte result;
	public SysOptionLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SysOptionLog(long id, long userId, String userName, String module, String method, String content,
			String actionUrl, String ip, Date createDate, byte result) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.module = module;
		this.method = method;
		this.content = content;
		this.actionUrl = actionUrl;
		this.ip = ip;
		this.createDate = createDate;
		this.result = result;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "SysOptionLog [id=" + id + ", userId=" + userId + ", userName=" + userName + ", module=" + module
				+ ", method=" + method + ", content=" + content + ", actionUrl=" + actionUrl + ", ip=" + ip
				+ ", createDate=" + createDate + ", result=" + result + "]";
	}
}
