package com.github.dingtalk.api.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回
 *
 * @author zyx
 * @date 2021-08-25 15:50:42
 */
@Setter
@Getter
public class ApiResponse<T> {

    /**
     * 是否成功
     */
    private boolean succeed;

    /**
     * code 码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回成功
     *
     * @param data 数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<T>();
        res.setSucceed(true);
        res.setCode(0);
        res.setData(data);
        return res;
    }

    /**
     * 返回失败
     *
     * @param msg 失败消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> fail(String msg) {
        ApiResponse<T> res = new ApiResponse<T>();
        res.setSucceed(false);
        res.setCode(500);
        res.setMsg(msg);
        return res;
    }
}
