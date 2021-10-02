package com.example.vueadminjava2.commen.captcha;

import java.util.HashMap;

/**
 * @Description :保存验证码的key和code,冒充redis的功能,（2021.10.2）在学完redia后先已经改为使用reids
 * @Author :kono淇杭哒、
 * @Create 2021-08-28 16:47
 */
public class CaptchaStore{
    public static HashMap<String,String> captchasMap = new HashMap<>();

    public static void saveCaptcha(String key,String code){
        captchasMap.put(key,code);
    }

    public static void disableCaptcha(String key){
        captchasMap.remove(key);
    }

    public static boolean verifyCaptcha(String key,String code){
        return captchasMap.get(key).equals(captchasMap.get(key));
    }
}
