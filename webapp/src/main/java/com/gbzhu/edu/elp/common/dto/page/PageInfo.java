package com.gbzhu.edu.elp.common.dto.page;

/**
 * 分页信息
 * 
 * @author 金丙传
 * 
 */
public class PageInfo {

	/**
	 * 每页多少条数据
	 */
	private final Integer defaultPageSize = 10;
	/**
	 * 
	 */
	private Integer totalCount;
	/**
	 * 
	 */
	private Integer pageSize = defaultPageSize;
	/**
	 * 
	 */
	private Integer totalPage;
	/**
	 * 
	 */
	private Integer firstPage = 1;
	/**
	 * 
	 */
	private Integer prevPage;
	/**
	 * 
	 */
	private Integer nextPage;
	/**
	 * 
	 */
	private Integer lastPage;
	/**
	 * 
	 */
	private Integer currentPage;
	/**
	 * 
	 */
	private Integer[] allPages;

	/**
	 * 默认构造函数
	 */
	public PageInfo() {
	}

	/**
	 * 构造函数
	 * 
	 * @param totalCount
	 *            总页数
	 * @param currentPage
	 *            当前页
	 */
	public PageInfo(Integer totalCount, Integer currentPage) {
		this.totalCount = totalCount;
		this.currentPage = currentPage;
	}

	/**
	 * 构造函数
	 * 
	 * @param totalCount
	 *            总页数
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页大小
	 */
	public PageInfo(Integer totalCount, Integer currentPage, Integer pageSize) {
		this(totalCount, currentPage);
		this.pageSize = pageSize;
	}

	/**
	 * @return the total_count
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalPage
	 */
	public Integer getTotalPage() {
		if (this.totalCount % this.pageSize == 0)
			totalPage = this.totalCount / this.pageSize;
		else
			totalPage = this.totalCount / this.pageSize + 1;
		return totalPage;
	}

	/**
	 * @return the currentPage
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the firstPage
	 */
	public Integer getFirstPage() {
		return firstPage;
	}

	/**
	 * @return the prevPage
	 */
	public Integer getPrevPage() {
		prevPage = this.getCurrentPage() == 1?1: this.getCurrentPage()- 1;
		return prevPage;
	}

	/**
	 * @return the nextPage
	 */
	public Integer getNextPage() {
		if (this.nextPage != null && this.nextPage >= this.getLastPage())
			nextPage = this.getLastPage();
		else {
			nextPage = this.getCurrentPage() + 1;
		}
		return nextPage;
	}

	/**
	 * @return the lastPage
	 */
	public Integer getLastPage() {
		lastPage = getTotalPage();
		return lastPage;
	}

	/**
	 * @return the allPages
	 */
	public Integer[] getAllPages() {
		Integer[] pages = new Integer[getTotalPage()];
		for (int i = 1; i <= getTotalPage(); i++) {
			pages[i - 1] = i;
		}
		this.allPages = pages;
		return allPages;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
