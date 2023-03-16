package com.yilin.csuftspider.utils;



import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * Title: Session
 * Description: TODO
 *  Session 工具类 自动保存cookie ，便于爬取数据 更换实现方法 更换okhttp3 为 httpClient实现，可自动管理cookie，前者bug太多所以替换为后者
 * @author Yilin
 * @version V1.0
 * @date 2022-12-12
 */
@Slf4j
public class Session {


    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();


    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    //get 请求
    public String get(String url)  {

        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        try {
            HttpResponse response = httpClient.execute(httpGet);

            return EntityUtils.toString(response.getEntity(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }



    //post 请求 自动处理重定向
    public String post(String url, HashMap<String, String > paramsMap){


        List<NameValuePair> params = new ArrayList<>();

        Set<String> keySet = paramsMap.keySet();
        for(String key:keySet) {
            String value = paramsMap.get(key);

            params.add(new BasicNameValuePair(key,value));

        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

        httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));


        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);


        // 三次重定向 均是 get 请求
            while (response.getStatusLine().getStatusCode() == 302) {

                HttpGet httpGet;
                Header header = response.getFirstHeader("location");
                String newUri = header.getValue();
                httpGet = new HttpGet(newUri);
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
                response = httpClient.execute(httpGet);
            }

            String res = EntityUtils.toString(response.getEntity(),"UTF-8");



            return  res;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    //post 请求 自动处理重定向
    public String postByOrderParams(String url, List<Pair<String,String>> lists){


        List<NameValuePair> params = new ArrayList<>();


        for(Pair<String,String> key:lists) {


            params.add(new BasicNameValuePair(key.getKey(),key.getValue()));

        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

        httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));


        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);


            // 三次重定向 均是 get 请求
            while (response.getStatusLine().getStatusCode() == 302) {

                HttpGet httpGet;
                Header header = response.getFirstHeader("location");
                String newUri = header.getValue();
                httpGet = new HttpGet(newUri);
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
                response = httpClient.execute(httpGet);
            }

            String res = EntityUtils.toString(response.getEntity(),"UTF-8");



            return  res;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }


    //获取图片验证码
    public byte[] getYzm(String url)  {
        HttpGet httpGet;
        httpGet = new HttpGet(url);
        log.info(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        try {
            CloseableHttpResponse  response = httpClient.execute(httpGet);
            // 4kb 足够了，下载了 50 张验证码发现大小均在 2200 ~ 2300 字节左右，也可以根据 Entity 的 ContentLength 来动态创建
//            byte[] bytes = new byte[1024 * 4];

            InputStream is = response.getEntity().getContent();
            byte[] bytes = inputToBytes(is);

            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * inputstream 转byte数组
     */

    public byte[] inputToBytes(InputStream inputStream) throws IOException {

        ByteArrayOutputStream swapStream  = new ByteArrayOutputStream();

        byte[] buff = new byte[100];
        int read = 0;
        while ((read = inputStream.read(buff,0,100))>0){


            swapStream.write(buff,0,read);

        }

        byte[] res = swapStream.toByteArray();

        return res;




    }



}
