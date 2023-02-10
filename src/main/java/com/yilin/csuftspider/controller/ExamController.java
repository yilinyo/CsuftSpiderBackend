package com.yilin.csuftspider.controller;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.controller.utils.SessionCheck;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.model.domain.Exam;
import com.yilin.csuftspider.service.ExamService;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Title: ExamController
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */

@RestController //适用于restful风格 json
@RequestMapping("/exam")

public class ExamController {


    @Resource
    private ExamService examService;

    //获得学期 考试安排
    @GetMapping("/getTerm")
    public BaseResponse<List<Exam>> getTermExams(HttpServletRequest request){

        // 登陆检查

        Session mySession = SessionCheck.isAlive(request);






        //学期构造
        String term ;

         Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);


        int mon=(now.get(Calendar.MONTH) + 1);



        if(mon >= 9){

            String nian1 = year + "";

            String nian2 = (year + 1) + "";

            term = nian1 +"-"+nian2 + "-"+"1";

        }else{

            String nian1 = year + "";

            String nian2 = (year - 1) + "";

            term = nian2 +"-"+nian1 + "-"+"2";

        }





        //获取所有课程成绩信息

        List<Exam> examList = examService.getExamsByTerm(mySession,term);


        return ResultUtils.success(examList);

    }






}
