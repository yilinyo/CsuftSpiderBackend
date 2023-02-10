package com.yilin.csuftspider.controller;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.controller.utils.SessionCheck;
import com.yilin.csuftspider.model.request.CourseSearchRequest;
import com.yilin.csuftspider.model.response.CourseInfo;
import com.yilin.csuftspider.service.CourseService;
import com.yilin.csuftspider.utils.Session;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Title: CourseController
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-07
 */
@RestController //适用于restful风格 json
@RequestMapping("/course")

public class CourseController {


    @Resource
    private CourseService courseService;

    //获得课表
    @PostMapping("/getCourse")
    public BaseResponse<CourseInfo> getTermExams(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request) {

        // 登陆检查

        Session mySession = SessionCheck.isAlive(request);




        String week = null;


        //检查参数
        if(courseSearchRequest==null || courseSearchRequest.getWeek()==null){

            week = "";

        }else {
            week = courseSearchRequest.getWeek();
        }

        CourseInfo courseInfo = courseService.getCourseInfo(mySession, week);



        return  ResultUtils.success(courseInfo);


    }


}
