package com.example.vueadminjava2.controller;

import com.example.vueadminjava2.entity.SysMenu;
import com.example.vueadminjava2.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysMenu)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:01:07
 */
@RestController
@RequestMapping("sysMenu")
public class SysMenuController{
    /**
     * 服务对象
     */
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SysMenu selectOne(Long id) {
        return this.sysMenuService.queryById(id);
    }

}
