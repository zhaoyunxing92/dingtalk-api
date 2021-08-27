package com.github.dingtalk.api.advice;

import com.github.dingtalk.api.domain.ApiResponse;
import com.github.dingtalk.api.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 00:51
 */
@Slf4j
@RestControllerAdvice
public class DingTalkExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param ex 异常
     * @return res
     */
    @ExceptionHandler(ServiceException.class)
    public ApiResponse<String> exceptionHandler(ServiceException ex) {

        return ex.getResponse();
    }

    /**
     * 处理业务异常
     *
     * @param ex 异常
     * @return res
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> exceptionHandler(Exception ex) {

        return ApiResponse.fail(ex.getMessage());
    }
}
