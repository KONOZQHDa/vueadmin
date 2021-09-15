package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.service.impl.SysRoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-09-11 9:57
 */
@SpringBootTest
class SysRoleDaoTest{
   @Autowired
    SysRoleServiceImpl sysRoleService;
    @Test
    void getRolesByUserId() {
        List<SysRole> rolesByUserId = sysRoleService.getRolesByUserId(1L);
        rolesByUserId.stream().forEach(System.out::println);
    }
}