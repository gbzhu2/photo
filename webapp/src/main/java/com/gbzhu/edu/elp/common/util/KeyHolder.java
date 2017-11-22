package com.gbzhu.edu.elp.common.util;

import java.util.UUID;

/**
 * 主键生成器
 * 
 * @author 金丙传
 * 
 */
public final class KeyHolder {
	/**
	 * 生成主键
	 * @return uuid字符串
	 */
	public static String getKey() {
		return UUID.randomUUID().toString();
	}
}
