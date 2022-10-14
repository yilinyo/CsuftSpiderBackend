package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Exam;
import com.yilin.csuftspider.service.ExamService;
import com.yilin.csuftspider.utils.Session;
import com.yilin.csuftspider.utils.exame.HandelExamesUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Title: ExamServiceimpl
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */
@Service
@Slf4j
public class ExamServiceimpl implements ExamService {

    //根据学期查询 考试信息


    @Override
    public List<Exam> getExamsByTerm(Session mySession, String term) {

        //构造参数
        HashMap<String, String> params = buildParams(term);
        //发起请求
        String res =  mySession.post(UrlConstant.EXAME_INFO_FORM_URL,params);


        //判断 是否成功请求
        Document document = Jsoup.parse(res);

        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!"我的考试 - 考试安排查询".equals(titles.get(0).text()))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        List<Exam> examList = HandelExamesUtils.getExameList(document);


        return examList;



    }

    @Override
    public HashMap<String, String> buildParams(String term) {
        //构造参数
        HashMap<String, String > paramsMap = new HashMap<>();
        //学期类别 期初 期中 期末 空为所有
        paramsMap.put("xqlbmc","");
        //学期时间
        paramsMap.put("xnxqid",term);
        //学期类别 1 期初 2 期中 3 期末 空为所有
        paramsMap.put("xqlb","");
        return paramsMap;
    }
}
