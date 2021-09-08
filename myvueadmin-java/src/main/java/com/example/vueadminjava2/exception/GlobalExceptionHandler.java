package com.example.vueadminjava2.exception;
import cn.dev33.satoken.exception.NotLoginException;
import com.example.vueadminjava2.commen.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-08-27 22:28
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result handler(RuntimeException e){
        log.error("运行时异常：----------------{}", e);
        return Result.fail(400,e.getMessage(),null);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public Result handler(NotLoginException e){
        return Result.fail(401,e.getMessage(),null);
    }
}
