package com.yilin.csuftspider.service;

import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.utils.JsMachine;
import com.yilin.csuftspider.utils.PasswordUtil;
import com.yilin.csuftspider.utils.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Title: Test1
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@SpringBootTest
public class Test1 {



    //声明request变量
    private MockHttpServletRequest request;


    @Resource
    private UserService userService;

    //测试 userService.login 带验证码
    @Test
    public void testLogin(){
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");


        String sid = "学号";
        String pwd = "密码";


        userService.login(sid,pwd,request);


    }


    /**
     * java 调js加密

     * @return
     */

    public static String loginByJS(String sid,String pwd){

        // java 原生加密

        Session mySession = new Session();

        //第一次进入登录 页面，并拿到 隐藏 表单数据
        String firstText  = mySession.get(UrlConstant.LOGIN_URL);

        //请求失败
        if(firstText == null){

            return null;
        }



        //请求成功
        //解析html
        Document document = Jsoup.parse(firstText);



        Element casLoginForm = document.getElementById("casLoginForm");

        Elements ltinput = casLoginForm.getElementsByAttributeValue("name", "lt");

        //获取lt值
        String lt = ltinput.get(0).attributes().get("value");

        //获取key值
        Elements script = document.getElementsByTag("script");
        Element element1 = script.get(1);
        String str = element1.toString();

        int i = str.lastIndexOf("= \"");
        int j = str.lastIndexOf("\";");
        int len = j-i+1;
        String key = str.substring(i+3,j);

        //使用js逆向加密获取加密密码
        String sign = JsMachine.encryptJs(pwd,key);

        //构造登录参数
        HashMap<String, String > paramsMap = new HashMap<>();
        //学号
        paramsMap.put("username",sid);
        //加密后的密码
        paramsMap.put("password",sign);
        //lt凭证
        paramsMap.put("lt",lt);
        //登录方式，写死
        paramsMap.put("dllt", "userNamePasswordLogin");
        //第一次登录错误一次 ，可以写死
        paramsMap.put("execution","e1s1");
        //写死
        paramsMap.put("_eventId","submit");
        //验证码相关，写死
        paramsMap.put("rmShown","1");


        //提交表单登录
        String resText = null;

        resText = mySession.post(UrlConstant.LOGIN_URL,paramsMap);
        //请求失败
        if(resText == null){

            return null;
        }

        return   resText;
    }

    /**
     * java 原生加密

     * @return
     */
    public static String loginByJava (String sid,String pwd){

        // java 原生加密

        Session mySession = new Session();

        //第一次进入登录 页面，并拿到 隐藏 表单数据
        String firstText  = mySession.get(UrlConstant.LOGIN_URL);

        //请求失败
        if(firstText == null){

            return null;
        }



        //请求成功
        //解析html
        Document document = Jsoup.parse(firstText);



        Element casLoginForm = document.getElementById("casLoginForm");

        Elements ltinput = casLoginForm.getElementsByAttributeValue("name", "lt");

        //获取lt值
        String lt = ltinput.get(0).attributes().get("value");

        //获取key值
        Elements script = document.getElementsByTag("script");
        Element element1 = script.get(1);
        String str = element1.toString();

        int i = str.lastIndexOf("= \"");
        int j = str.lastIndexOf("\";");
        int len = j-i+1;
        String key = str.substring(i+3,j);

        //使用java 原生加密获取加密密码
        String sign =   PasswordUtil.encrypt(pwd,key);

        //构造登录参数
        HashMap<String, String > paramsMap = new HashMap<>();
        //学号
        paramsMap.put("username",sid);
        //加密后的密码
        paramsMap.put("password",sign);
        //lt凭证
        paramsMap.put("lt",lt);
        //登录方式，写死
        paramsMap.put("dllt", "userNamePasswordLogin");
        //第一次登录错误一次 ，可以写死
        paramsMap.put("execution","e1s1");
        //写死
        paramsMap.put("_eventId","submit");
        //验证码相关，写死
        paramsMap.put("rmShown","1");


        //提交表单登录
        String resText = null;

        resText = mySession.post(UrlConstant.LOGIN_URL,paramsMap);
        //请求失败
        if(resText == null){

            return null;
        }

        return   resText;

    }

    //测试哪种加密方法快

    @Test
    public void logintest(){



        String sid = "学号";
        String pwd = "密码";



        long startTime = System.currentTimeMillis();

        loginByJava(sid, pwd);

        long endTime = System.currentTimeMillis();


        System.out.println("java调用运行时间：" + (endTime - startTime) + "ms");

        System.out.println("-----------------------");


        startTime = System.currentTimeMillis();

        loginByJS(sid, pwd);

        endTime = System.currentTimeMillis();

        System.out.println("java调用js运行时间：" + (endTime - startTime) + "ms");







    }





}
