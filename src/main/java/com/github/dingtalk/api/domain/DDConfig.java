package com.github.dingtalk.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * dd config
 *
 * @author zyx
 * @date 2021-08-25 15:12:43
 */
@Getter
@Setter
@Accessors(chain = true)
public class DDConfig {

    /**
     * 微应用ID
     */
    private Long agentId;

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 生成签名的时间戳
     */
    private Long timeStamp;

    /**
     * 自定义固定字符串。
     */
    private String nonce;

    /**
     * 签名
     */
    private String signature;

    /**
     * 0表示微应用的jsapi,1表示服务窗的jsapi；不填默认为0。该参数从dingtalk.js的0.8.3版本开始支持
     */
    private int type;

    /**
     * 需要使用的jsapi列表，注意：不要带dd。
     */
    private List<String> apis;
}
