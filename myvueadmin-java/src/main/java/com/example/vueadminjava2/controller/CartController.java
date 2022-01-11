package com.example.vueadminjava2.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.commen.vo.CartItem;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.BookSortDao;
import com.example.vueadminjava2.dao.CartDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.BookSort;
import com.example.vueadminjava2.entity.Cart;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Cart)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:57:53
 */
@RestController
public class CartController{
    @Resource
    private CartDao cartDao;
    @Resource
    private BookDao bookDao;
    @Resource
    private BookSortDao bookSortDao;

    @PostMapping("saveCart")
    public Result saveCart(@RequestBody Cart cart, @RequestParam("isDiscount") boolean isDiscount) {
        String loginId = StpUtil.getLoginIdAsString();
        cart.setUserId(loginId);
        Book book = bookDao.selectById(cart.getBookId());
        if (isDiscount) {
            cart.setAddedPrice(book.getDiscountPrice());
        }
        else {
            cart.setAddedPrice(book.getPrice());
        }
        cartDao.insert(cart);
        return Result.successWithMessage("加入购物车成功！", null);
    }

    @GetMapping("getCart")
    public Result getCart() {
        String loginId = StpUtil.getLoginIdAsString();
        List<Cart> carts = cartDao.selectList(new QueryWrapper<Cart>().eq("user_id", loginId));
        ArrayList<CartItem> cartItems = new ArrayList<>();
        for (Cart cart : carts) {
            Book book = bookDao.selectById(cart.getBookId());
            QueryWrapper<BookSort> bookSortQueryWrapper = new QueryWrapper<>();
            bookSortQueryWrapper.eq("book_id", book.getId());
            BookSort bookSort = bookSortDao.selectOne(bookSortQueryWrapper);
            book.setSortId(bookSort.getSortId());
            CartItem cartItem = new CartItem();
            cartItem.setAll(cart.getId(), book, cart.getAddedPrice(), cart.getNum());
            cartItems.add(cartItem);
        }
        return Result.success(cartItems);
    }

    @DeleteMapping("removeCartItems")
    public Result removeCart(@RequestParam("ids") List<String> cartItemIds) {
        cartDao.deleteBatchIds(cartItemIds);
        return Result.success(null);
    }

    @PutMapping("updateCart")
    public Result updateCart(Cart cart) {
        cartDao.updateById(cart);
        return Result.success(null);
    }
}

