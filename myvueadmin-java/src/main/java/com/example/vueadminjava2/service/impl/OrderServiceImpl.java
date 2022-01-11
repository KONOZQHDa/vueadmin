package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.commen.Enum.OrderStatusType;
import com.example.vueadminjava2.dao.OrderDao;
import com.example.vueadminjava2.entity.Order;
import com.example.vueadminjava2.service.OrderItemService;
import com.example.vueadminjava2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Order)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:33
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService{

    @Resource
    private OrderDao orderDao;
    @Autowired
    private OrderItemService orderItemService;

    /**
     * @description: 用于处理死信队列中的订单，判断订单是否已经支付，若未支付则进行库存释放等
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/9 22:35
     */
    @Override
    public void checkOrder(String orderNumber) {
        Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("order_number", orderNumber).select("pay_status"));
        //若订单仍未支付
        if (order.getPayStatus().equals(OrderStatusType.NOT_PAYED.getCode())) {
            //释放对应商品资源
            orderItemService.releaseExpiredOrder(orderNumber);
            //修改订单状态为已过期
            Order orderToUpdate = new Order();
            orderToUpdate.setPayStatus(OrderStatusType.EXPIRED.getCode());
            orderDao.update(orderToUpdate, new QueryWrapper<Order>().eq("order_number", orderNumber));
        }
    }
}

