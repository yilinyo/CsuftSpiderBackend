package com.yilin.csuftspider.utils;


import okhttp3.*;


import java.io.IOException;

/**
 *  定义 okhttpclient 全局请求拦截器
 *  "Cookie","SERVERID=122" 这个很重要需要添加
 *  10/12 今天突然发现这个SERVERID会变，比如今天变成了124，你得每次发现学校那边变了就要改下，但Python不会出现这种问题，不知道为什么。
 *  11/29 122
 *  2022 - 12/12 不再维护此 拦截器 ,okhttp3 已弃用，更换为 httpclient ，此类已弃用
 */
@Deprecated
public class BasicParamsInterceptor  implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {




        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36 Edg/100.0.1185.36")
                .addHeader("Referer","http://authserver.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fjwgl.csuft.edu.cn%2F")

                .build();
        return chain.proceed(request);
    }
}