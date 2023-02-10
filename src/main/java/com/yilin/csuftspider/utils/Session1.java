package com.yilin.csuftspider.utils;


import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Title: Session1
 * Description: TODO
 * 由okhttp3 实现的session 类 ，2022-12-12已弃用，先更换为 用HttpClient 实现的 Session类
 * @author Yilin
 * @version V1.0
 * @date 2022-12-12
 */
@Deprecated
public class Session1 {
    //okHttpClient 支持重定向 支持拦截器 支持cookiejar

    public static Cookie cookie;

    private final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().followRedirects(true).addInterceptor(new BasicParamsInterceptor()).cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {


//
//            System.out.println("-------自动装载------");
            cookieStore.put(url.host(), cookies);
            List<Cookie> cookies1 = cookieStore.get(url.host());
//
//            for (Cookie c : cookies1){
//                System.out.println(c);
//            }
//
//            System.out.println("-------store------");

        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());

            if(cookies!=null) {
                for (Cookie c : cookies) {


                    System.out.println(c);
                    System.out.println(">>>>>");

                }

                System.out.println("-------------");
            }
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
