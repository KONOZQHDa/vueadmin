package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (OrderItem)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:45
 */
@SuppressWarnings("serial")
@Data
public class OrderItem extends Model<OrderItem>{
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //商品所属订单的订单编号
    private String orderNumber;

    private String bookId;
    //购买数量
    private Integer num;
    //商品单价
    private Double price;

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

