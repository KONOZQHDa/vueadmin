package com.example.vueadminjava2.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (SysRole)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:33
 */
@Data
public class SysRole implements Serializable{
    private static final long serialVersionUID = -91479478506066057L;

    private Long id;

    private String name;

    private String code;
    /**
     * 备注
     */
    private String remark;

    private Date created;

    private Date updated;

    private Integer state;



}
