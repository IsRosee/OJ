package com.yz.oj.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltUtil {

    private static final int SALT_LENGTH = 16; // 盐的长度（字节数）

    public static String generateSalt() {
        // 创建一个强随机数生成器
        SecureRandom sr = new SecureRandom();

        // 生成指定长度的盐
        byte[] salt = new byte[SALT_LENGTH];
        sr.nextBytes(salt);

        // 将盐编码为Base64字符串
        return Base64.getEncoder().encodeToString(salt);
    }
}

