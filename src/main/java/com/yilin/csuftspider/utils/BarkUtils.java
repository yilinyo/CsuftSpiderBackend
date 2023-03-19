package com.yilin.csuftspider.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



/**
 * Title: BarkUtils
 * Description: TODO
 * 这是用于 bark 推送到 IOS 设备的工具类
 * @author Yilin
 * @version V1.0
 * @date 2023-03-19
 */


@Component
@Slf4j
@Data
public class BarkUtils implements InitializingBean {
    @Value("${csuftspider.bark.serverUrl}")
    private  String serverUrl ;
    @Value("${csuftspider.bark.apiKey}")
    private  String apiKey ;

    @Value("${csuftspider.bark.title}")
    private  String title = "CSUFTSPIDER";
    @Value("${csuftspider.bark.icon}")
    private  String icon = "";
    @Value("${csuftspider.bark.autoCopy}")
    private  int autoCopy = 1;
    @Value("${csuftspider.bark.level}")
    private String level= "active";
    @Value("${csuftspider.bark.group}")
    private  String group ="开发通知";


    private static String LEVEL;

    private static  String SERVERUL;

    private static String APIKEY;


    private static String TITLE;

    private static String ICON ;

    private static int AUTOCOPY;

        private static String GROUP ;



    public static String buildUrl(String body){


        StringBuilder sb = new StringBuilder(SERVERUL)
                .append("/")
                .append(APIKEY)
                .append("/?icon=")
                .append(ICON)
                .append("&title=")
                .append(TITLE)
                .append("&autoCopy=")
                .append(AUTOCOPY)
                .append("&level=")
                .append(LEVEL)
                .append("&group=")
                .append(GROUP)
                .append("&body=")
                .append(body);


        return sb.toString();


    }


    public static boolean send(String body){


       Session s = new Session();

       if(body==null || "".equals(body)){
           body = "空消息";
       }

        String getUrl = buildUrl(body);

        try{
            String res = s.get(getUrl);

            JSONObject jsonObject = new JSONObject(res);
            String code = jsonObject.getString("code");

            if("200".equals(code)){

                log.info("Bark请求失败...");
                return false;

            }


        }catch (Exception e){

            log.info("Bark请求失败..."+e);
           return false;
        }


        return true;


    }

    @Override
    public void afterPropertiesSet() throws Exception {

      LEVEL = level;

        SERVERUL =serverUrl;

      APIKEY = apiKey;


      TITLE = title;

      ICON = icon;

  AUTOCOPY = autoCopy;

        GROUP =group ;


    }
}
