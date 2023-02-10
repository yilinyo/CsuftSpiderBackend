package com.yilin.csuftspider.model.response;

import com.yilin.csuftspider.model.domain.Grade;
import lombok.Data;

import java.util.List;

/**
 * Title: GradeStatus
 * Description: TODO
 * 成绩状态 是否有成绩更新
 * @author Yilin
 * @version V1.0
 * @date 2022-12-29
 */
@Data
public class GradeStatus {


    /**
     * 是否有成绩更新 0 没有 1 有
     *
     */
    private Integer isUpdated;

    /**
     * 成绩列表
     */
    private List<Grade> gradeList;






}
