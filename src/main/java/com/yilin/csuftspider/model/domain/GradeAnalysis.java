package com.yilin.csuftspider.model.domain;

import lombok.Data;

/**
 * Title: GradeAnalysis
 * Description: TODO
 * 统计每个时间的 apf ，gpa
 * @author Yilin
 * @version V1.0
 * @date 2022-09-30
 */
@Data
public class GradeAnalysis {

    /**
     * 时间
     */

    private String time;

    /**
     * 平均绩点
     */

    private Double gpa;

    /**
     * 平均分
     */

    private Double apf;
}
