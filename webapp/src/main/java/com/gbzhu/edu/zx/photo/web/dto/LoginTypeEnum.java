package com.gbzhu.edu.zx.photo.web.dto;

/**
 * 登陆方式枚举
 * @author zhuguobiao
 *
 */
public enum LoginTypeEnum {
	/**
	 * 普通登录，需要整页刷新
	 */
	page,

	/**
	 * ajax登录，json数据处理 
	 */
	json
}