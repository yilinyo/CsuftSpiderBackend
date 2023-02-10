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
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


    @Resource
    RedisTemplate redisTemplate1;

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
    public GradeAnalysisInfo getGradeAnalysis(Session mySession, String uid) {


        // 缓存

        String redisKey = String.format("com:yilin:csuftspider:cache:csinfo:%s",uid);

        ValueOperations<String, Object> stringObjectValueOperations= redisTemplate1.opsForValue();


       GradeAnalysisInfo gradeAnalysisInfo = (GradeAnalysisInfo)stringObjectValueOperations.get(redisKey);

        // 有缓存 return
        if(gradeAnalysisInfo!=null){

            return gradeAnalysisInfo;
        }

        //没缓存


        gradeAnalysisInfo = getGradeAnalysisInfoAndSet(mySession, redisKey, stringObjectValueOperations);


        return  gradeAnalysisInfo;



    }

    /**
     * 读取信息并且 set到redis
     * @param mySession
     * @param redisKey
     * @param stringObjectValueOperations
     * @return
     */

    @NotNull
    private GradeAnalysisInfo getGradeAnalysisInfoAndSet(Session mySession, String redisKey, ValueOperations<String, Object> stringObjectValueOperations) {
        GradeAnalysisInfo gradeAnalysisInfo;
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

        gradeAnalysisInfo = HandleGradesUtils.getAnalysisUtil(document);

        // 存入缓存 60s
        try {
            stringObjectValueOperations.set(redisKey,gradeAnalysisInfo,60000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.info("redis.error: "+e);
        }
        return gradeAnalysisInfo;
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

    /**
     * 缓存预热
     * 预热成绩分析
     * @param mySession
     */
    @Async
    @Override
    public void preCacheGrade(Session mySession,String uid) {

        //  必须预热


        // 缓存

        log.info(Thread.currentThread().getName()+"开始执行缓存预热");

        String redisKey = String.format("com:yilin:csuftspider:cache:csinfo:%s",uid);
        ValueOperations<String, Object> stringObjectValueOperations= redisTemplate1.opsForValue();

        getGradeAnalysisInfoAndSet(mySession, redisKey, stringObjectValueOperations);





    }
}
