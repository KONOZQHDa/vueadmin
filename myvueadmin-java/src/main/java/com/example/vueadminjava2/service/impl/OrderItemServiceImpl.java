package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.OrderItemDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.OrderItem;
import com.example.vueadminjava2.service.OrderItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (OrderItem)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:45
 */
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItem> implements OrderItemService{
    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private BookDao bookDao;


    @Override
    public void releaseExpiredOrder(String orderNumber) {
        List<OrderItem> orderItems = orderItemDao.selectList(new QueryWrapper<OrderItem>().eq("order_number", orderNumber));
        List<String> bookIds = orderItems.stream().map(orderItem -> orderItem.getBookId()).collect(Collectors.toList());
        List<Book> books = bookDao.selectBatchIds(bookIds);
        for (int i = 0; i < orderItems.size(); i++) {
            Book tarBook = books.get(i);
            tarBook.setStock(tarBook.getStock() + orderItems.get(i).getNum());
        }
    }
}

