package com.yilin.csuftspider.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Title: BaseResponse
 * Description: TODO
 *  通用返回类
 * @author Yilin
 * @version V1.0
 * @date 2022-09-20
 */
@Data
public class BaseResponse<T> implements Serializable {

    private  int code;

    private  T data;

    private  String msg;
    private String description;


    public BaseResponse(int code, T data, String msg,String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }

    public BaseResponse(int code, T data ,String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = "";
    }
    public BaseResponse(int code, T data ) {
        this.code = code;
        this.data = data;
        this.msg = "";
        this.description = "";
    }

    public BaseResponse(ErrorCode errorCode){

        this(errorCode.getCode(),null,errorCode.getMsg(),errorCode.getDescription());

    }

}
