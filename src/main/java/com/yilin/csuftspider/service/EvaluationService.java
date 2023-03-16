package com.yilin.csuftspider.service;

import com.yilin.csuftspider.utils.Session;

import java.util.List;

/**
 * Title: EvaluationService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-03-08
 */
public interface EvaluationService {

    public List<String> getNums(Session mySession);

    public boolean commit(Session mySession,String text);

}
