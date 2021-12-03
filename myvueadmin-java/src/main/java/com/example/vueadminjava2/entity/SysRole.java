package com.example.vueadminjava2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysRole)实体类
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-08-29 15:02:33
 */
@Data
public class SysRole implements Serializable{
    private static final long serialVersionUID = -91479478506066057L;

    private Long id;

    private String name;

    private String code;

    private Integer state;
    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updated;


}
