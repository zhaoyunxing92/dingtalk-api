package com.github.dingtalk.api.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-08-24 22:20:37
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "dingtalk")
public class DingTalkConfig {

    /**
     * app id
     */
    private Long id;

    /**
     * 企业的corpid。ISV开发的应用后台免登用的access_token，需使用ISV自己的corpId和SSOsecret来换取，不是管理员所在的企业的。
     */
    private String corpId;

    /**
     * 在开发者后台的基本信息 > 开发信息（旧版）页面获取微应用管理后台SSOSecret。
     */
    private String corpSecret;

    /**
     * 小程序key
     */
    private String key;

    /**
     * 小程序秘钥,如果是第三方企业
     */
    private String secret;

    /**
     * 钉钉推送的suiteTicket。
     */
    private String ticket;

    /**
     * 需要使用的jsapi列表，注意：不要带dd。
     */
    private List<String> apis;

    /**
     * 是否isv调用
     */
    private boolean isv;
}
