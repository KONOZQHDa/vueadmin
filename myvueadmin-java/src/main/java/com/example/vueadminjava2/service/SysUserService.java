package com.example.vueadminjava2.service;

import com.example.vueadminjava2.entity.SysUser;

import java.util.List;

/**
 * (SysUser)表服务接口
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:02
 */
public interface SysUserService{

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryById(Long id);

    /**
     * 通过username查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    SysUser queryByUserNameAndPassword(String username, String password);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysUser> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 通过主键获取用户所拥有权限
     *
     * @param id 主键
     * @return
     */
    List<String> getAuthoritiesById(Object id);

    /*
     * 查询所有用户
     *
     * */
    List<SysUser> queryAllUsers();

    Integer getTotalCount();

    List<SysUser> searchUsers(String userName);

    SysUser getUserByUserName(String userName);

}
