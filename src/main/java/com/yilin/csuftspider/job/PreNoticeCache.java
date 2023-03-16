package com.yilin.csuftspider.job;

import com.yilin.csuftspider.model.domain.Notice;
import com.yilin.csuftspider.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: PreNoticeCache
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-02-26
 */

@Component
@EnableScheduling
@Slf4j
public class PreNoticeCache {
    @Resource
    NoticeService noticeService;


    @Scheduled(cron = "0 0 0/3 * * ?")
    public void doNoticeCache(){



        try{


            log.info("定时任务：开始预热教务处通知");

            List<Notice> notices = noticeService.getNotices();



        }catch (Exception e){



            log.error("定时任务：预热教务处通知失败"+e);


            return;

        }


        log.info("定时任务：预热成功");






    }



}
