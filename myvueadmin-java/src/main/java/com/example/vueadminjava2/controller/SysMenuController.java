package com.example.vueadminjava2.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.entity.SysMenu;
import com.example.vueadminjava2.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("list")
    public Result list(){
        List<SysMenu> sysMenus = sysMenuService.queryAllMenus();
        List<SysMenu> treeMenus = sysMenuService.treeMenus(sysMenus);
        return Result.success(treeMenus);
    }

    @SaCheckPermission("sys:menu:update")
    @GetMapping("updateMenu")
    public Result update(SysMenu menu){
        System.out.println(menu);
        sysMenuService.update(menu);
        return Result.success(200,"更新成功！",null);
    }

    @SaCheckPermission("sys:menu:save")
    @GetMapping("addMenu")
    public Result addMenu(SysMenu menu){
        sysMenuService.insert(menu);
        return Result.success(200,"新增菜单成功！",null);
    }

    @SaCheckPermission("sys:menu:delete")
    @GetMapping("deleteMenu")
    public Result deleteMenu(@RequestParam("id") Long id){
        sysMenuService.deleteById(id);
        return Result.success(200,"删除成功",null);
    }

}
