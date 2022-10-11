package com.yilin.csuftspider.utils;


import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 测试 PasswordUtil 工具类
 * </p>
 *
 * @author : Xbai-hang
 * @since : 2022/10/11
 */
public class TestPasswordUtil {

    /**
     * 测试工具类
     */
    @Test
    public void test() {
        System.out.println(PasswordUtil.encrypt("666666", "SIKj33ZupTY3qqIG"));
    }

    /**
     * 测试教务处爬虫
     */
    @Test
    public void testLogin() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://authserver.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fjwgl.csuft.edu.cn%2F");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        HttpResponse response = httpClient.execute(httpGet);
        Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()));
        Elements inputs = document.select("input[type=hidden]");
        Map<String, String> mm = new HashMap<>();
        for (Element input : inputs) {
            mm.put(input.attr("name"), input.attr("value"));
        }
        System.out.println("==== lt: " + mm.get("lt"));
        // System.out.println(document.getElementsByTag("script").get(1).hasText()); // false 获取 script 标签中的内容需要使用 data
        String salt = document.getElementsByTag("script")
                .get(1).html().split("=")[2].trim().substring(1, 17);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "敏感数据脱敏"));
        nvps.add(new BasicNameValuePair("password", PasswordUtil.encrypt("敏感数据脱敏", salt)));
        nvps.add(new BasicNameValuePair("lt", mm.get("lt")));
        nvps.add(new BasicNameValuePair("dllt", mm.get("dllt")));
        nvps.add(new BasicNameValuePair("execution", mm.get("execution")));
        nvps.add(new BasicNameValuePair("_eventId", mm.get("_eventId")));
        nvps.add(new BasicNameValuePair("rmShown", mm.get("rmShown")));
        HttpPost httpPost = new HttpPost("http://authserver.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fjwgl.csuft.edu.cn%2F");
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        response = httpClient.execute(httpPost);
        // 三次重定向 均是 get 请求
        while (response.getStatusLine().getStatusCode() == 302) {
            Header header = response.getFirstHeader("location");
            String newUri = header.getValue();
            httpGet = new HttpGet(newUri);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
            response = httpClient.execute(httpGet);
        }
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
