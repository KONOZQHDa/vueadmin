package com.example.vueadminjava2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vueadminjava2.entity.Order;

/**
 * (Order)表服务接口
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:33
 */
public interface OrderService extends IService<Order>{
    void checkOrder(String orderNumber);
}

