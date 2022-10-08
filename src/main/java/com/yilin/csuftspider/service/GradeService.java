package com.yilin.csuftspider.service;



import com.yilin.csuftspider.model.domain.LevelGrade;
import com.yilin.csuftspider.model.response.GradeAnalysisInfo;
import com.yilin.csuftspider.model.response.GradesInfo;
import com.yilin.csuftspider.utils.Session;

import java.util.HashMap;
import java.util.List;

/**
 * Title: GradeService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
public interface GradeService {




    /**
     *  获取全部成绩
     * @return
     */

    GradesInfo getAllGrades(Session mySession);


    /**
     * 根据学期获取但学期成绩
     */

    GradesInfo getSignerTermGrades(String term,Session mySession);


    /**
     * 根据学年获取一年学期成绩
     */
    GradesInfo getYearGrades(String year,Session mySession);


    /**
     * 获取等级考试列表
     */

   List<LevelGrade> getLevelGrade(Session mySession);


    /**
     * 获取成绩分析
     */

    GradeAnalysisInfo getGradeAnalysis(Session mySession);

    /**
     * 成绩查询参数构造
     */
    HashMap<String, String > buildParams(String kksj);



}
