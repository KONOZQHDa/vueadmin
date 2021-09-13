package com.example.vueadminjava2.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.io.Serializable;
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

    private String city;

    private Date created;

    private Date updated;

    private Date lastLogin;

    private Integer state;

    private List<String> roles;

    private List<String> authorizations;

}
