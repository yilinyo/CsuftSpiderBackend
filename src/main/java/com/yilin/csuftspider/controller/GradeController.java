package com.yilin.csuftspider.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.controller.utils.SessionCheck;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.model.domain.Grade;
import com.yilin.csuftspider.model.domain.LevelGrade;
import com.yilin.csuftspider.model.request.GradeSearchRequest;
import com.yilin.csuftspider.model.response.GradeAnalysisInfo;
import com.yilin.csuftspider.model.response.GradesInfo;
import com.yilin.csuftspider.service.GradeService;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;
import com.yilin.csuftspider.utils.pdf.PdfUtils;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Title: GradeController
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */

@RestController //适用于restful风格 json
@RequestMapping("/grade")
public class GradeController {

    @Resource
    private GradeService gradeService;


    //获得全部课程成绩
     @GetMapping("/getAll")
    public BaseResponse<GradesInfo> getAllGrades(HttpServletRequest request){

         // 登陆检查

         Session mySession = SessionCheck.isAlive(request);

         //获取所有课程成绩信息

         GradesInfo allGrades = gradeService.getAllGrades(mySession);


         return ResultUtils.success(allGrades);

     }

    //获得单学期课程成绩 需要传入学期
    @PostMapping("/getTerm")
    public BaseResponse<GradesInfo> getTermGrades(@RequestBody  GradeSearchRequest gradeSearchRequest,HttpServletRequest request){


        // 登陆检查
        Session mySession = SessionCheck.isAlive(request);

         String term = gradeSearchRequest.getTerm();


         if(term == null){
             //构造当前学期

             Calendar now = Calendar.getInstance();
             String nian1 = now.get(Calendar.YEAR) -1 + "-";
             String nian2 = now.get(Calendar.YEAR)  + "-" ;

             int mon=(now.get(Calendar.MONTH) + 1);
             String xueqi;
             if(mon < 7){
                 xueqi = "1";
             }else{
                 xueqi = "2";
             }

             StringBuilder sb = new StringBuilder();
             sb.append(nian1).append(nian2).append(xueqi);

             term = sb.toString();
         }else {


             //验证 参数合法性

             String regex = "\\d{4}-\\d{4}-\\d";

             if (!Pattern.matches(regex, term)) {
                 throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
             }


             int firstYear = Integer.parseInt(term.substring(0, 4));
             int secondYear = Integer.parseInt(term.substring(5, 9));

             if ((secondYear - firstYear != 1) || firstYear < 2010) {

                 throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
             }


         }


        //获取所有课程成绩信息

        GradesInfo termGrades = gradeService.getSignerTermGrades(term,mySession);


        return ResultUtils.success(termGrades);

    }

    //获得年度课程成绩 需要传入年份
    @PostMapping("/getYear")
    public BaseResponse<GradesInfo> getYearGrades(@RequestBody  GradeSearchRequest gradeSearchRequest,HttpServletRequest request){
        // 登陆检查
        Session mySession = SessionCheck.isAlive(request);

        String year = gradeSearchRequest.getTerm();


        if(year == null){
            //构造当前学年

            Calendar now = Calendar.getInstance();
            String nian1 = now.get(Calendar.YEAR) -1 + "-";
            String nian2 = now.get(Calendar.YEAR) +"" ;



            StringBuilder sb = new StringBuilder();
            sb.append(nian1).append(nian2);

            year = sb.toString();
        }else {




            //验证 参数合法性

            String regex = "\\d{4}-\\d{4}";

            if (!Pattern.matches(regex, year)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
            }


            int firstYear = Integer.parseInt(year.substring(0, 4));
            int secondYear = Integer.parseInt(year.substring(5, 9));

            if ((secondYear - firstYear != 1) || firstYear < 2010) {

                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
            }

        }




        //获取所有课程成绩信息

        GradesInfo termGrades = gradeService.getYearGrades(year,mySession);


        return ResultUtils.success(termGrades);

    }
    //获得全部等级考试成绩

    @GetMapping("/getLevel")
    public BaseResponse<List<LevelGrade>>  getLevelGrades(HttpServletRequest request){


        // 登陆检查

        Session mySession = SessionCheck.isAlive(request);

        //获取所有等级成绩

        List<LevelGrade> levelGrades = gradeService.getLevelGrade(mySession);


        return ResultUtils.success(levelGrades);


    }

    //获得成绩分析
    @GetMapping("/getAnalysis")
    public BaseResponse<GradeAnalysisInfo> getGradeAnalysis(HttpServletRequest request){

        // 登陆检查

        Session mySession = SessionCheck.isAlive(request);

        //获取所有课程成绩信息

        GradeAnalysisInfo gradeAnalysis = gradeService.getGradeAnalysis(mySession);

        User user =(User) request.getSession().getAttribute(UserService.USER_LOGIN_INFO);

        if(user == null || user.getSid()==null){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"请重新登录");
        }

        gradeAnalysis.setSid(user.getSid());


        return ResultUtils.success(gradeAnalysis);

    }

    @PostMapping("/getPdf")
    public void getPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {


        // 登陆检查

        Session mySession = SessionCheck.isAlive(request);

        GradeAnalysisInfo gradeAnalysis = gradeService.getGradeAnalysis(mySession);

        User user =(User) request.getSession().getAttribute(UserService.USER_LOGIN_INFO);

        if(user == null || user.getSid()==null|| user.getName()==null){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"请重新登录");
        }

        String name = user.getName();

        String sid = user.getSid();


        String fileName = name + "的课程成绩单";

        //设置响应格式等
        //解决ios设备问题 设置编码
        StringBuilder headerValue = new StringBuilder();
        headerValue.append("attachment;");
        headerValue.append(" filename=\"").append(encodeURIComponent(fileName)).append("\";");
        response.setHeader("content-disposition",headerValue.toString());
        response.setContentType("application/pdf");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        //暴露自定义头
        response.setHeader("Access-Control-Expose-Headers","content-disposition");

        //创建文档实例
        Document doc=new Document(PageSize.A4, 50, 50, 50, 50);




        OutputStream out = response.getOutputStream();


        PdfWriter.getInstance(doc,out);

        GradesInfo allGrades = gradeService.getAllGrades(mySession);

       PdfUtils.draw(doc,"课程成绩单",name,sid,allGrades) ;









    }


    //处理特殊符号
    private static String encodeURIComponent(String value) {
        try {
            return  URLEncoder.encode(value, "UTF-8").replace("\\+", "%20");
        } catch (Exception ex) {

            return null;
        }

    }










}
