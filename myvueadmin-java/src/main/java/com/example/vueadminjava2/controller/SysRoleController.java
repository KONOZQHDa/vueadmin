package com.example.vueadminjava2.controller;

import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.commen.result.RolesResultData;
import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysRole)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:34
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController{
    /**
     * 服务对象
     */
    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("getRoles")
    public Result getUserRoles(){
        List<SysRole> roles = sysRoleService.getRoles();
//        List<String> roleNames = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        return Result.success(roles);
    }



}
