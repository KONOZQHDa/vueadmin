package com.example.vueadminjava2.service.impl;

import com.example.vueadminjava2.dao.SysMenuDao;
import com.example.vueadminjava2.entity.SysMenu;
import com.example.vueadminjava2.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (SysMenu)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:01:06
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService{
    @Resource
    private SysMenuDao sysMenuDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysMenu queryById(Long id) {
        return this.sysMenuDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SysMenu> queryAllByLimit(int offset, int limit) {
        return this.sysMenuDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SysMenu insert(SysMenu sysMenu) {
        this.sysMenuDao.insert(sysMenu);
        return sysMenu;
    }

    /**
     * 修改数据
     *
     * @param sysMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SysMenu update(SysMenu sysMenu) {
        sysMenuDao.updateById(sysMenu);
        return this.queryById(sysMenu.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.sysMenuDao.deleteById(id) > 0;
    }


    @Override
    public List<SysMenu> queryAllMenus() {
        return sysMenuDao.queryAllMenus();
    }

    @Override
    public List<SysMenu> treeMenus(List<SysMenu> menus) {
        List<SysMenu> finalMenu = new ArrayList<>();
        for (SysMenu menu : menus) {
            for (SysMenu childMenu : menus) {
                if (childMenu.getParentId().equals(menu.getId())) {
                    menu.getChildren().add(childMenu);
                }
            }
            if (menu.getParentId() == 0L) {
                finalMenu.add(menu);
            }
        }
        return finalMenu;
    }

    @Override
    public List<SysMenu> queryMenusByUserId(Long userId) {
        return sysMenuDao.queryMenusByUserId(userId);
    }
}
