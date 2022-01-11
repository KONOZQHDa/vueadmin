package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-05 15:27
 */
@SpringBootTest
public class UserDaoTest{
    @Resource
    private UserDao userDao;

    @Test
    public void queryUserTest() {
        User user = userDao.selectById("1466673355699535874");
        System.out.println(user);
    }
}
