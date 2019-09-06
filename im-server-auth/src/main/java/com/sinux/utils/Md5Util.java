package com.sinux.utils;

import cn.hutool.crypto.digest.MD5;

/**
 * 
* <p>Title: Md5Util</p>  
* <p>Description: </p>  
* @author yexj  
* @date 2019年8月22日
 */
public class Md5Util {
	
	/**
	 * 
	 * <p>Title: Md5Hex</p>  
	 * <p>Description: md5加密</p>  
	 * @author yexj  
	 * @date 2019年8月22日  
	 * @param salt 加密盐
	 * @param msg 加密明文
	 * @param digestCount 加密次数
	 * @return 返回加密结果
	 */
	public static String md5Hex(String salt, String msg, int digestCount) throws Exception {
		MD5 md = new MD5();
		String hex = msg;
		for(int i=0;i<digestCount;i++) {
			hex = md.digestHex(hex+":"+salt);
		}
		return hex;
	} 
}
