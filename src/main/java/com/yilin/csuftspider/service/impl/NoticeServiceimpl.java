package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Notice;

import com.yilin.csuftspider.service.NoticeService;
import com.yilin.csuftspider.utils.Session;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Title: NoticeServiceimpl
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-02-26
 */
@Slf4j
@Service
public class NoticeServiceimpl implements NoticeService {



    @Resource
    RedisTemplate redisTemplate1;

    @Resource
    Session session;


    @Override
    public List<Notice> getNotices() {
        String redisKey = "com:yilin:csuftspider:cache:notices";

        ValueOperations<String, Object> stringObjectValueOperations= redisTemplate1.opsForValue();

        List<Notice> notices = (List<Notice>)stringObjectValueOperations.get(redisKey);

        // 有缓存 return
        if(notices!=null){

            return notices;
        }
        //没缓存
        String txt = session.get(UrlConstant.BASE_NOTICE_URL);


        Document parse = Jsoup.parse(txt);

        Elements title = parse.getElementsByTag("title");

        if(title == null || (!"教务处-通知公告-".equals(title.get(0).text()))){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学校教务系统正在维护，请稍后再重试");
        }




        Elements rt_detail = parse.getElementsByClass("rt detail");

        Elements lis = rt_detail.get(0).getElementsByTag("li");




        List<Notice> lists = new ArrayList<>();



        for(Element li:lis){

            Notice notice = new Notice();
            Element a = li.getElementsByTag("a").get(0);


            String href = a.attr("href");

            String link = href.replaceFirst("./", UrlConstant.BASE_NOTICE_URL);

            String context = a.text();

            String date = li.getElementsByTag("span").get(0).text();

            notice.setContext(context);
            notice.setLink(link);

            notice.setDate(date);

            lists.add(notice);

        }
        // 存入缓存 2 hour
        try {
            stringObjectValueOperations.set(redisKey,lists,2, TimeUnit.HOURS);
        } catch (Exception e) {
            log.info("redis.error: "+e);
        }
        return lists;


    }
}
