package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * (User)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-26 21:03:31
 */
@Data
public class User{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userName;

    private String password;

    private String nickName;

    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    private Date updated;

    private String phone;

    private String avatar;

    private Integer status;

    private String salt;
}

