package com.yilin.csuftspider.service;


import com.yilin.csuftspider.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: UserService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
public interface  UserService   {

    public static final String USER_LOGIN_STATE = "userLoginState";

    public static final String USER_LOGIN_INFO = "userLoginInfo";
    /**
     * 登录
     * @return
     */
    User login(String sid, String pwd, HttpServletRequest request);



}
