package com.yilin.csuftspider.utils;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.utils.baiduai.BaiduORCUtils;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: TestBaiduOrcUtil
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-12
 */
@SpringBootTest
public class TestBaiduOrcUtil {

    /**
     * 教务处账号
     */
    private static final String USERNAME = "学号";

    /**
     * 教务处密码
     */
    private static final String PASSWORD = "密码";

    @Test
    public void getAuth() throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://authserver.webvpn.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fwebvpn.csuft.edu.cn%2Fusers%2Fauth%2Fcas%2Fcallback%3Furl%3Dhttp%253A%252F%252Fwebvpn.csuft.edu.cn%252F");
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
        nvps.add(new BasicNameValuePair("username", USERNAME));
        nvps.add(new BasicNameValuePair("password", PasswordUtil.encrypt(PASSWORD, salt)));
        nvps.add(new BasicNameValuePair("lt", mm.get("lt")));
        nvps.add(new BasicNameValuePair("dllt", mm.get("dllt")));
        nvps.add(new BasicNameValuePair("execution", mm.get("execution")));
        nvps.add(new BasicNameValuePair("_eventId", mm.get("_eventId")));
        nvps.add(new BasicNameValuePair("rmShown", mm.get("rmShown")));
        // 加入验证码判断的逻辑
        httpGet = new HttpGet("http://authserver.webvpn.csuft.edu.cn/authserver/needCaptcha.html?" +
                "username=" + USERNAME +
                "&pwdEncrypt2=pwdEncryptSalt" + "&_=" + System.currentTimeMillis());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        response = httpClient.execute(httpGet);
        // 返回值中是 true or false 代表是否需要验证码
        boolean useCaptcha = EntityUtils.toString(response.getEntity()).equals("true");
        if (useCaptcha) {
            System.out.println("需要使用验证码");
            // 获取验证码
            httpGet = new HttpGet("http://authserver.webvpn.csuft.edu.cn/authserver/captcha.html?ts=" + System.currentTimeMillis() % 1000);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
            response = httpClient.execute(httpGet);
            // 4kb 足够了，下载了 50 张验证码发现大小均在 2200 ~ 2300 字节左右，也可以根据 Entity 的 ContentLength 来动态创建
            byte[] bytes = new byte[1024 * 20];
            int read = response.getEntity().getContent().read(bytes);
            if (read == -1) { // 获取图片失败
                return;
            }
            //验证码
            String yzm = BaiduORCUtils.accurateBasic(bytes);

            nvps.add(new BasicNameValuePair("captchaResponse", yzm));

            HttpPost httpPost = new HttpPost("http://authserver.webvpn.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fwebvpn.csuft.edu.cn%2Fusers%2Fauth%2Fcas%2Fcallback%3Furl%3Dhttp%253A%252F%252Fwebvpn.csuft.edu.cn%252F");
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
            String res = EntityUtils.toString(response.getEntity());
            Header location = response.getFirstHeader("location");
            // 登陆成功的时候有时会重定向到 http://authserver.csuft.edu.cn/authserver/index.do 页面，响应头也含有 location，但是是已经登陆成功的情况
            if (location != null && location.getValue().contains("authserver/login")) {
                String errorMsg = Jsoup.parse(res).getElementById("msg").text();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
            System.out.println(location);
            System.out.println(res);

            httpGet = new HttpGet("http://jwgl.webvpn.csuft.edu.cn");

            CloseableHttpResponse execute = httpClient.execute(httpGet);

            String s = execute.getEntity().getContent().toString();

            System.out.println(s);



        }
    }
    //测试 百度 ak sk
    @Test
    public void testToken(){
        BaiduORCUtils.getAuth();

    }

}
