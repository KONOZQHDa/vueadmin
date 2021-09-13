package com.example.vueadminjava2.service;

import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.entity.SysUser;

import java.util.List;

/**
 * (SysRole)表服务接口
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:33
 */
public interface SysRoleService{

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRole queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole insert(SysRole sysRole);

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole update(SysRole sysRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 通过user_id查找用户拥有的角色
     *
     * @param userId user表主键
     * @return 角色集合
     */
    List<SysRole> getRolesByUserId(Object userId);

    List<SysRole> getRoles();

}
