package com.yilin.csuftspider.exception;

import com.yilin.csuftspider.common.ErrorCode;

/**
 * Title: BusinessException
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */

    private final int code ;

    /**
     * 描述
     */
    private final String description;

    public BusinessException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode, String description){

        super(errorCode.getMsg());
        this.code = errorCode.getCode();

        this.description = description;


    }
    public BusinessException(ErrorCode errorCode){

        super(errorCode.getMsg());
        this.code = errorCode.getCode();

        this.description = errorCode.getDescription();


    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}