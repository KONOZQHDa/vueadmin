package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (SysMenu)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:01:03
 */
@Data
public class SysMenu implements Serializable{
    private static final long serialVersionUID = -28379073906652175L;

    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updated;

    private Integer state;

    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<SysMenu> children = new ArrayList<>();

}
