package com.gbzhu.edu.zx.photo.dto;

import java.util.Date;

/**
 * 查询日期范围dto
 * 
 * @author iflytek-clfu
 * 
 */
public class DateRangeDto {

	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 初始化
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 */
	public DateRangeDto(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
