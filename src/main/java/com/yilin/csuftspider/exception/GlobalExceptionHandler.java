package com.yilin.csuftspider.exception;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Title: GlobalExceptionHandler
 * Description: TODO
 * 全局异常处理器
 * @author Yilin
 * @version V1.0
 * @date 2022-09-20
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){

        log.error("businessException: "+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getDescription(),e.getMessage());

    }
    @ExceptionHandler(UnknownHostException.class)
    public BaseResponse unknownHostExceptionHandler(UnknownHostException e){

        log.error("businessException: "+e.getMessage(),e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"网络错误",e.getMessage());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runTimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"",e.getMessage());

    }

    @ExceptionHandler(SocketTimeoutException.class)
    public BaseResponse runTimeExceptionHandler(SocketTimeoutException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"请重新连接",e.getMessage());

    }


}
