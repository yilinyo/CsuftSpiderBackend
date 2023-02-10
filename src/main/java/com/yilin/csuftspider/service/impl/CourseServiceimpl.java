package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.model.response.CourseInfo;
import com.yilin.csuftspider.service.CourseService;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;
import com.yilin.csuftspider.utils.course.HandelCourseUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Title: CourseServiceimpl
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-07
 */

@Service
@Slf4j
public class CourseServiceimpl implements CourseService {





    @Override
    public CourseInfo getCourseInfo(Session mySession, String week) {





        //构造参数
        HashMap<String, String> params = buildParams(week);
        //发起请求
        String res =  mySession.post(UrlConstant.COURSE_TABEL_URL,params);



        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!"学期理论课表".equals(titles.get(0).text()))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        CourseInfo courseInfo = HandelCourseUtils.getCourseInfo(document);






        return courseInfo;


    }
    /**
     *    参数构造
     */


    @Override
    public HashMap<String, String> buildParams(String week) {
        //构造参数
        HashMap<String, String > paramsMap = new HashMap<>();
        //周次
        paramsMap.put("zc",week);
        //不填默认为当前学期
        paramsMap.put("xnxq01id","2022-2023-2");
        //是否放大默认1
        paramsMap.put("sfFd","1");

        return paramsMap;
    }



}
