package com.yilin.csuftspider.model.response;

import com.yilin.csuftspider.model.domain.GradeAnalysis;
import lombok.Data;

import java.util.List;

/**
 * Title: GradeAnalysisInfo
 * Description: TODO
 * 按学期时间 返回gpa ，apf
 * @author Yilin
 * @version V1.0
 * @date 2022-09-30
 */
@Data
public class GradeAnalysisInfo {


    /**
     *学号
     */
    private String sid ;
    /**
     * 按学期来 的分析列表
     */
    private List<GradeAnalysis> termList ;
    /**
     * 按学期来 的分析列表
     */
    private List<GradeAnalysis> yearList ;
    /**
     * 按年份来的 分析列表
     */

    /**
     * 已获学分
     */
    private Double allCredit;

    /**
     * 所有gpa
     */
    private Double allGpa;
    /**
     * 所有Apf
     */
    private Double allApf;









}
