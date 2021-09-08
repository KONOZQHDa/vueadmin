package com.example.vueadminjava2.controller;

import com.example.vueadminjava2.entity.SysUserRole;
import com.example.vueadminjava2.service.SysUserRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysUserRole)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:20
 */
@RestController
@RequestMapping("sysUserRole")
public class SysUserRoleController{
    /**
     * 服务对象
     */
    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SysUserRole selectOne(Long id) {
        return this.sysUserRoleService.queryById(id);
    }

}
