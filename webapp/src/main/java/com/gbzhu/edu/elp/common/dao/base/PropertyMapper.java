package com.gbzhu.edu.elp.common.dao.base;

import java.lang.reflect.Method;
/**
 * 属性映射
 * @author 金丙传
 *
 */
public class PropertyMapper {
	/**
	 * 属性名称
	 */
	private String propertyName;
	/**
	 * set方法
	 */
	private Method writeMethod;
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	/**
	 * @return the writeMethod
	 */
	public Method getWriteMethod() {
		return writeMethod;
	}
	/**
	 * @param writeMethod the writeMethod to set
	 */
	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}
	
}
