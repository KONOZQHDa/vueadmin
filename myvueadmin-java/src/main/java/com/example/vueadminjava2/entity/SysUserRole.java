package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysUserRole)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:19
 */
@Data
public class SysUserRole implements Serializable{
    private static final long serialVersionUID = -82046529540725360L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long roleId;

}
