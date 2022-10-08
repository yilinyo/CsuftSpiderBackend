package com.yilin.csuftspider.service;

import com.yilin.csuftspider.model.domain.Exam;
import com.yilin.csuftspider.utils.Session;

import java.util.HashMap;
import java.util.List;

/**
 * Title: ExamService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */
public interface ExamService {

    /**
     * 根据学期查询考试信息
     */

    List<Exam> getExamsByTerm(Session mySession, String term);
    /**
     * 查询参数构造
     */


    HashMap<String, String > buildParams(String kksj);

}
