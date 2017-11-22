package com.gbzhu.edu.zx.photo.web.dto;

/**
 * 权限枚举
 * @author zhuguobiao
 *
 */
public enum AuthorityType {
	
	/**
	 * 保留权限，后期扩展可能会用到
	 */
	ADMIN_DENY("拒绝访问", 0),
	
	/**
	 * 允许访问
	 */
	ADMIN_ALLOW("允许访问", 1), 
	
	/**
	 * 保留权限，后期扩展可能会用到 
	 */
	SALES_ORDER_DELETE("删除操作", 2);
	
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 序号
	 */
	private int index;

	/**
	 * 构造方法
	 * @param name 名称
	 * @param index 序号
	 */
	private AuthorityType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * get name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get index
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * set index
	 * @param index index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}