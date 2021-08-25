package com.github.dingtalk.api.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 过去企业token请求
 *
 * @author zyx
 * @date 2021-08-25 13:05:55
 */
@Getter
@Setter
@AllArgsConstructor
public class CorpTokenRequest {

    @JSONField(name = "auth_corpid")
    private String corpId;

}
