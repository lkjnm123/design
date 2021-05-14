package com.graduation.ecommerce.design.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PwdUtils {
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        // Create array for salt
        byte[] salt = new byte[16];
        // Get a random salt
        secureRandom.nextBytes(salt);
        // 将十进制数转换成十六进制(使用&运算，正数部分没变，负数部分二进制从右往左第9位及以上都为0
        StringBuilder builder = new StringBuilder();
        for (byte num : salt) {
            builder.append(Integer.toString((num & 0xff) + 0x100, 16).substring(1));
        }
        return builder.toString();
    }
    public static String getSHACode(String password, String salt, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // Get password mix salt，可以做多种组合，增加原始密码长度、复杂度
            password = salt + password + salt;
            // digest(byte[] input)使用指定的byte数组对摘要进行最后更新，然后完成摘要计算，此方法首先调用
            // update(input)，向update方法传递input数组，然后调用digest()。
            byte[] bytes = md.digest(password.getBytes()); // 如有中文，须添加字符集
            StringBuilder builder = new StringBuilder();
            for (byte num : bytes) {
                builder.append(Integer.toString((num & 0xff) + 0x100, 16).substring(1));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("algorithm wrong");
            return null;
        }
    }
}
