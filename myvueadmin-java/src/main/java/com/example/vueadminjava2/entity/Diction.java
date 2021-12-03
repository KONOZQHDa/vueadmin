package com.example.vueadminjava2.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * (Diction)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-10 17:05:00
 */
@SuppressWarnings("serial")
@Data
public class Diction{

    @TableId
    private String id;

    @TableField("`key`")
    private String dicKey;

    @TableField("value")
    private String dicValue;

    private String dictypeId;


}

