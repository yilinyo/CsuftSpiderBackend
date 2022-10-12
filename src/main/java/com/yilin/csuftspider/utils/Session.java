package com.yilin.csuftspider.utils;


import okhttp3.*;

import java.util.*;


/**
 * Title: Session
 * Description: TODO
 *  Session 工具类 自动保存cookie ，便于爬取数据
 * @author Yilin
 * @version V1.0
 * @date 2022-09-26
 */
public class Session {
    //okHttpClient 支持重定向 支持拦截器 支持cookiejar


    private final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().followRedirects(true).addInterceptor(new BasicParamsInterceptor()).cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            cookieStore.put(url.host(), cookies);
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return  (cookies != null) ? cookies : new ArrayList<Cookie>();
        }
    }).build();




    /**
     * @param url  要请求的url
     * @param  paramsMap post的请求参数
     * @return  post的返回结果
     */
    public String post(String url, HashMap<String, String > paramsMap){

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = paramsMap.keySet();
        for(String key:keySet) {
            String value = paramsMap.get(key);
            formBodyBuilder.add(key,value);

        }
        FormBody formBody = formBodyBuilder.build();

        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();


        try (Response response = mOkHttpClient.newCall(request).execute()) {

            String  respStr = response.body().string();
            return respStr;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String get(String url)  {

        final Request.Builder builder = new Request.Builder();
        builder.url(url);
        final Request request = builder
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {

//            if(response.code() != 200){
//                return null;
//            }
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//验证码图片 获取byte[]
    public byte[] getYzm(String url)  {

        final Request.Builder builder = new Request.Builder();
        builder.url(url);
        final Request request = builder
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {

//            if(response.code() != 200){
//                return null;
//            }
            return response.body().bytes();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

   }
