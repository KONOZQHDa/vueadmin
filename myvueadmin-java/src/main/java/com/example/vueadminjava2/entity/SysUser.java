package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (SysUser)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:03:02
 */
@Data
public class SysUser implements Serializable{
    private static final long serialVersionUID = -31874235235625497L;

    private Long id;

    private String username;

    private String password;

    private String avatar;

    private String email;

    private Integer state;

    private String city;

    private String salt;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastLogin;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updated;

    @TableField(exist = false)
    private List<String> roles;

    @TableField(exist = false)
    private List<String> authorizations;


}
