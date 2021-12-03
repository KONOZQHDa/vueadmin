package com.example.vueadminjava2.commen.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-08-27 20:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T>{
    //状态码
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result(200, "success", data);
    }

    public static <R> Result<R> success(int code, String message, R data) {
        return new Result(code, message, data);
    }

    public static <T> Result<T> fail(T data) {
        return new Result(400, "fail", data);
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result(code, message, data);
    }

    public static <T> Result<T> successWithMessage(String message, T data) {
        return new Result(200, message, data);
    }

    public static <T> Result<T> failWithMessage(String message, T data) {
        return new Result(400, message, data);
    }
}
