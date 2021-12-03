package com.example.vueadminjava2.service.impl;

import com.example.vueadminjava2.dao.SysUserDao;
import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SysUser)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:02
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService{
    @Resource
    private SysUserDao sysUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryById(Long id) {
        return this.sysUserDao.queryById(id);
    }

    /**
     * 通过username查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    @Override
    public SysUser queryByUserNameAndPassword(String username, String password) {
        return this.sysUserDao.queryByUserNameAndPassword(username, password);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SysUser> queryAllByLimit(int offset, int limit) {
        return this.sysUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public SysUser insert(SysUser sysUser) {
        this.sysUserDao.insert(sysUser);
        return sysUser;
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public SysUser update(SysUser sysUser) {
        this.sysUserDao.update(sysUser);
        return this.queryById(sysUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.sysUserDao.deleteById(id) > 0;
    }

    @Override
    public List<String> getAuthoritiesById(Object id) {
        return sysUserDao.getAuthoritiesById(id);
    }

    @Override
    public List<SysUser> queryAllUsers() {
        return sysUserDao.queryAllUsers();
    }

    @Override
    public Integer getTotalCount() {
        return sysUserDao.getTotalCount();
    }

    @Override
    public List<SysUser> searchUsers(String userName) {
        return sysUserDao.fuzzyQueryByUserName(userName);
    }

    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserDao.queryByUsername(userName);
    }
}
