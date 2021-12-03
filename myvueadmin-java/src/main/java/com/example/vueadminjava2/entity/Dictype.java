package com.example.vueadminjava2.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * (Dictype)表实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-10 17:05:50
 */
@SuppressWarnings("serial")
@Data
public class Dictype{
    @TableId
    private String id;

    @TableField("`key`")
    private String dictypeKey;

    @TableField("remark")
    private String dictypeRemark;

    private Integer usfulStatus;

}

