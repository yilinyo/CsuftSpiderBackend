package com.yilin.csuftspider.constant;

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
public interface ConstantData {

    /**
     * 与前端约定的 sm4 加密密钥 通常是约定密钥16位  如 6666666666666666
     */

    String sm4Salt = "";

    /**
     *  百度orc 图片识别 ak
     *
     */


    String baiduAk = "";

    /**
     *  百度orc 图片识别 sk
     */

    String baiduSk = "";






}
