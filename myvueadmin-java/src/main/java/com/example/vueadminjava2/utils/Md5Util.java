package com.example.vueadminjava2.utils;


import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-10-28 15:43
 */
public class Md5Util{
    public static String getMd5PasswordWithSalt(String password, String salt) {
        String pwdWithSalt = password + salt;
        return DigestUtils.md5DigestAsHex(pwdWithSalt.getBytes(StandardCharsets.UTF_8));
    }
}
