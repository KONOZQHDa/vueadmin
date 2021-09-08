package com.example.vueadminjava2.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (SysRoleMenu)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:48
 */
@Data
public class SysRoleMenu implements Serializable{
    private static final long serialVersionUID = 894864674650958158L;

    private Long id;

    private Long roleId;

    private Long menuId;



}
