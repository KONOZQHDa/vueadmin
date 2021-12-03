package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (Book)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 10:03:36
 */
@Data
public class Book implements Serializable{
    private static final long serialVersionUID = 506928031917389484L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private Double price;

    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    private Date updated;

    private String imageUrl;

    private Integer goodStatus;

    private Double discountPrice;

    private Integer stock;

    private String creater;

    @TableField(exist = false)
    private String sortId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date specialStartDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date specialEndDateTime;

}

