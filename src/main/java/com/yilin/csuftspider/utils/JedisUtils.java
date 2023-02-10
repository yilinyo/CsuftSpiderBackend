package com.yilin.csuftspider.utils;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * @ClassName JedisUtils
 * @Description TODO
 * @Author admin
 * @Version 1.0
 */
@Component
public class JedisUtils {
    @Autowired
    private JedisPool jedisPool;
 
    /**
     * 获取Jedis资源
     */
    public Jedis getJedis(){
        return jedisPool.getResource();
    }
    /**
     * 释放Jedis连接
     */
    public void close(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    /**
     * 获取数量
     * @param key
     * @return
     */

    public Integer getNum(String key) {
        Jedis jedis = this.getJedis();

        Boolean exists = jedis.exists(key);

        Integer num = -1;

        if(exists){

            try {
                String s = jedis.get(key);
                num = Integer.valueOf(s);

            }catch (Exception e){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }


        }

        this.close(jedis);

        return num;
    }

    /**
     * 存放信息
     * @param key
     * @param num
     */
    public void saveInfo(String key, String num) {

        Jedis jedis = this.getJedis();

        Boolean exists = jedis.exists(key);



            jedis.set(key,num);



        this.close(jedis);

    }

 
}