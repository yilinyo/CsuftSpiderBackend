package com.yilin.csuftspider.controller;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.controller.utils.SessionCheck;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.model.request.LoginRequset;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Title: UserController
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */

@RestController //适用于restful风格 json
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/hi")
    public String hi(){

        System.out.println("hi");
        return "hi";
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody LoginRequset loginRequset , HttpServletRequest request){


        if(loginRequset == null ){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        }
       String sid = loginRequset.getSid();
        String pwd = loginRequset.getPwd();

        if(sid == null || pwd == null){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        }


        //封装user对象
        User user = userService.login(sid, pwd, request);

        return  ResultUtils.success(user);

    }


    @PostMapping("/logout")
    public BaseResponse<Integer> userLogin(HttpServletRequest request){

            request.getSession().removeAttribute(UserService.USER_LOGIN_INFO);
            request.getSession().removeAttribute(UserService.USER_LOGIN_STATE);

        return  ResultUtils.success(1);

    }

    @GetMapping("/getCurrent")
    public BaseResponse<User> searchInfo(HttpServletRequest request){

        // 登陆检查



        Session mySession = SessionCheck.isAlive(request);

       //拿取User

        User currentUser =(User) request.getSession().getAttribute(UserService.USER_LOGIN_INFO);

        return ResultUtils.success(currentUser);


    }












}