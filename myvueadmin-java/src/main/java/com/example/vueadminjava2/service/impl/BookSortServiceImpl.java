package com.example.vueadminjava2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava2.dao.BookSortDao;
import com.example.vueadminjava2.entity.BookSort;
import com.example.vueadminjava2.service.BookSortService;
import org.springframework.stereotype.Service;

/**
 * (BookSort)表服务实现类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:25:35
 */
@Service("bookSortService")
public class BookSortServiceImpl extends ServiceImpl<BookSortDao, BookSort> implements BookSortService{

}

