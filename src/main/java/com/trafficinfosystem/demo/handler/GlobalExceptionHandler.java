package com.trafficinfosystem.demo.handler;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.trafficinfosystem.demo.constant.MessageConstant;
import com.trafficinfosystem.demo.exception.BaseException;
import com.trafficinfosystem.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @ExceptionHandler(MongoWriteException.class)
    public Result handleDuplicateKeyException(MongoWriteException ex) {
        log.error("Exception information：{}", ex.getMessage());
        // 解析错误信息来获取是哪个字段导致的重复

        WriteError writeError = ex.getError();
        if (writeError.getCode() == 11000 && ex.getMessage().contains("dup key: { username: ")) {
            Pattern pattern = Pattern.compile("dup key: \\{ username: \"(.*?)\"");
            Matcher matcher = pattern.matcher(ex.getMessage());

            if (matcher.find()) {
                String username = matcher.group(1);
                String msg = username + " " + MessageConstant.ALREADY_EXISTS;
                return Result.error(msg);
            }

            String msg = MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
