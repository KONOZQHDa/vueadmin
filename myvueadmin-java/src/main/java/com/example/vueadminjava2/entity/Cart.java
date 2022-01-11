package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * (Cart)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:57:53
 */
@SuppressWarnings("serial")
@Data
public class Cart extends Model<Cart>{
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String bookId;
    //数量
    private Integer num;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date updated;
    //加入购物车时书籍的价格
    private Double addedPrice;
    //是否可用，如书籍下架后则不可用
    private Integer status;
}



