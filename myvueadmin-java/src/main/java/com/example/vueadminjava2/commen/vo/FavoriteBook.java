package com.example.vueadminjava2.commen.vo;

import com.example.vueadminjava2.entity.Book;
import lombok.Data;

/**
 * @Description : 订单中数据VO对象
 * @Author :kono淇杭哒、
 * @Create 2021-12-06 22:38
 */
@Data
public class FavoriteBook{

    //收藏夹书的Id
    private String id;

    private String name;

    private Double price;

    private Double discountPrice;

    private String imageUrl;

    private Integer goodStatus;

    private Integer stock;

    private String sortId;

    private Double addedPrice;

    public void setAll(Book book, double addedPrice) {
        this.id = book.getId();
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
