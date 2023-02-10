package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Grade;
import com.yilin.csuftspider.model.response.GradeStatus;
import com.yilin.csuftspider.service.JedisService;
import com.yilin.csuftspider.utils.JedisUtils;
import com.yilin.csuftspider.utils.Session;
import com.yilin.csuftspider.utils.grade.HandleGradesUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class JedisServiceImpl implements JedisService {
 
    @Autowired
    private JedisUtils jedisUtils;


    @Override
    public GradeStatus getGradeStatus(Session mySession,String sid) {
        String s = mySession.get(UrlConstant.GRADES_TABEL_URL);

        //判断 是否成功请求
        Document document = Jsoup.parse(s);



        Elements titles = document.getElementsByTag("title");

        if(titles == null || (!titles.get(0).text().equals("学生个人考试成绩"))){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"连接出错，请重试或重新登录");
        }

        List<Grade> gradeList = HandleGradesUtils.getGradeList(document);

        //真实的成绩数目
        int size = gradeList.size();
        //缓存的成绩数目
        int cacheSize = jedisUtils.getNum(sid);


        GradeStatus gradeStatus = new GradeStatus();

        //redis 没有数据
        // TODO: 2023/1/15 这里要 换逻辑 存 对象序列化json字符串 ,需要脱敏分数等，重写equals来判定新出成绩 
        if(cacheSize==-1){

           jedisUtils.saveInfo(sid, String.valueOf(size));

           gradeStatus.setIsUpdated(0);
           return gradeStatus;


        }

        //判断是否 更新了数据
        if(size - cacheSize == 0){
            //数据没有更新
            gradeStatus.setIsUpdated(0);

        }else{
            //数据更新了
            gradeStatus.setIsUpdated(1);
//            int num = size - cacheSize;

           //java 8 跳过 前 size-num个
            List<Grade> updateList = gradeList.stream().skip(cacheSize).collect(Collectors.toList());

            //更新缓存
            jedisUtils.saveInfo(sid, String.valueOf(size));
            gradeStatus.setGradeList(updateList);


        }
        return gradeStatus;




    }
}
