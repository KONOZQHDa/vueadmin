package com.example.vueadminjava2.commen.Enum;

import lombok.Getter;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-09 15:00
 */
@Getter
public enum OrderStatusType{
    NOT_PAYED(0, "待支付"),
    PAYED(1, "已支付"),
    EXPIRED(2, "已过期"),
    CANCLED(3, "已取消");

    private final Integer code;
    private final String message;

    OrderStatusType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
