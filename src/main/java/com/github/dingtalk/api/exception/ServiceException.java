package com.github.dingtalk.api.exception;

import com.github.dingtalk.api.domain.ApiResponse;
import lombok.Getter;

/**
 * 统一异常
 *
 * @author zyx
 * @date 2021-08-25 16:35:35
 */
public class ServiceException extends RuntimeException {

    @Getter
    private final ApiResponse<String> response;

    public ServiceException(ApiResponse<String> response) {
        super(response.getMsg());
        this.response = response;
    }
}
