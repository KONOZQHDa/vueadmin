package com.example.vueadminjava2;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-11 18:13
 */
@SpringBootTest
public class RedisTest{
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void pageBookRedisTest() {
        Page<Book> test = (Page<Book>) redisUtil.get("test");
        System.out.println(test.getRecords());
    }
}
