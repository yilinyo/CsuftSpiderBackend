package com.yilin.csuftspider.service;

import com.yilin.csuftspider.model.response.CourseInfo;
import com.yilin.csuftspider.utils.Session;


import java.util.HashMap;

/**
 * Title: CourseService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-07
 */
public interface CourseService {


    /**
     * 根据周次查寻课表

     */
    CourseInfo getCourseInfo(Session mySession,String week);
    /**
     * 课表查询参数构造
     */
    HashMap<String, String > buildParams(String week);
}
