package com.gbzhu.edu.elp.common.exception;
/**
 * 系统异常
 * <p><pre>
 * 使用场景有：
 * 1.用于对系统中check异常的包装。
 * 2.要抛出非业务异常时使用。
 * </pre>
 * @author 金丙传
 *
 */
public class ELPSysException extends RuntimeException{
	/**
	 * 构造函数
	 */
	public ELPSysException() {
		super();
	}

	/**
	 * 构造函数
	 * @param message 信息
	 */
	public ELPSysException(String message) {
		super(message);
	}
     /**
      * 构造函数
      * @param message 信息
      * @param cause cause
      */
	public ELPSysException(String message, Throwable cause) {
		super(message, cause);
	}
    /**
     * 构造函数
     * @param cause cause
     */
	public ELPSysException(Throwable cause) {
		super(cause);
	}
}
