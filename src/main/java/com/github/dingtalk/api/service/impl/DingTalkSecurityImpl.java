package com.github.dingtalk.api.service.impl;

import com.github.dingtalk.api.service.DingTalkSecurity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 安全类全部负责加密
 *
 * @author zyx
 * @date 2021-08-25 11:03:26
 */
@Service
public class DingTalkSecurityImpl implements DingTalkSecurity {

    /**
     * 计算dd.config的签名参数
     *
     * @param ticket    通过微应用appKey获取的jsticket
     * @param nonce     自定义固定字符串
     * @param timeStamp 当前时间戳
     * @param url       调用dd.config的当前页面URL
     * @return 签名
     * @throws Exception ex
     */
    public String signature(String ticket, String nonce, long timeStamp, String url) throws Exception {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonce + "&timestamp=" + timeStamp
                + "&url=" + decodeUrl(url);
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
            sha1.reset();
            sha1.update(plain.getBytes(StandardCharsets.UTF_8));
            return byteToHex(sha1.digest());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
