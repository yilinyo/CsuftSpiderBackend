package com.yilin.csuftspider.model.request;

import lombok.Data;

/**
 * Title: GradeSearchRequest
 * Description: TODO
 * 成绩搜索请求类
 * @author Yilin
 * @version V1.0
 * @date 2022-09-29
 */
@Data
public class GradeSearchRequest {

    /**
     * 学年
     */
    private String year;

    /**
     * 学期
     */

    private  String term;
}
