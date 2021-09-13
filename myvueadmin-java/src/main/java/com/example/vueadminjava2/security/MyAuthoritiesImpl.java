package com.example.vueadminjava2.security;

import cn.dev33.satoken.stp.StpInterface;
import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.service.impl.SysRoleServiceImpl;
import com.example.vueadminjava2.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description :完成sa-token的权限管理
 * @Author :kono淇杭哒、
 * @Create 2021-08-30 0:44
 */
@Component
public class MyAuthoritiesImpl implements StpInterface{
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysRoleServiceImpl sysRoleService;
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<String> list = sysUserService.getAuthoritiesById(loginId);
        System.out.println(list);
        return list;

    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<SysRole> roles = sysRoleService.getRolesByUserId(loginId);
        return roles.stream().map(role -> role.getName()).collect(Collectors.toList());
    }
}
