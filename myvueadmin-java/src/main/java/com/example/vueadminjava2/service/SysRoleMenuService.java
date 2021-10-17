package com.example.vueadminjava2.service;

import com.example.vueadminjava2.entity.SysRoleMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysRoleMenu)表服务接口
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:48
 */
public interface SysRoleMenuService{

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRoleMenu queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysRoleMenu> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysRoleMenu 实例对象
     * @return 实例对象
     */
    SysRoleMenu insert(SysRoleMenu sysRoleMenu);

    /**
     * 修改数据
     *
     * @param sysRoleMenu 实例对象
     * @return 实例对象
     */
    SysRoleMenu update(SysRoleMenu sysRoleMenu);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    List<String> getMenusByRoleId(Long roleId);

    ArrayList<Integer> getMunusIdByRoleId(Long roleId);

    int deleteByRoleIdAndMenuId(Long roleId, Long menuId);

}
