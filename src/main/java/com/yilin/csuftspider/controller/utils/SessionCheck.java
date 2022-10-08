package com.yilin.csuftspider.controller.utils;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: SessionCheck
 * Description: TODO
 * 判断是否是登录态的 工具
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */
public class SessionCheck {

    public static Session isAlive(HttpServletRequest request){


        Session mySession = (Session) request.getSession().getAttribute(UserService.USER_LOGIN_STATE);

        if(mySession == null){
            System.out.println("非法！");
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"请登录");

        }

        return mySession;


    }




}
