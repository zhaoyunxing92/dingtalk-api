package com.github.dingtalk.api.domain.dingtalk;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉响应
 *
 * @author zyx
 * @date 2021-08-17 22:18:27
 */
@Getter
@Setter
public class DingTalkResponse {

    /**
     * 返回码描述
     */
    @JSONField(name = "errmsg")
    private String msg;

    /**
     * 返回码
     */
    @JSONField(name = "errcode")
    private Integer code;
}
