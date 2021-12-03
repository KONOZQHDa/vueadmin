package com.example.vueadminjava2.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.UserDao;
import com.example.vueadminjava2.entity.User;
import com.example.vueadminjava2.utils.Md5Util;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * (User)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-26 21:03:31
 */
@RestController
@RequestMapping("user")
public class UserController{
    @Resource
    private UserDao userDao;

    @PostMapping("register")
    public Result addUser(@RequestBody User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name", user.getUserName());
        //判断用户名是否可用
        User userWithTarUserName = userDao.selectOne(userQueryWrapper);
        if (userWithTarUserName != null) {
            return Result.failWithMessage("用户名已存在！", null);
        }
        //判断昵称是否可用
        userQueryWrapper.or().eq("nick_name", user.getNickName());
        User userWithTarNickName = userDao.selectOne(userQueryWrapper);
        if (userWithTarNickName != null) {
            return Result.failWithMessage("昵称已被占用！", null);
        }
        String salt = UUID.randomUUID().toString();
        String md5PasswordWithSalt = Md5Util.getMd5PasswordWithSalt(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(md5PasswordWithSalt);
        userDao.insert(user);
        return Result.success("注册成功！");
    }

    @PutMapping("updateInfo")
    public Result updateInfo(@RequestBody User user) {
        String loginId = StpUtil.getLoginIdAsString();
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", loginId);
        userDao.update(user, userUpdateWrapper);
        return Result.successWithMessage("更新信息成功！", null);
    }

    @GetMapping("userInfo")
    public Result userInfo() {
        String loginId = StpUtil.getLoginIdAsString();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", loginId).select("user_name", "nick_name", "phone", "avatar");
        User loginUser = userDao.selectOne(userQueryWrapper);
        return Result.success(loginUser);
    }

    @GetMapping("getAvatar")
    public Result getAvatar(String userName) {
        if (userName != null && userName != "") {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("user_name", userName).select("avatar");
            User user = userDao.selectOne(userQueryWrapper);
            return user == null ? Result.success(null) : Result.success(user.getAvatar());
        }
        return Result.success(null);
    }

    /**
     * 修改密码前需要输入当前密码，验证当前密码是否正确
     */
    @PutMapping("changePassword")
    public Result changePassword(@RequestBody Map<String, String> passwordMap) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        String loginId = StpUtil.getLoginIdAsString();
        userQueryWrapper.eq("id", loginId);
        User loginUser = userDao.selectOne(userQueryWrapper);
        if (Md5Util.getMd5PasswordWithSalt(passwordMap.get("oldPassword"), loginUser.getSalt()).equals(loginUser.getPassword())) {
            String newPassword = Md5Util.getMd5PasswordWithSalt(passwordMap.get("newPassword"), loginUser.getSalt());
            User user = new User();
            user.setId(loginId);
            user.setPassword(newPassword);
            userDao.updateById(user);
            return Result.successWithMessage("修改密码成功,请重新登录", null);
        }
        return Result.failWithMessage("原密码错误！", null);
    }
}

