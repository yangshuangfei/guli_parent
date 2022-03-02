package com.stitch.service.base.handle;

import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的异常处理
 *
 * @author wadh1
 * @ControllerAdvice是在类上声明的注解，其用法主要有三点： 1.结合方法型注解@ExceptionHandler，用于捕获Controller中抛出的指定类型的异常，从而达到不同类型的异常区别处理的目的。
 * 2.结合方法型注解@InitBinder，用于request中自定义参数解析方式进行注册，从而达到自定义指定格式参数的目的。
 * 3.结合方法型注解@ModelAttribute，表示其注解的方法将会在目标Controller方法执行之前执行。
 * @since 20201221
 */
@ControllerAdvice //作用是给Controller控制器添加统一的操作或处理
@Slf4j //添加日志记录器注解
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) //异常处理器  处理所有异常
    @ResponseBody
    public R error(Exception e) {
        log.error(ExceptionUtils.getMessage(e));
        return R.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e) {
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e) {
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(StitchException.class)
    @ResponseBody
    public R error(StitchException e) {
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
