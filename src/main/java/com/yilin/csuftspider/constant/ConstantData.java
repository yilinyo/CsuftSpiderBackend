package com.yilin.csuftspider.constant;

import lombok.Data;
import org.springframework.aop.IntroductionInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Title: ConstantData
 * Description: TODO
 * 一些敏感数据 常量 便于管理
 * @author Yilin
 * @version V1.0
 * @date 2022-12-12
 */
@Component
@Data
public class ConstantData implements InitializingBean {

    /**
     * 与前端约定的 sm4 加密密钥 通常是约定密钥16位  如 6666666666666666
     */
    @Value("${csuftspider.system.login.salt}")
    private String sm4Salt;

    /**
     *  百度orc 图片识别 ak
     *
     */

    @Value("${csuftspider.baidu.ocr.baiduAk}")
    private String baiduAk;

    /**
     *  百度orc 图片识别 sk
     */
    @Value("${csuftspider.baidu.ocr.baiduSk}")
    private String baiduSk;



    public static String UVALUATION_BASE = "老师在教学态度上，能得到学生的认可，如有高度的责任心，对学生有耐心、爱心，教书育人，遵守教学纪律，并能以身作则，为人师表;在教学内容上，能够从学生的实际水平出发，认真备课，内容充实，重点突出。在教学过程中，对学生一视同仁，鼓励学生主动探求知识点，承认差异，关注基础薄弱的学生，善于激励，耐心辅导，给基础弱的学生以表现的机会，让他们尝试成功的喜悦;在教学方法上，能够合理运用现代化教学手段，且教法灵活，课堂气氛活跃，注意知识性和趣味性的结合。";


    public static String SM4SALT;

    public static String BAIDUAK;

    public static String BAIDUSK;


    @Override
    public void afterPropertiesSet() throws Exception {

        SM4SALT = sm4Salt;

        BAIDUAK = baiduAk;

         BAIDUSK =baiduSk;

    }
}
