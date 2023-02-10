package com.yilin.csuftspider.service;


import com.yilin.csuftspider.utils.JedisUtils;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;


@SpringBootTest
public class JedisApplicationTests {

    //声明request变量
    private MockHttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private CourseService courseService;

    @Resource
    RedisTemplate redisTemplate1;
 
    @Test
 public   void contextLoads() {

//        request = new MockHttpServletRequest();
//        request.setCharacterEncoding("UTF-8");
//        HashMap<Integer,Integer> hs = new HashMap<>();
//        int[] nums = new int[1];
//
//        for(int num : nums){
//
//
//            hs.put(num,hs.containsKey(num)?hs.get(num)+1:1);
//
//        }
//
//

        JedisUtils j = new JedisUtils();
        Jedis jedis = j.getJedis();









//
//
//        userService.login(sid,pwd,request);
//
//        Session s = (Session)request.getSession().getAttribute(UserService.USER_LOGIN_STATE);
//
//        CourseInfo courseInfo = courseService.getCourseInfo(s, String.valueOf(1));



    }

    @Test
    public void testRedis(){


        ValueOperations valueOperations = redisTemplate1.opsForValue();
        valueOperations.set("123","223");



    }






}