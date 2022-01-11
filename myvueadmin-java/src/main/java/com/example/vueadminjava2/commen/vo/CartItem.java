package com.example.vueadminjava2.commen.vo;

import com.example.vueadminjava2.entity.Book;
import lombok.Data;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-08 10:14
 */
@Data
public class CartItem{
    private String id;

    private Double addedPrice;

    private Integer num;

    //当前价格
    private Double presentPrice;

    //购车项书籍信息

    private String bookId;

    private String name;

    private Double price;

    private Double discountPrice;


    private String imageUrl;

    private Integer goodStatus;

    private Integer stock;

    private String sortId;

    public void setAll(String cartId, Book book, double addedPrice, Integer num) {
        this.id = cartId;
        this.bookId = book.getId();
        this.num = num;
        this.discountPrice = book.getDiscountPrice();
        this.name = book.getName();
        this.price = book.getPrice();
        this.imageUrl = book.getImageUrl();
        this.goodStatus = book.getGoodStatus();
        this.stock = book.getStock();
        this.sortId = book.getSortId();
        this.addedPrice = addedPrice;
    }
}
