package com.gbzhu.edu.elp.common.exception;

/**
 * 事务不回滚业务异常
 *
 * <p>需要限制业务异常而引起的回滚，使用此异常
 * @author yywang5
 */
public class ELPNoRollbackBizException extends ELPBizException{

    /**
     * 构造函数
     */
    public ELPNoRollbackBizException() {
        super();
    }

    /**
     * 构造函数
     * @param message 信息
     */
    public ELPNoRollbackBizException(String message) {
        super(message);
    }
    /**
     * 构造函数
     * @param message 信息
     * @param cause cause
     */
    public ELPNoRollbackBizException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * 构造函数
     * @param cause cause
     */
    public ELPNoRollbackBizException(Throwable cause) {
        super(cause);
    }
}
