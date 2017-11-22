package com.gbzhu.edu.elp.common.exception;

/**
 * 系统业务异常。
 * <p>当违反业务规则时抛出此异常。
 * @author 金丙传
 * 
 */
public class ELPBizException extends RuntimeException {
	/**
	 * 构造函数
	 */
	public ELPBizException() {
		super();
	}

	/**
	 * 构造函数
	 * @param message 信息
	 */
	public ELPBizException(String message) {
		super(message);
	}
     /**
      * 构造函数
      * @param message 信息
      * @param cause cause
      */
	public ELPBizException(String message, Throwable cause) {
		super(message, cause);
	}
    /**
     * 构造函数
     * @param cause cause
     */
	public ELPBizException(Throwable cause) {
		super(cause);
	}
}
