package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-09-27 20:33
 */
@SpringBootTest
public class RedisTest{
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void jedisTest() {
        redisUtil.set("name", "zqh");
        System.out.println(redisUtil.get("name"));
    }
}
