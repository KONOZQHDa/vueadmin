package com.example.vueadminjava2.dao;

import com.example.vueadminjava2.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
}
