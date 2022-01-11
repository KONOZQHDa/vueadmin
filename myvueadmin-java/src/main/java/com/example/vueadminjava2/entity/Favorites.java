package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * (Collection)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:14
 */
@SuppressWarnings("serial")
@Data
public class Favorites{
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String bookId;
    //加入收藏夹时的书籍价格
    private Double addedPrice;
    //是否可用，如果书籍下架了则不可用
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date created;


}

