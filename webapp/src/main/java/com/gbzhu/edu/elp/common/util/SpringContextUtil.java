package com.gbzhu.edu.elp.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获得整个工程的上下文环境
 * 
 * @author 金丙传
 * 
 */
public final class SpringContextUtil implements ApplicationContextAware {
	/**
	 * 上下文
	 */
	private static ApplicationContext ctu;

	/**
	 * 私有构造函数
	 */
	private SpringContextUtil() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
			ctu = applicationContext;
	}

	/**
	 * 获得上下文
	 * 
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return ctu;
	}

}
