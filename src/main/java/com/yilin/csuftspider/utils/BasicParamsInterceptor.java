package com.yilin.csuftspider.utils;


import okhttp3.*;


import java.io.IOException;

/**
 *  定义 okhttpclient 全局请求拦截器
 *  "Cookie","SERVERID=122" 这个很重要需要添加
 */

public class BasicParamsInterceptor  implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .addHeader("Referer","http://authserver.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fjwgl.csuft.edu.cn%2F")
                .addHeader("Cookie","SERVERID=122")
                .build();
        return chain.proceed(request);
    }
}