package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (Order)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:33
 */
@SuppressWarnings("serial")
@Data
@TableName("`order`")
public class Order extends Model<Order>{
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;
    //订单编号
    private String orderNumber;
    //订单总价
    private String totalPrice;
    //收货地址
    private String address;

    private String telephone;
    //支付状态,0为未支付，1未已支付
    private Integer payStatus;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private Date created;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

