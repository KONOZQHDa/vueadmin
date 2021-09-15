package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.SysMenu;
import com.example.vueadminjava2.service.impl.SysMenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-09-10 22:52
 */
@SpringBootTest
class SysMenuDaoTest{
    @Autowired
    SysMenuServiceImpl sysMenuService;
    @Test
    void queryAllMenus() {
        List<SysMenu> sysMenus = sysMenuService.queryAllMenus();
        List<SysMenu> sysMenus1 = sysMenuService.treeMenus(sysMenus);
        sysMenus1.stream().forEach(System.out::println);
    }
}