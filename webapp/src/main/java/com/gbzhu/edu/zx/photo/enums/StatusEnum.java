package com.gbzhu.edu.zx.photo.enums;

/**
 * 状态枚举
 * @author iflytek-clfu
 *
 */
public enum StatusEnum {
	/**
	 * 无效的
	 */
	INVALID(0),
	
	/**
	 * 有效的
	 */
	VALID(1);
	
	/**
	 * 状态值
	 */
	private Integer value;
	
	/**
	 * 状态值构造
	 * @param value 值
	 */
	private StatusEnum(Integer value){
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
}
