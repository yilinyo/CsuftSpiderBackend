package com.yilin.csuftspider.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;
import java.util.Random;

/**
 * <p>
 *  教务处密码加密工具类
 * </p>
 *
 * @author : Xbai-hang
 * @since : 2022/10/11
 */
@Slf4j
public class PasswordUtil {

    static {
        // Java 默认不支持 PKCS7Padding, 需要手动加包
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
     */
    private static final String PADDING_CHARS = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";

    /**
     * 区块大小
     */
    private static final int BLOCK_SIZE = 16;

    /**
     * PKCS7Padding 填充字符
     */
    @Deprecated
    private static final Byte[] FILL_BYTES = {0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15};

    /**
     * 密码加密工具类
     * @param password 密码
     * @param key key
     * @return 加密后的密钥
     */
    public static String encrypt(String password, String key) {
        return aesEncrypt(password, key);
    }

    /**
     * AES 128 CBC 标准加密
     * @param password 密码
     * @param key AES 密钥
     * @return 加密后的密钥
     */
    private static String aesEncrypt(@NotNull String password, @NotNull String key) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("密码为空或 key 为空");
        }
        if (key.length() != BLOCK_SIZE) {
            return null;
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        // 设置密钥规范为 AES
        SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
        // 算法/模式/补码方式
        Cipher cipher;
        try {
             cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error("encrypt algorithm error");
            e.printStackTrace();
            return "";
        }
        IvParameterSpec iv = new IvParameterSpec(randomString(BLOCK_SIZE).getBytes(StandardCharsets.UTF_8));
        byte[] encrypted;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, spec, iv);
            encrypted = cipher.doFinal(getPaddingPassword(password).getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("password padding error");
            e.printStackTrace();
            return "";
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 随机产生 length 个字符并拼接成串
     * @param len 长度 len
     * @return 随机字符组成的字符串
     */
    private static String randomString(int len) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            builder.append(PADDING_CHARS.charAt(random.nextInt(PADDING_CHARS.length() - 1)));
        }
        return builder.toString();
    }

    /**
     * 将密码补充成符合 AES-128 CBC 规范形式
     * @param password 明文密码
     * @return 补充后的密码
     */
    private static String getPaddingPassword(String password) {
        return randomString(64) + password;
    }
}