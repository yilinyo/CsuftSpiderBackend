package com.yilin.csuftspider.model.domain;

import lombok.Data;

/**
 * Title: Grade
 * Description: TODO
 * 成绩信息实体表
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Data
public class Grade {

    /**
     * 序号
     */

    private Integer id;

    /**
     * 开课学期
     */
    private String term;

    /**
     * 课程名称
     */
    private String courseName;


    /**
     *  课程成绩
     */

    private String grade;

    /**
     *  课程学分
     */

    private Double credit;

    /**
     * 单科绩点
     */

    private  String gradePoint;

    /**
     * 课程属性 公选  必修
     */
    private String attribute;

    /**
     * 正常 0 补考 1 考试方式
     */


    private Integer status;




}
