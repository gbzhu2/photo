package com.gbzhu.edu.elp.common.dto.json;

/**
 * 包含对象数据的JSON对象
 * @author 黄伟
 * @param <T> 对象
 */
public class JSONResultHaveObject<T> extends StandardJSONResult{

	/**
	 * 对象数据
	 */
	private T data;

	/**
	 * 
	 * @return 对象数据
	 */
	public T getData() {
		return data;
	}

	/**
	 * 
	 * @param data 对象数据
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	
}
