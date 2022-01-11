package com.example.vueadminjava2.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.commen.vo.FavoriteBook;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.BookSortDao;
import com.example.vueadminjava2.dao.FavoritesDao;
import com.example.vueadminjava2.dao.SortDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.BookSort;
import com.example.vueadminjava2.entity.Favorites;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Collection)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-12-05 20:58:14
 */
@RestController
public class FavoritesController{
    @Resource
    private FavoritesDao favoritesDao;

    @Resource
    private BookDao bookDao;

    @Resource
    private BookSortDao bookSortDao;

    @Resource
    private SortDao sortDao;


    /**
     * @description: 收藏书籍接口
     * @parameters:
     * @return:
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/6 12:28
     */
    @PostMapping("saveFavorites")
    public Result addFavorites(@RequestBody Favorites favorite, @RequestParam("isDiscount") boolean isDiscount) {
        //通过SATOKEN的工具类获取发送请求的用户Id
        String loginId = StpUtil.getLoginIdAsString();
        favorite.setUserId(loginId);
        Book book = bookDao.selectById(favorite.getBookId());
        if (isDiscount) {
            favorite.setAddedPrice(book.getDiscountPrice());
        }
        else {
            favorite.setAddedPrice(book.getPrice());
        }
        favoritesDao.insert(favorite);
        return Result.success(null);
    }


    /**
     * @description: 获取用户收藏夹中的所有数据
     * @parameters:
     * @return: ArrayList<FavoriteBook> 收藏夹数据VO对象的集合
     * @author : kono曾淇杭哒！
     * @creat: 2021/12/6 12:28
     */
    @GetMapping("getFavorites")
    public Result getFavorites() {
        String loginId = StpUtil.getLoginIdAsString();
        QueryWrapper<Favorites> favoritesQueryWrapper = new QueryWrapper<>();
        favoritesQueryWrapper.eq("user_id", loginId);
        List<Favorites> favorites = favoritesDao.selectList(favoritesQueryWrapper);
        ArrayList<FavoriteBook> favoriteBooks = new ArrayList<>();
        for (Favorites favorite : favorites) {
            Book book = bookDao.selectById(favorite.getBookId());
            QueryWrapper<BookSort> bookSortQueryWrapper = new QueryWrapper<>();
            bookSortQueryWrapper.eq("book_id", book.getId());
            BookSort bookSort = bookSortDao.selectOne(bookSortQueryWrapper);
            book.setSortId(bookSort.getSortId());
            FavoriteBook favoriteBook = new FavoriteBook();
            favoriteBook.setAll(book, favorite.getAddedPrice());
            favoriteBooks.add(favoriteBook);

        }
        return Result.success(favoriteBooks);
    }

    @DeleteMapping("removeFavorite")
    public Result removeFavorite(@RequestParam("bookId") String bookId) {
        String loginId = StpUtil.getLoginIdAsString();
        QueryWrapper<Favorites> favoritesQueryWrapper = new QueryWrapper<>();
        favoritesQueryWrapper.eq("book_id", bookId);
        favoritesQueryWrapper.eq("user_id", loginId);
        favoritesDao.delete(favoritesQueryWrapper);
        return Result.success(null);
    }

}



