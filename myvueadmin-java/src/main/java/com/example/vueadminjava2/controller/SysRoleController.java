package com.example.vueadminjava2.controller;

import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.entity.SysRoleMenu;
import com.example.vueadminjava2.service.SysRoleMenuService;
import com.example.vueadminjava2.service.SysRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private SysRoleMenuService sysRoleMenuService;


    @GetMapping("getRoles")
    public Result getUserRoles() {
        List<SysRole> roles = sysRoleService.getRoles();
//        List<String> roleNames = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        return Result.success(roles);
    }

    @GetMapping("updateRole")
    public Result updateRole(SysRole role) {
        sysRoleService.update(role);
        return Result.success(200, "更新角色成功！", null);
    }

    @GetMapping("addRole")
    public Result addRole(SysRole role) {
        sysRoleService.insert(role);
        return Result.success(200, "添加角色成功！", null);
    }

    @GetMapping("searchRole")
    public Result searchRole(String word) {
        List<SysRole> sysRoles = sysRoleService.searchRole(word);
        return Result.success(sysRoles);
    }

    @GetMapping("deleteRole")
    public Result deleteRole(@RequestParam("ids") List<Integer> idList) {
        for (Integer id : idList) {
            sysRoleService.deleteById(Long.valueOf(id));
        }
        return Result.success(200, "删除成功！", null);
    }

    @GetMapping("getPermissions")
    public Result getPermissions(Long roleId) {
        List<String> menusByRoleId = sysRoleMenuService.getMenusByRoleId(roleId);
        return Result.success(menusByRoleId);
    }

    @GetMapping("assignPermission")
    public Result assignPermission(Long roleId, @RequestParam("menuIds") ArrayList<Integer> menuIds) {
        ArrayList<Integer> hasMenusId = sysRoleMenuService.getMunusIdByRoleId(roleId);
        menuIds.removeAll(hasMenusId);
        for (Integer menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(Long.valueOf(menuId));
            sysRoleMenuService.insert(sysRoleMenu);
        }
        return Result.success(200, "分配权限成功！", null);
    }

    @GetMapping("deletePermission")
    public Result deletePermission(Long roleId, @RequestParam("menuIds") List<Integer> menuIds) {
        ArrayList<Integer> hasMenusId = sysRoleMenuService.getMunusIdByRoleId(roleId);
        menuIds.retainAll(hasMenusId);
        for (Integer menuId : menuIds) {
            sysRoleMenuService.deleteByRoleIdAndMenuId(roleId, Long.valueOf(menuId));
        }
        return Result.success(200, "收回权限成功！", null);
    }
}
