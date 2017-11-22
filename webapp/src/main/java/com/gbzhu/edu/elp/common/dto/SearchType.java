package com.gbzhu.edu.elp.common.dto;

/**
 * 搜索类型
 * @author 黄伟
 *
 */
public enum SearchType {
     
	/**
	 * 自定义时间搜索
	 */
	uesrDefinedSearch,
	
	/**
	 * 最近30天
	 */
	thirtyDaysSearch,
	
	/**
	 *本月 
	 */
	currentMonth,
	 
	/**
	 * 本周
	 */
	 currentWeek,

	 /**
	 * 最近半年
	 */
	 lastHalfYear,

	 /**
	  * 最近3个月
	  */
	 lastThreeMonth,

	 /**
	 * 最近一个月
	 */
	 lastMonth;
}
