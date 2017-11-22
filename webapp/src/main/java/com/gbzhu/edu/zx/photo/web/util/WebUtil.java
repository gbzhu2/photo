/**
 * Copyright 2016 Iflytek, Inc. All rights reserved.
 */
package com.gbzhu.edu.zx.photo.web.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gbzhu.edu.zx.photo.web.dto.OpmUserDto;


/**
 * <p>
 * <code>WebUtil</code>
 * </p>
 * 
 * @author gbzhu2
 * @since 1.0
 * @version 1.0
 */
public class WebUtil {

	/**
	 * sessionkey
	 */
	public static final String SESSION_KEY = "SESSION_KEY_CURRENT_USER";

	/**
	 * verifycode key
	 */
	private static final String SESSION_KEY_VCODE = "SESSION_KEY_VERIFY_CODE";

	/**
	 * <p>
	 * <code>getIP</code>从HttpServletRequest取得客户端真实IP。
	 * </p>
	 * 
	 * @author amkong
	 * @param request
	 *            请求
	 * @return ip地址
	 */
	public static String getIP(HttpServletRequest request) {
		String ipAddress = null;
		String unknown = "unknown";
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| unknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| unknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| unknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * <p>
	 * <code>setCurrentUserSession</code>将当前用户信息保存到Session
	 * </p>
	 * 
	 * @param userDto
	 *            用户信息
	 * @param request
	 *            request
	 */
	public static void setCurrentUserSession(OpmUserDto userDto,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_KEY, userDto);
	}

	/**
	 * <p>
	 * <code>getCurrentUser</code>从Session中获取用户信息
	 * </p>
	 * 
	 * @param request
	 *            request
	 * @return 用户信息
	 */
	public static OpmUserDto getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		OpmUserDto userDto = (OpmUserDto) session.getAttribute(SESSION_KEY);
		return userDto;
	}

	/**
	 * <p>
	 * <code>setVerifyCodeSession</code>保存验证码到session
	 * </p>
	 * 
	 * @param code
	 *            验证码字符
	 * @param request
	 *            request
	 */
	public static void setVerifyCodeSession(String code,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_KEY_VCODE, code);
	}

	/**
	 * <p>
	 * <code>getVerifyCode</code>从session中获取验证码
	 * </p>
	 * 
	 * @param request
	 *            request
	 * @return 验证码字符
	 */
	public static String getVerifyCode(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object object = session.getAttribute(SESSION_KEY_VCODE);
		return object == null ? null : object.toString();
	}
}
