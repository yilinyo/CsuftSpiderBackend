package com.yilin.csuftspider.model.domain;

import lombok.Data;

/**
 * Title: LevelGrade
 * Description: TODO
 * 等级考试信息实体表
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Data
public class LevelGrade {

    /**
     * 序号
     */

    private Integer id;

    /**
     * 考试名称
     */

    private  String examName;

    /**
     * 成绩
     */


    private Double grade;

    /**
     * 考试日期
     */

    private String date;

}
