package com.yilin.csuftspider.model.response;

import lombok.Data;

/**
 * Title: CourseInfo
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-07
 */

@Data
public class CourseInfo {

    /**
     * 普通课表
     */
    private String[][] arr;


    /**
     * 备注
     */

    private String extra;



}
