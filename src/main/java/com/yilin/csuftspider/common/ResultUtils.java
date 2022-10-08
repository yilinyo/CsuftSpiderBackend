package com.yilin.csuftspider.common;

/**
 * Title: ResultUtils
 * Description: TODO
 * 返回 工具类
 * @author Yilin
 * @version V1.0
 * @date 2022-09-20
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){

        return new BaseResponse<T>(0,data,"ok");

    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){

        return new BaseResponse<T>(errorCode);

    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String discription, String msg){

        return new BaseResponse(errorCode.getCode(),null,msg,discription);

    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(int errorCode, String discription, String msg){

        return new BaseResponse(errorCode,null,msg,discription);

    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String discription){

        return new BaseResponse(errorCode.getCode(),null,discription);

    }


}
