package com.example.vueadminjava2.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.Enum.OrderStatusType;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.commen.vo.CartItem;
import com.example.vueadminjava2.commen.vo.OrderItemVo;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.OrderDao;
import com.example.vueadminjava2.dao.OrderItemDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.Order;
import com.example.vueadminjava2.entity.OrderItem;
import com.example.vueadminjava2.rabbitmq.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * (Order)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:32
 */
@RestController
@RequestMapping("order")
public class OrderController{
    @Autowired
    private Publisher publisher;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;


    @Resource
    private BookDao bookDao;

    /**
     * @description: 获取用户的所有订单
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/15 20:49
     */
    @GetMapping("userOrders")
    public Result userOrders() {
        List<Order> userOrders = orderDao.selectList(new QueryWrapper<Order>().eq("user_id", StpUtil.getLoginIdAsString()));
        return Result.success(userOrders);
    }


    /**
     * @description: 生成订单
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/15 20:58
     */
    @PostMapping("addOrder")
    public Result addOrder(@RequestBody ArrayList<CartItem> cartItems, Order order) {
        order.setUserId(StpUtil.getLoginIdAsString());
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPayStatus(OrderStatusType.NOT_PAYED.getCode());
        orderDao.insert(order);
        //给新生成的订单插入相应的订单物品，并减少商品库存
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNumber(order.getOrderNumber());
            orderItem.setBookId(cartItem.getBookId());
            orderItem.setNum(cartItem.getNum());
            orderItem.setPrice(cartItem.getPresentPrice());
            orderItemDao.insert(orderItem);
            //调整订单中相应商品的库存
            Book book = bookDao.selectById(cartItem.getBookId());
            book.setStock(book.getStock() - cartItem.getNum());
            bookDao.updateById(book);
        }
        //发送该订单的延时消息队列，30分钟后判断订单是否已经支付
        publisher.publishDelayedMessage("order:" + order.getOrderNumber());
        return Result.success(order.getOrderNumber());
    }

    /**
     * @description: 支付订单
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/15 20:58
     */
    @PutMapping("payOrder")
    public Result payOrder(@RequestParam("orderNumber") String orderNumber) {
        Order orderToUpdate = new Order();
        orderToUpdate.setPayStatus(OrderStatusType.PAYED.getCode());
        orderDao.update(orderToUpdate, new QueryWrapper<Order>().eq("order_number", orderNumber));
        return Result.successWithMessage("支付成功！", null);
    }

    /**
     * @description: 取消订单
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/15 20:58
     */
    @PutMapping("cancelOrder")
    public Result cancelOrder(@RequestParam("orderNumber") String orderNumber) {
        Order orderToUpdate = new Order();
        orderToUpdate.setPayStatus(OrderStatusType.CANCLED.getCode());
        orderDao.update(orderToUpdate, new QueryWrapper<Order>().eq("order_number", orderNumber));
        return Result.successWithMessage("取消订单成功！", null);
    }

    /**
     * @description: 根据订单编号返回该订单的订单项集合
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/10 21:18
     */
    @GetMapping("orderDetails")
    public Result orderDetails(@RequestParam("orderNumber") String orderNumber) {
        List<OrderItem> orderItems = orderItemDao.selectList(new QueryWrapper<OrderItem>().eq("order_number", orderNumber));
        List<String> bookIds = orderItems.stream().map(orderItem -> orderItem.getBookId()).collect(Collectors.toList());
        List<Book> books = bookDao.selectBatchIds(bookIds);
        ArrayList<OrderItemVo> orderItemVos = new ArrayList<>();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItemVo orderItemVo = new OrderItemVo();
            orderItemVo.setAll(books.get(i), orderItems.get(i));
            orderItemVos.add(orderItemVo);
        }
        return Result.success(orderItemVos);
    }

    /**
     * @description: 根据订单编号返回订单的信息
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/10 21:20
     */
    @GetMapping("orderInfo")
    public Result orderInfo(@RequestParam("orderNumber") String orderNumber) {
        Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("order_number", orderNumber));
        return Result.success(order);
    }
}

