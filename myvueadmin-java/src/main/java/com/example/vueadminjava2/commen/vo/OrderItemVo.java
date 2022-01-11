package com.example.vueadminjava2.commen.vo;

import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.OrderItem;
import lombok.Data;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-08 23:44
 */
@Data
public class OrderItemVo{
    private String id;

    private Integer num;

    private Double price;

    private String bookId;

    private String name;

    private String imageUrl;

    public void setAll(Book book, OrderItem orderItem) {
        this.bookId = book.getId();
        this.name = book.getName();
        this.imageUrl = book.getImageUrl();
        this.id = orderItem.getId();
        this.num = orderItem.getNum();
        this.price = orderItem.getPrice();
    }
}
