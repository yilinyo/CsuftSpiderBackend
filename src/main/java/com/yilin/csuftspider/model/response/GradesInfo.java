package com.yilin.csuftspider.model.response;

import com.yilin.csuftspider.model.domain.Grade;
import lombok.Data;

import java.util.List;

/**
 * Title: GradesInfo
 * Description: TODO
 * 详细成绩返回类
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Data
public class GradesInfo {

    /**
     * 成绩列表
     */
    private List<Grade> gradeList;

    /**
     * 平均绩点
     */

    private Double gpa;

    /**
     * 平均分
     */

    private Double apf;

    /**
     * 课程数量
     */

    private Integer num;

    /**
     * 基本学业分
     */

    private Double  BasicPoint;




    public GradesInfo() {
    }

    public GradesInfo(List<Grade> gradeList, Double gpa, Double apf, Integer num) {
        this.gradeList = gradeList;
        this.gpa = gpa;
        this.apf = apf;
        this.num = num;
    }

    public GradesInfo(List<Grade> gradeList, Double gpa, Double apf, Integer num, Double basicPoint) {
        this.gradeList = gradeList;
        this.gpa = gpa;
        this.apf = apf;
        this.num = num;
        BasicPoint = basicPoint;
    }
}
