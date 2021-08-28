package com.github.dingtalk.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钉钉事件回调返回加密体
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:38
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkEventEncrypt {

    /**
     * 签名
     */
    @JSONField(name = "msg_signature")
    private String signature;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 加密字符串
     */
    private String encrypt;

    /**
     * 随机字符串
     */
    private String nonce;
}
