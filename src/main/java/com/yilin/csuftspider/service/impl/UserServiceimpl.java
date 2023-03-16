package com.yilin.csuftspider.service.impl;
import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;

import com.yilin.csuftspider.constant.UrlConstant;

import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.IPUtil;
import com.yilin.csuftspider.utils.JsMachine;
import com.yilin.csuftspider.utils.Session;


import com.yilin.csuftspider.utils.baiduai.BaiduORCUtils;
import com.yilin.csuftspider.utils.sm4.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Title: UserServiceimpl
 * Description: TODO
 * 更新 实现 方法逻辑
 * @author Yilin
 * @version V1.0
 * @date 2022-12-12
 */

@Service
@Slf4j
public class UserServiceimpl implements UserService {

    @Resource
    RedisTemplate redisTemplate1;
    @Override
    public User login(String sid, String pwd, HttpServletRequest request) {

        // 验证参数合法性
        if(sid == null || pwd == null || StringUtils.isAnyBlank(sid ,pwd )){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");

        }

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
//        sm4解密密码
        pwd = Sm4Utils.getDecryptPwd(pwd);

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


        //检查是否需要验证码
        String need = mySession.get("http://authserver.webvpn.csuft.edu.cn/authserver/needCaptcha.html?" +
                "username=" + sid +
                "&pwdEncrypt2=pwdEncryptSalt" + "&_=" + System.currentTimeMillis());

        if("true".equals(need)){
            log.info("需要验证码");
            byte[] bytes = mySession.getYzm("http://authserver.webvpn.csuft.edu.cn/authserver/captcha.html?ts=" + System.currentTimeMillis() % 1000);

            String yzm = BaiduORCUtils.accurateBasic(bytes);
            log.info("验证码："+yzm);

            paramsMap.put("captchaResponse", yzm);


        }


        //提交表单登录
        String resText = null;

        resText = mySession.post(UrlConstant.LOGIN_URL,paramsMap);

        //检查是否登陆成功
        Document document1 = Jsoup.parse(resText);

        Elements title = document1.getElementsByTag("title");

        if(title == null || (!"中南林业科技大学 WebVPN".equals(title.get(0).text()))){

            if("统一身份认证".equals(title.get(0).text())){

                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"学号或密码错误");
            }
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学校教务系统正在维护，请稍后再重试");

        }

        resText = mySession.get("http://jwgl.webvpn.csuft.edu.cn");
        //请求失败
        if(resText == null){

            return null;
        }
        //检查是否登陆成功
        document1 = Jsoup.parse(resText);

         title = document1.getElementsByTag("title");

        if(title == null || (!"学生个人中心".equals(title.get(0).text()))){

           throw new BusinessException(ErrorCode.PARAMS_ERROR,"学校教务系统正在维护，请稍后再重试");
        }


        //拿到 姓名
        document = Jsoup.parse(resText);

        Elements el = document.getElementsByClass("Nsb_top_menu_nc");
        String strName = el.get(0).text();

        int index = strName.lastIndexOf("(");
        strName = strName.substring(0,index);

        //将mySession 和 User 信息 存入本次http请求session
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,mySession);


        User user = new User(strName,sid);

        session.setAttribute(USER_LOGIN_INFO,user);

        //统计ip日活跃量
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String redisKey = String.format("com:yilin:csuftspider:dau:%s",sd.format(new Date()));
//        String ipAdress = IPUtil.getIpAddress(request);
        HyperLogLogOperations<String,String> hyperlog = redisTemplate1.opsForHyperLogLog();
        hyperlog.add(redisKey,sid);
        log.info("登陆成功: "+strName + ",学号："+ sid);

        log.info("今日活跃用户人数：" + hyperlog.size(redisKey).intValue());

        return user;



    }
}
