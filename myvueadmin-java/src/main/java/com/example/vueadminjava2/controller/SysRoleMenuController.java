package com.example.vueadminjava2.controller;

import com.example.vueadminjava2.entity.SysRoleMenu;
import com.example.vueadminjava2.service.SysRoleMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysRoleMenu)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:49
 */
@RestController
@RequestMapping("sysRoleMenu")
public class SysRoleMenuController{
    /**
     * 服务对象
     */
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SysRoleMenu selectOne(Long id) {
        return this.sysRoleMenuService.queryById(id);
    }

}
