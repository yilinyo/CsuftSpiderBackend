package com.yilin.csuftspider.utils.baiduai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Title: BaiduORCUtils
 * Description: TODO
 * 用于调用 百度 ocr 的工具类 ，可自行去百度https://ai.baidu.com/ai-doc/OCR/1k3h7y3db 查看免费额度很多
 * @author Yilin
 * @version V1.0
 * @date 2022-10-12
 */
public class BaiduORCUtils {


        /**
         * 获取权限token
         * @return 返回示例：
         * {
         * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
         * "expires_in": 2592000
         * }
         */
        public static String getAuth() {
            // 官网获取的 API Key 更新为你注册的
            String clientId = "0bR9K8lrpip4KbOtNqlb0CF2";
            // 官网获取的 Secret Key 更新为你注册的
            String clientSecret = "6dl1cFMA6k0PLAiEgOnNLmZkhyMKwkG7";
            return getAuth(clientId, clientSecret);
        }

        /**
         * 获取API访问token
         * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
         * @param ak - 百度云官网获取的 API Key
         * @param sk - 百度云官网获取的 Securet Key
         * @return assess_token 示例：
         * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
         */
        public static String getAuth(String ak, String sk) {
            // 获取token地址
            String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
            String getAccessTokenUrl = authHost
                    // 1. grant_type为固定参数
                    + "grant_type=client_credentials"
                    // 2. 官网获取的 API Key
                    + "&client_id=" + ak
                    // 3. 官网获取的 Secret Key
                    + "&client_secret=" + sk;
            try {
                URL realUrl = new URL(getAccessTokenUrl);
                // 打开和URL之间的连接
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.err.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                /**
                 * 返回结果示例
                 */
                System.err.println("result:" + result);
                JSONObject jsonObject = new JSONObject(result);
                String access_token = jsonObject.getString("access_token");
                return access_token;
            } catch (Exception e) {
                System.err.printf("获取token失败！");
                e.printStackTrace(System.err);
            }
            return null;
        }


    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String accurateBasic(byte[] imgData) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {
            // 本地文件路径
//            String filePath = "[本地文件路径]";
//            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, param);


            JSONObject jsonObject = new JSONObject(result);

//    成功响应数据        {"words_result":[{"words":"ZffX"}],"words_result_num":1,"log_id":1580230448679439816}

            JSONArray words_result = jsonObject.getJSONArray("words_result");

            JSONObject o = words_result.getJSONObject(0);

            String data = o.getString("words");
            //识别了一些空格脏数据需要去除
            data = data.replace(" ","");

// 返回验证码数据
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    }



