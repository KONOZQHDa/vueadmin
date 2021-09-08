package com.example.vueadminjava2;

import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.impl.SysRoleServiceImpl;
import com.example.vueadminjava2.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VueadminJava2ApplicationTests{
    @Autowired
    SysUserServiceImpl sysUserService;
    @Autowired
    SysRoleServiceImpl sysRoleService;

    @Test
    void contextLoads() {
//        List<String> rolesByUserId = sysRoleService.getRolesNameByUserId("1");
      /*  List<SysUser> sysUsers = sysUserService.queryAllUsers();
        System.out.println(sysUsers);*/
//        System.out.println(sysUserService.getTotalCount());
    }

}
