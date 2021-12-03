package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (Sort)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 18:20:26
 */
@Data
@TableName("type")
public class Sort{

    private String id;

    @TableField("name")
    private String sortName;

    private Integer usful;

}

