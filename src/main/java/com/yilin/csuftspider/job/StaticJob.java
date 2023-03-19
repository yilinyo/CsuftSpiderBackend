package com.yilin.csuftspider.job;

import com.yilin.csuftspider.model.domain.Notice;
import com.yilin.csuftspider.utils.BarkUtils;
import com.yilin.csuftspider.utils.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Title: StaticJob
 * Description: TODO
 * 统计 定时任务
 * @author Yilin
 * @version V1.0
 * @date 2023-03-19
 */

@Component
@EnableScheduling
@Slf4j
public class StaticJob {

    @Autowired
    private JedisUtils jedisUtils;

    @Resource
    RedisTemplate redisTemplate1;

    @Scheduled(cron = "1 0 0 * * ?")
    public void doStatic(){


        Long totalNum = jedisUtils.getSize();


        //统计ip日活跃量
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        //前一天
        calendar.add(Calendar.DATE,-1);

        Date time = calendar.getTime();
        String redisKey = String.format("com:yilin:csuftspider:dau:%s",sd.format(time));


        HyperLogLogOperations<String,String> hyperlog = redisTemplate1.opsForHyperLogLog();

        int dailyNum = hyperlog.size(redisKey).intValue();




        String message = sd.format(time)+"日活跃"+ dailyNum+ "人"+ ",累计"+totalNum+"位用户";

        //推送到bark ios
        BarkUtils.send(message);


        log.info(message);



    }

}
