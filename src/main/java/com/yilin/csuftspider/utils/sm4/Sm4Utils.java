package com.yilin.csuftspider.utils.sm4;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.yilin.csuftspider.constant.ConstantData;

/**
 * Title: Sm4Utils
 * Description: TODO
 * sm4 加密工具类 ，测试 jdk1.8最合适 ，jdk17会出错
 * @author Yilin
 * @version V1.0
 * @date 2022-10-18
 */
public class Sm4Utils {

    /**
     * 约定密钥16位 与前端约定 如 6666666666666666
     */
   static String key= ConstantData.sm4Salt;

    /**
     * 解密密码
     */
    public static String getDecryptPwd(String encryptHex){


        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        return decryptStr;

    }





}
