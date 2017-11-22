package com.gbzhu.edu.elp.common.dto.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页列表。
 * <p>
 * 用于传输后端进行分页的列表数据。
 * @author znyin
 * @param <T> 列表中元素的类型。
 */
public class Pager<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数。
	 */
	private int totalCount;
	
	/**
	 * 当前页的记录列表。
	 */
	private List<T> list;
	
	/**
	 * 构造函数。
	 * @param totalCount 总记录数。
	 * @param list 当前页的记录列表。
	 */
	public Pager(int totalCount,List<T> list){
		this.totalCount=totalCount;
		this.list=list;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	
}
