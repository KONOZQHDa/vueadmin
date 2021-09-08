package com.example.vueadminjava2.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.vueadminjava2.commen.captcha.Captcha;
import com.example.vueadminjava2.commen.captcha.CaptchaStore;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.impl.SysUserServiceImpl;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Description : 认证功能
 * @Author :kono淇杭哒、
 * @Create 2021-08-28 16:42
 */
@RestController
public class AuthController{
    @Autowired
    private Producer producer;
    @Autowired
    private SysUserServiceImpl userServiceImpl;

    /**
     * @methordName:
     * @description: 返回验证码图片，这里验证码信息的保存暂时用Capture类来，学了redis再用redis
     * @thingking:
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/8/28 16:53
     */
    @GetMapping("/captcha")
    public Result getCaptcha() {
        //生成该验证码的key
        String key = UUID.randomUUID().toString();
        //验证码图片的密码
        String code = producer.createText();
        //对应密码的验证码图片
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            return Result.fail(400, "生成验证码错误", null);
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String captchaImg = str + encoder.encode(outputStream.toByteArray());

        //保存验证码信息用于登录时对比认证
        CaptchaStore.saveCaptcha(key,code);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("captchaKey", key);
        hashMap.put("captchaImg", captchaImg);

        return Result.success(hashMap);
    }

    @PostMapping("/login")
    public Result login(@RequestBody SysUser user,Captcha captcha) {
        //判断验证码是否正确
        if(CaptchaStore.verifyCaptcha(captcha.getCaptchaKey(),captcha.getCaptcha())){
            //当前验证码作废
            CaptchaStore.disableCaptcha(captcha.getCaptchaKey());
            //判断用户名和密码是否合法,认证
            SysUser sysUser = userServiceImpl.queryByUserNameAndPassword(user.getUsername(),user.getPassword());
            if (sysUser != null) {
                //通过satoken设置为已登录，并生成得到对应的tokeninfo对象
                StpUtil.login(sysUser.getId());
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                //构造token
                HashMap<String, String> tokenMap = new HashMap<>();
                tokenMap.put("tokenName", tokenInfo.getTokenName());
                tokenMap.put("tokenValue", tokenInfo.getTokenValue());
                System.out.println(tokenInfo.getTokenName());
                System.out.println(tokenInfo.getTokenValue());
                //将token作为data返回给前端，前端要将其保存起来，然后每次请求时将其添加到请求头中带上
                return Result.success(tokenMap);
            }
            return Result.fail(400, "用户名或密码错误", null);
        }else {
            CaptchaStore.disableCaptcha(captcha.getCaptchaKey());
            return Result.fail(400,"验证码错误",null);
        }
    }


    @SaCheckLogin
    @GetMapping("/logout")
    public Result logout(){
        StpUtil.logout();
        return Result.success("登出成功");
    }
}
