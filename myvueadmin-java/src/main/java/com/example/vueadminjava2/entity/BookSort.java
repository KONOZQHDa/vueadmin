package com.example.vueadminjava2.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (BookSort)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:25:35
 */
@SuppressWarnings("serial")
@Data
@TableName("book_type")
public class BookSort{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //外键
    private String bookId;
    //外键
    private String sortId;

}

