package com.yilin.csuftspider.service;

import com.yilin.csuftspider.model.response.GradeStatus;
import com.yilin.csuftspider.utils.Session;

/**
 * Title: JedisService
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-12-29
 */
public interface JedisService {


//    返回GradeStatus

    GradeStatus getGradeStatus(Session mySession,String sid);




}
