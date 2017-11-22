/**
 * Copyright 2016 Iflytek, Inc. All rights reserved.
 */
package com.gbzhu.edu.zx.photo.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbzhu.edu.elp.common.dto.json.StandardJSONResult;
import com.gbzhu.edu.elp.common.util.JSONUtils;

/**
 * <p>
 * <code>IndexController</code>主入口：包含index、404、50x等页面处理、日志记录；
 * </p>
 * 
 * @author gbzhu
 * @since 1.0
 * @version 1.0
 */
@Controller
public class IndexController {

	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * <p>
	 * <code>login</code>跳转到首页。
	 * </p>
	 * 
	 * @param request
	 *            request
	 * @param session
	 *            session
	 * @param model
	 *            model 请求
	 * @return ModelAndView
	 */
	@RequestMapping("/index")
	public String login(HttpServletRequest request, HttpSession session,
			ModelMap model) {
		return "/photo/index";
	}

	/**
	 * 空白页，没有任何权限时跳转
	 * 
	 * @return 空白页
	 */
	@RequestMapping("/blank")
	public String blank() {
		return "/photo/blank";
	}

	/**
	 * 登出
	 * 
	 * @param request
	 *            request
	 * @return boolean
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public String logout(HttpServletRequest request) {
		return JSONUtils.toJSONString(StandardJSONResult.getFailedInstance());
	}

	/**
	 * not found 跳转
	 * 
	 * @return 404界面
	 */
	@RequestMapping("/error/404")
	public String jump404Error() {
		return "/photo/blank";
	}

	/**
	 * <p>
	 * <code>jump500Error</code>500错误页面
	 * </p>
	 * 
	 * @return 500错误页面
	 */
	@RequestMapping("/error/500")
	public String jump505Error() {
		return "/photo/blank";
	}
}
