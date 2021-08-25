package com.github.dingtalk.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取jsapi_ticket
 *
 * @author zyx
 * @date 2021-08-24 22:04:45
 */
@Getter
@Setter
public class Ticket extends DingTalkResponse {

    /**
     * 生成的ticket
     */
    private String ticket;

    /**
     * access_token的过期时间，单位秒。
     */
    @JSONField(name = "expires_in")
    private Integer expires;

}
