package com.example.vueadminjava2.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (SysMenu)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:01:03
 */
@Data
public class SysMenu implements Serializable{
    private static final long serialVersionUID = -28379073906652175L;

    private Long id;
    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    private String name;
    /**
     * 菜单URL
     */
    private String path;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    private String component;
    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer ordernum;

    private Date created;

    private Date updated;

    private Integer state;

}
