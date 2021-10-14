package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-09-15 19:31
 */
@SpringBootTest
class SysUserDaoTest{
    @Autowired
    SysUserServiceImpl sysUserService;

    @Autowired
    SysUserDao sysUserDao;

    @Test
    void queryById() {
        SysUser sysUser = sysUserDao.queryById(Long.valueOf(6));
        System.out.println(sysUser);
    }
}