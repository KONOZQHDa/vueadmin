package com.example.vueadminjava2.service.impl;

import com.example.vueadminjava2.entity.SysRole;
import com.example.vueadminjava2.dao.SysRoleDao;
import com.example.vueadminjava2.entity.SysUser;
import com.example.vueadminjava2.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SysRole)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:33
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService{
    @Resource
    private SysRoleDao sysRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysRole queryById(Long id) {
        return this.sysRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SysRole> queryAllByLimit(int offset, int limit) {
        return this.sysRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public SysRole insert(SysRole sysRole) {
        this.sysRoleDao.insert(sysRole);
        return sysRole;
    }

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public SysRole update(SysRole sysRole) {
        this.sysRoleDao.update(sysRole);
        return this.queryById(sysRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.sysRoleDao.deleteById(id) > 0;
    }

    /**
     * 通过user_id查找用户拥有的角色
     *
     * @param userId user表主键
     * @return 角色集合
     */
    @Override
    public List<String> getRolesNameByUserId(Object userId) {
        return sysRoleDao.getRolesNameByUserId(userId);
    }

    @Override
    public List<SysRole> getRoles() {
        return sysRoleDao.getRoles();
    }

}

