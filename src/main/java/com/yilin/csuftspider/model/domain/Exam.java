package com.yilin.csuftspider.model.domain;

import lombok.Data;

/**
 * Title: Exam
 * Description: TODO
 * 考试信息实体表
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Data
public class Exam {

    /**
     * 序号
     */

    private  Integer id;

    /**
     * 姓名
     */

    private  String name;

    /**
     * 考试时间
     */

    private  String date;

    /**
     * 考试地点
     */

    private  String location;



}
