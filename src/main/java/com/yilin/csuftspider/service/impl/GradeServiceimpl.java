package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.LevelGrade;
import com.yilin.csuftspider.model.response.GradeAnalysisInfo;
import com.yilin.csuftspider.model.response.GradesInfo;
import com.yilin.csuftspider.service.GradeService;
import com.yilin.csuftspider.utils.Session;
import com.yilin.csuftspider.utils.grade.HandleGradesUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Title: GradeServiceimpl
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Service
@Slf4j
public class GradeServiceimpl  implements GradeService {



    //全部成绩

    @Override
    public GradesInfo getAllGrades(Session mySession) {
        //构造参数
        HashMap<String, String > paramsMap = buildParams("");

        //发起请求
        String res = mySession.post(UrlConstant.GRADES_TABEL_URL, paramsMap);

        //判断 是否成功请求
        Document document = Jsoup.parse(res);



        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!titles.get(0).text().equals("学生个人考试成绩"))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        //处理html 并返成绩表


        GradesInfo gradesInfo = HandleGradesUtils.getGradesInfo(document);



        return gradesInfo;


    }

    //单学期成绩

    @Override
    public GradesInfo getSignerTermGrades(String term, Session mySession) {


        //构造参数
        HashMap<String, String > paramsMap = buildParams(term);

        //发起请求
        String res = mySession.post(UrlConstant.GRADES_TABEL_URL, paramsMap);

        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!titles.get(0).text().equals("学生个人考试成绩"))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        //处理html 并返成绩表


        GradesInfo gradesInfo = HandleGradesUtils.getGradesInfo(document);



        return gradesInfo;


    }


    //年度成绩

    @Override
    public GradesInfo getYearGrades(String year, Session mySession) {


        //构造参数
        HashMap<String, String > paramsMap = buildParams("");

        //发起请求
        String res = mySession.post(UrlConstant.GRADES_TABEL_URL, paramsMap);

        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!"学生个人考试成绩".equals(titles.get(0).text()))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        //处理html 并返成绩表




        GradesInfo gradesInfo = HandleGradesUtils.getAnnualGradesInfo(document,year);



        return gradesInfo;



    }


    //等级考试成绩
    @Override
    public List<LevelGrade> getLevelGrade(Session mySession) {


        //发起请求
        String res =  mySession.get(UrlConstant.LEVEL_GRADES_TABLE_URL);

//        >等级考试成绩

        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!"等级考试成绩".equals(titles.get(0).text()))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }


        //处理html 并返成绩表

        List<LevelGrade> levelGrades = HandleGradesUtils.getLevelGrades(document);

        return levelGrades;



    }
    //成绩分析
    @Override
    public GradeAnalysisInfo getGradeAnalysis(Session mySession) {
        //构造参数
        HashMap<String, String > paramsMap = buildParams("");

        //发起请求
        String res = mySession.post(UrlConstant.GRADES_TABEL_URL, paramsMap);

        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!titles.get(0).text().equals("学生个人考试成绩"))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        //处理html 并返成绩表

        GradeAnalysisInfo analysis = HandleGradesUtils.getAnalysisUtil(document);


        return  analysis;



    }

    @Override
    public HashMap<String, String> buildParams(String kksj) {

        //构造参数
        HashMap<String, String > paramsMap = new HashMap<>();
        //开课时间填空为 所有时间
        paramsMap.put("kksj",kksj);
        //课程性质 默认空为全部性质
        paramsMap.put("kcxz","");
        //课程名称（粗略查询） 默认空为全部
        paramsMap.put("kcmc","");
        //显示方式，默认为 all
        paramsMap.put("xsfs","all");

        return  paramsMap;

    }
}
