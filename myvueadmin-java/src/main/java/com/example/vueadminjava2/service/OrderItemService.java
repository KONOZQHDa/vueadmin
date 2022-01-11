package com.example.vueadminjava2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vueadminjava2.entity.OrderItem;

/**
 * (OrderItem)表服务接口
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:45
 */
public interface OrderItemService extends IService<OrderItem>{
    /**
     * 释放超时未支付的订单中物品资源
     */
    void releaseExpiredOrder(String orderNumber);
}

