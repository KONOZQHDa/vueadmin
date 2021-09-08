package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.SysUser;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysUser)表数据库访问层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:02
 */
public interface SysUserDao{

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
     * @param password
     * @return 实例对象
     */
    SysUser queryByUserNameAndPassword(@Param("username") String username,@Param("password") String password);



    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysUser> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysUser 实例对象
     * @return 对象列表
     */
    List<SysUser> queryAll(SysUser sysUser);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    int insert(SysUser sysUser);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysUser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysUser> entities);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    int update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);
    /**
     * 通过主键查询用户所拥有权限
     *
     * @param id 主键
     * @return 权限数组
     */
    List<String> getAuthoritiesById(Object id);

//    查询所有用户
    List<SysUser> queryAllUsers();

//    查询所有用户的数量
    Integer  getTotalCount();

//    根据用户名模糊查询
    List<SysUser> fuzzyQueryByUserName(String userName);
}

