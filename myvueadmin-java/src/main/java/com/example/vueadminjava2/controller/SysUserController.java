package com.example.vueadminjava2.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.commen.result.UsersResultData;
import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.entity.SysUserRole;
import com.example.vueadminjava2.service.SysUserService;
import com.example.vueadminjava2.service.impl.SysRoleServiceImpl;
import com.example.vueadminjava2.service.impl.SysUserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysUser)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:02
 */
@RestController
@RequestMapping("sysUser")
public class SysUserController{
    /**
     * 服务对象
     */
    @Resource
    private SysUserService sysUserService;

    @Autowired
    SysRoleServiceImpl sysRoleService;

    @Autowired
    SysUserRoleServiceImpl sysUserRoleService;

    /**
     * 添加用户
     *
     * @param user
     * @return void
     */

    @SaCheckPermission("sys:user:save")
    @GetMapping("/addUser")
    public Result addUser(SysUser user) {
        Date date = new Date();
        user.setCreated(date);
        sysUserService.insert(user);
        return Result.success(200, "添加用户成功！", null);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return void
     */
    @GetMapping("/updateUser")
    @SaCheckPermission("sys:user:update")
    public Result updateUser(SysUser user){
        sysUserService.update(user);
        return Result.success(200,"编辑用户成功！",null);
    }

    @SaCheckPermission("sys:user:list")
    @GetMapping("/userList")
    public Result userList(){
        List<SysUser> users = sysUserService.queryAllUsers();
        for (SysUser user : users) {
            user.setRoles(sysRoleService.getRolesByUserId(user.getId()).stream().map(role -> role.getName()).collect(Collectors.toList()));
        }
        Integer totalCount = sysUserService.getTotalCount();
        UsersResultData usersResultData = UsersResultData.create(users, totalCount);
        return Result.success(usersResultData);
    }

    @SaCheckPermission("sys:user:delete")
    @GetMapping("/deleteUser")
    public Result deleteUser(@RequestParam("ids") List<Integer> idList){
        for (Integer id : idList) {
            sysUserService.deleteById(Long.valueOf(id));
        }
        return Result.success(200,"删除成功！",null);
    }

    @SaCheckPermission("sys:user:repass")
    @GetMapping("/resetPassword")
    public Result resetPassword(@RequestParam("id") Integer id){
        SysUser sysUser = sysUserService.queryById(Long.valueOf(id));
        sysUser.setPassword("123456");
        sysUserService.update(sysUser);
        return Result.success(200,"重置密码成功！",null);
    }

    @SaCheckPermission("sys:user:role")
    @GetMapping("/assignRole")
    public Result assignRole(@RequestParam("userId") Integer userId,@RequestParam("rolesId") List<Long> rolesId){
        List<SysRole> roles = sysRoleService.getRolesByUserId(Long.valueOf(userId));
        List<Long> roleIdList = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        if (Collections.disjoint(roleIdList, rolesId)) {
            for (Long roleId : rolesId) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(Long.valueOf(userId));
                sysUserRole.setRoleId(roleId);
                sysUserRoleService.insert(sysUserRole);
            }
            return Result.success(200,"添加角色成功！",null);
        }
        return Result.fail(400,"已拥有该角色，添加失败",null);
    }

    @GetMapping("searchUsers")
    public Result searchUsers(@RequestParam("word") String word){
        List<SysUser> sysUsers = sysUserService.searchUsers(word);
        UsersResultData usersResultData = UsersResultData.create(sysUsers, sysUsers.size());
        return Result.success(usersResultData);
    }

    @GetMapping("userInfo")
    public Result getUser() {
        long loginId = StpUtil.getLoginIdAsLong();
        SysUser sysUser = sysUserService.queryById(loginId);
        List<String> authorizations = sysUserService.getAuthoritiesById(loginId);
        sysUser.setAuthorizations(authorizations);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("username",sysUser.getUsername());
        dataMap.put("avatar",sysUser.getAvatar());
        dataMap.put("authorizations",sysUser.getAuthorizations());
        return Result.success(dataMap);
    }

    @SaCheckPermission("sys:user:role")
    @GetMapping("removeRole")
    public Result removeRole(@RequestParam("userId") Long userId,@RequestParam("rolesId") List<Long> rolesId){
        List<SysRole> rolesByUserId = sysRoleService.getRolesByUserId(userId);
        List<Long> userRoleIds = rolesByUserId.stream().map(role -> role.getId()).collect(Collectors.toList());
        if (userRoleIds.containsAll(rolesId)) {
            for (Long roleId : rolesId) {
                sysUserRoleService.deleteByUserIdAndRoleId(userId,roleId);
            }
            return Result.success(200,"删除成功！",null);
        }
        return Result.fail(400,"该用户未分配该角色，删除角色失败！",null);
    }
}
