package com.example.vueadminjava2.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueadminjava2.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-11-06 10:21
 */
@SpringBootTest
public class BookDaoTest{
    @Resource
    private BookDao bookDao;

    @Test
    public void addBook() {
        Book book = new Book();
        book.setName("三体");
        book.setPrice(100.0);
        bookDao.insert(book);
    }

    @Test
    public void getDiscountBooks() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        LocalDateTime nowDateTime = LocalDateTime.now();
        nowDateTime.format(dateTimeFormatter);
        queryWrapper.le("special_start_date_time", nowDateTime).ge("special_end_date_time", nowDateTime);
        List<Book> discountBooks = bookDao.selectList(queryWrapper);
        System.out.println(discountBooks);
    }
}
