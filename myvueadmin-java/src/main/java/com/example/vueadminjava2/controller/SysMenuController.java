package com.example.vueadminjava2.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.SysMenuDao;
import com.example.vueadminjava2.dao.SysRoleMenuDao;
import com.example.vueadminjava2.dao.SysUserDao;
import com.example.vueadminjava2.entity.SysMenu;
import com.example.vueadminjava2.entity.SysRoleMenu;
import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.SysMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private SysMenuDao sysMenuDao;

    @Resource
    private SysRoleMenuDao sysRoleMenuDao;

    @Resource
    private SysUserDao sysUserDao;

    /**
     * 获取所有菜单数据
     */
    @GetMapping("list")
    public Result list() {
        List<SysMenu> sysMenus = sysMenuService.queryAllMenus();
        List<SysMenu> treeMenus = sysMenuService.treeMenus(sysMenus);
        return Result.success(treeMenus);
    }

    /**
     * 根据用户ID获取其权限所拥有的所有菜单
     */
    @GetMapping("getUserMenus")
    public Result listByPerms() {
        long loginId = StpUtil.getLoginIdAsLong();
        //判断是否是获取后台用户的菜单
        SysUser sysUser = sysUserDao.queryById(loginId);
        if (sysUser != null) {
            List<SysMenu> sysMenus = sysMenuService.queryMenusByUserId(loginId);
            List<SysMenu> treeMenus = sysMenuService.treeMenus(sysMenus);
            return Result.success(treeMenus);
        }
        //放回普通用户的菜单
        else {
            QueryWrapper<SysRoleMenu> sysRoleMenuQueryWrapper = new QueryWrapper<>();
            sysRoleMenuQueryWrapper.eq("role_id", 3);
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuDao.selectList(sysRoleMenuQueryWrapper);
            List<Long> normalUserMenusId = sysRoleMenus.stream().map(rolemenu -> rolemenu.getMenuId()).collect(Collectors.toList());
            QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
            sysMenuQueryWrapper.in("id", normalUserMenusId);
            List<SysMenu> normalUserMenus = sysMenuDao.selectList(sysMenuQueryWrapper);
            List<SysMenu> normalUserTreeMenus = sysMenuService.treeMenus(normalUserMenus);
            return Result.success(normalUserTreeMenus);
        }
    }


    @SaCheckPermission("sys:menu:update")
    @GetMapping("updateMenu")
    public Result update(SysMenu menu) {
        System.out.println(menu);
        sysMenuService.update(menu);
        return Result.success(200, "更新成功！", null);
    }

    @SaCheckPermission("sys:menu:save")
    @GetMapping("addMenu")
    public Result addMenu(SysMenu menu) {
        sysMenuService.insert(menu);
        return Result.success(200, "新增菜单成功！", null);
    }

    @SaCheckPermission("sys:menu:delete")
    @GetMapping("deleteMenu")
    public Result deleteMenu(@RequestParam("id") Long id) {
        sysMenuService.deleteById(id);
        return Result.success(200, "删除成功", null);
    }


}
