package com.trafficinfosystem.demo.handler;

import com.mongodb.DuplicateKeyException;
import com.trafficinfosystem.demo.constant.MessageConstant;
import com.trafficinfosystem.demo.exception.BaseException;
import com.trafficinfosystem.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常 (Global exception handler, handles business exceptions thrown in the project)
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常 (Catching business exceptions)
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("Exception information：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * Handle MongoDB duplicate key exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("Exception information：{}", ex.getMessage());
        // 解析错误信息来获取是哪个字段导致的重复
        if (ex.getMessage().contains("index: username")) {
            String msg = "用户名已存在";
            return Result.error(msg);
        } else {
            return Result.error("已存在重复的记录");
        }
    }
}
