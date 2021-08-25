package com.github.dingtalk.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-08-17 22:15:43
 */
@Getter
@Setter
public class AccessToken extends DingTalkResponse {

    /**
     * 生成的access_token
     */
    @JSONField(name = "access_token")
    private String token;

    /**
     * access_token的过期时间，单位秒。
     */
    @JSONField(name = "expires_in")
    private Integer expires;

}
