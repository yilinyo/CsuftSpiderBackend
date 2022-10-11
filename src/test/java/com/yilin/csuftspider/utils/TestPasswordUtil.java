package com.yilin.csuftspider.utils;


import com.alibaba.fastjson2.JSONObject;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final Tesseract tesseract = new Tesseract();

    static {
        // 英文资源包
        tesseract.setLanguage("eng");

        Path dataDirectory = Paths.get(System.getProperty("user.dir") + "/tessdata");

        tesseract.setDatapath(dataDirectory.toString());

        tesseract.setOcrEngineMode(7);
    }

    /**
     * 教务处账号
     */
    private static final String USERNAME = "敏感数据脱敏";

    /**
     * 教务处密码
     */
    private static final String PASSWORD = "敏感数据脱敏";

    /**
     * 打码平台 KEY
     *   https://www.345api.com/ 0.01 元/次，免费 50 次
     */
    private static final String CODE_KEY = "敏感数据脱敏";

    private static final Set<Character> dirtySet = new HashSet<Character>(128) {{
        add('A'); add('B'); add('C'); add('D'); add('E'); add('F'); add('G'); add('H'); add('I'); add('J'); add('K'); add('L');
        add('M'); add('N'); add('O'); add('P'); add('Q'); add('R'); add('S'); add('T'); add('U'); add('V'); add('W'); add('X');
        add('Y'); add('Z'); add('0'); add('1'); add('2'); add('3'); add('4'); add('5'); add('6'); add('7'); add('8'); add('9');
    }};


    /**
     * 测试工具类
     */
    @Test
    public void test() {
        System.out.println(PasswordUtil.encrypt(USERNAME, "SIKj33ZupTY3qqIG"));
    }

    /**
     * 测试教务处爬虫
     */
    @Test
    public void testLogin() throws IOException, TesseractException {
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
        nvps.add(new BasicNameValuePair("username", USERNAME));
        nvps.add(new BasicNameValuePair("password", PasswordUtil.encrypt(PASSWORD, salt)));
        nvps.add(new BasicNameValuePair("lt", mm.get("lt")));
        nvps.add(new BasicNameValuePair("dllt", mm.get("dllt")));
        nvps.add(new BasicNameValuePair("execution", mm.get("execution")));
        nvps.add(new BasicNameValuePair("_eventId", mm.get("_eventId")));
        nvps.add(new BasicNameValuePair("rmShown", mm.get("rmShown")));
        // 加入验证码判断的逻辑
        httpGet = new HttpGet("http://authserver.csuft.edu.cn/authserver/needCaptcha.html?" +
                "username=" + USERNAME +
                "&pwdEncrypt2=pwdEncryptSalt" + "&_=" + System.currentTimeMillis());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        response = httpClient.execute(httpGet);
        // 返回值中是 true or false 代表是否需要验证码
        boolean useCaptcha = EntityUtils.toString(response.getEntity()).equals("true");
        if (useCaptcha) {
            System.out.println("需要使用验证码");
            // 获取验证码
            httpGet = new HttpGet("http://authserver.csuft.edu.cn/authserver/captcha.html?ts=" + System.currentTimeMillis() % 1000);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
            response = httpClient.execute(httpGet);
            // 4kb 足够了，下载了 50 张验证码发现大小均在 2200 ~ 2300 字节左右，也可以根据 Entity 的 ContentLength 来动态创建
            byte[] bytes = new byte[1024 * 4];
            int read = response.getEntity().getContent().read(bytes);
            if  (read == -1) { // 获取图片失败
               return;
            }
            // 保存至本地
            // FileOutputStream fos = new FileOutputStream(new File("D:/a.jpg"));
            // fos.write(bytes);

            // 效果较差
            // BufferedImage image = ImageIO.read(response.getEntity().getContent());
            //
            // String result = tesseract.doOCR(image);
            // System.out.println(result);
            // System.out.println(removeDirtyData(result));

            HttpPost httpPost = new HttpPost("https://www.345api.com/api/code/ocr?key=" + CODE_KEY + "&data=" +
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes));
            response = httpClient.execute(httpPost);
            // {"code":200,"msg":"识别成功","data":{"code_data":"nk2e","require_date":0.18,"used":2,"remaining":-2},"debug":"","source":"https://www.345api.com/ 345API数据接口","exec_time":0.380164,"user_ip":"183.214.91.2"}
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            JSONObject data = JSONObject.parseObject(json).getJSONObject("data");
            if (data == null) {
                System.out.println("打码平台接口异常");
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            nvps.add(new BasicNameValuePair("captchaResponse", data.getString("code_data")));
            System.out.println(nvps);
        }


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
        String res = EntityUtils.toString(response.getEntity());
        Header location = response.getFirstHeader("location");
        // 登陆成功的时候有时会重定向到 http://authserver.csuft.edu.cn/authserver/index.do 页面，响应头也含有 location，但是是已经登陆成功的情况
        if (location != null && location.getValue().contains("authserver/login")) {
            String errorMsg = Jsoup.parse(res).getElementById("msg").text();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
        }
        System.out.println(location);
        System.out.println(res);
    }

    /**
     * 测试时间戳（毫秒级）1665510840534
     */
    @Test
    public void testMillsTimestamp() {
        System.out.println(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis() % 1000);
    }

    /**
     * 测试验证码识别
     */
    @Test
    public void testCaptcha() throws IOException, TesseractException {
        Tesseract tesseract = new Tesseract();
        // 英文资源包
        tesseract.setLanguage("eng");

        Path dataDirectory = Paths.get(System.getProperty("user.dir") + "/tessdata");

        tesseract.setDatapath(dataDirectory.toString());

        tesseract.setOcrEngineMode(1);

        BufferedImage image = ImageIO.read(new FileInputStream("D://a.jpg"));

        String result = tesseract.doOCR(image);

        System.out.println(removeDirtyData(result));
    }

    /**
     * 去除脏数据
     * @param dirtyData OCR 识别后的 脏数据
     * @return 验证码
     */
    private String removeDirtyData(String dirtyData) {
        char[] chars = dirtyData.toUpperCase().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch : chars) {
            if (builder.length() == 4) {
                break;
            }
            if (!dirtySet.contains(ch)) {
                continue;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    @Test
    public void testJsonObjectConvert() throws JSONException {
        String message = "{\"code\":200,\"msg\":\"识别成功\",\"data\":{\"code_data\":\"nk2e\",\"require_date\":0.18,\"used\":2,\"remaining\":-2},\"debug\":\"\",\"source\":\"https://www.345api.com/ 345API数据接口\",\"exec_time\":0.380164,\"user_ip\":\"183.214.91.2\"}";
        // Captcha parse = JSON.parseObject(message.getBytes(StandardCharsets.UTF_8), Captcha.class);
        JSONObject captcha = JSONObject.parseObject(message);
        System.out.println(captcha.getJSONObject("data").getString("code_data"));
    }
}
