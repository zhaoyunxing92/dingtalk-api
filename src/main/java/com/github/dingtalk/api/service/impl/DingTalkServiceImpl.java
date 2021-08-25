package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.dingtalk.api.domain.*;
import com.github.dingtalk.api.request.CorpTokenRequest;
import com.github.dingtalk.api.service.DingTalkSecurity;
import com.github.dingtalk.api.service.DingTalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-08-24 21:56:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingTalkServiceImpl implements DingTalkService {

    private final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");

    private final DingTalkConfig dingTalkConfig;

    private final DingTalkSecurity dingTalkSecurity;

    /**
     * 获取企业内部token
     *
     * <a href=https://developers.dingtalk.com/document/app/obtain-orgapp-token>获取企业内部应用的access_token</a>
     *
     * @return token
     */
    @Override
    public String getToken() {
        try {
            HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getToken)).newBuilder();
            url.addEncodedQueryParameter("appkey", dingTalkConfig.getKey());
            url.addEncodedQueryParameter("appsecret", dingTalkConfig.getSecret());
            Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().maxAge(7200, TimeUnit.SECONDS).build())
                    .url(url.build())
                    .get()
                    .build();

            String json = execute(request);
            AccessToken accessToken = JSON.parseObject(json, AccessToken.class);
            Assert.isTrue(accessToken.getCode().equals(0), accessToken.getMsg());
            return accessToken.getToken();
        } catch (IOException e) {
            log.error("get token err:", e);
            return "";
        }
    }

    /**
     * 获取第三方应用授权企业的access_token
     *
     * <a href=https://developers.dingtalk.com/document/app/obtains-the-enterprise-authorized-credential>获取企业内部应用的access_token</a>
     *
     * @param corpId 企业id
     * @return token
     */
    @Override
    public String getCorpToken(String corpId) {
        try {

            Assert.hasLength(corpId, "corpId不能为空");

            long timestamp = System.currentTimeMillis();

            String ticket = dingTalkConfig.getTicket();
            String secret = dingTalkConfig.getSecret();

            String signature = dingTalkSecurity.signature(secret, ticket, timestamp);

            HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getCorpToken)).newBuilder();
            url.addEncodedQueryParameter("accessKey", dingTalkConfig.getKey());
            url.addEncodedQueryParameter("timestamp", String.valueOf(timestamp));
            url.addEncodedQueryParameter("suiteTicket", ticket);
            url.addEncodedQueryParameter("signature", signature);

            CorpTokenRequest args = new CorpTokenRequest(corpId);

            RequestBody body = RequestBody.create(jsonMediaType, JSON.toJSONString(args));

            Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().maxAge(7200, TimeUnit.SECONDS).build())
                    .url(url.build())
                    .post(body)
                    .build();

            String json = execute(request);
            AccessToken accessToken = JSON.parseObject(json, AccessToken.class);

            Assert.isTrue(accessToken.getCode().equals(0), accessToken.getMsg());
            return accessToken.getToken();
        } catch (Exception ex) {
            log.error("get corp token err:", ex);
            return "";
        }
    }

    /**
     * 获取jsapi_ticket
     * <a href="https://developers.dingtalk.com/document/app/obtain-jsapi_ticket">获取jsapi_ticket</a>
     *
     * @param token 企业的token
     * @return jsapi token
     */
    @Override
    public String getTicketToken(String token) {
        try {
            Assert.hasLength(token, "token不能为空");
            HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getTicketToken)).newBuilder();
            url.addEncodedQueryParameter("access_token", token);

            Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().maxAge(7200, TimeUnit.SECONDS).build())
                    .url(url.build())
                    .get()
                    .build();

            String json = execute(request);
            Ticket ticket = JSON.parseObject(json, Ticket.class);

            Assert.isTrue(ticket.getCode().equals(0), ticket.getMsg());
            return ticket.getTicket();
        } catch (IOException e) {
            log.error("get ticket token err:", e);
            return "";
        }
    }

    /**
     * 获取钉钉dd.config
     *
     * @param url    当前网页的URL，不包含#及其后面部分。
     * @param corpId 企业id
     * @return DDConfig
     */
    @Override
    public ApiResponse<DDConfig> generateDingTalkConfig(String url, String corpId) {
        try {
            String token;
            if (dingTalkConfig.isIsv()) {
                token = getCorpToken(corpId);
            } else {
                token = getToken();
            }

            String ticket = getTicketToken(token);
            String nonce = dingTalkSecurity.getRandomStr(6);
            long timestamp = System.currentTimeMillis();
            String signature = dingTalkSecurity.signature(ticket, nonce, timestamp, url);

            return ApiResponse.success(new DDConfig()
                    .setNonce(nonce)
                    .setCorpId(corpId)
                    .setTimeStamp(timestamp)
                    .setSignature(signature)
                    .setApis(dingTalkConfig.getApis())
                    .setAgentId(dingTalkConfig.getId()));

        } catch (Exception ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }
}
