package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.BookSortDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.BookSort;
import com.example.vueadminjava2.service.BookService;
import com.example.vueadminjava2.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-11 20:09
 */
@Service
public class BookServiceImpl implements BookService{
    @Resource
    private BookDao bookDao;

    @Resource
    private BookSortDao bookSortDao;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void initData() {
        new Thread(() -> runGetDiscountBooks()).start();
    }

    public void runGetDiscountBooks() {
        while (true) {
            List<Book> discountBooks = getDiscountBooks();
            // 删除原来的特价商品
            redisUtil.del("book:discount");
            // 把新查询到的特价商品添加到集合中
            redisUtil.lSetAll("book:discount", discountBooks);
            try {
                // 每隔一分钟执行一次更新特价商品
                Thread.sleep(1000 * 60);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Book> getDiscountBooks() {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        //通过判断比较当前时间和特价时间来找出所有特价书籍
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        LocalDateTime nowDateTime = LocalDateTime.now();
        nowDateTime.format(dateTimeFormatter);
        bookQueryWrapper.le("special_start_date_time", nowDateTime).ge("special_end_date_time", nowDateTime);
        List<Book> discountBookList = bookDao.selectList(bookQueryWrapper);
        //给查询到的书本注入书籍类型的ID
        for (Book discountBook : discountBookList) {
            QueryWrapper<BookSort> bookSortQueryWrapper1 = new QueryWrapper<>();
            bookSortQueryWrapper1.eq("book_id", discountBook.getId());
            BookSort bookSort = bookSortDao.selectOne(bookSortQueryWrapper1);
            discountBook.setSortId(bookSort.getSortId());
        }
        return discountBookList;
    }
}
