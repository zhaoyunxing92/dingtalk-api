package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.dingtalk.api.domain.*;
import com.github.dingtalk.api.domain.dingtalk.AccessToken;
import com.github.dingtalk.api.domain.dingtalk.Ticket;
import com.github.dingtalk.api.domain.dingtalk.UserDetail;
import com.github.dingtalk.api.domain.dingtalk.UserInfo;
import com.github.dingtalk.api.exception.ServiceException;
import com.github.dingtalk.api.request.CorpTokenRequest;
import com.github.dingtalk.api.service.DingTalkSecurity;
import com.github.dingtalk.api.service.DingTalkService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Instant;
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
    @SneakyThrows
    @Override
    public String getToken() {

        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getToken)).newBuilder();
        url.addEncodedQueryParameter("appkey", dingTalkConfig.getKey());
        url.addEncodedQueryParameter("appsecret", dingTalkConfig.getSecret());
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxAge(7000, TimeUnit.SECONDS).build())
                .url(url.build())
                .get()
                .build();

        return execute(request, AccessToken.class).getToken();
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
    @SneakyThrows
    public String getCorpToken(String corpId) {

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
                .cacheControl(new CacheControl.Builder().maxAge(7000, TimeUnit.SECONDS).build())
                .url(url.build())
                .post(body)
                .build();

        return execute(request, AccessToken.class).getToken();
    }

    /**
     * 获取jsapi_ticket
     * <a href="https://developers.dingtalk.com/document/app/obtain-jsapi_ticket">获取jsapi_ticket</a>
     *
     * @param corpId 企业的token
     * @return jsapi token
     */
    @Override
    @SneakyThrows
    public String getTicketToken(String corpId) {

        String token = getAccessToken(corpId, dingTalkConfig.isIsv());

        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getTicketToken)).newBuilder();
        url.addEncodedQueryParameter("access_token", token);

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxAge(7000, TimeUnit.SECONDS).build())
                .url(url.build())
                .get()
                .build();

        return execute(request, Ticket.class).getTicket();
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

            String ticket = getTicketToken(corpId);
            String nonce = dingTalkSecurity.getRandomStr(8);
            long timestamp = Instant.now().getEpochSecond();

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

    /**
     * 通过免登码获取用户信息
     *
     * <a href="https://developers.dingtalk.com/document/app/get-user-userid-through-login-free-code?spm=ding_open_doc.document.0.0.6f6547e5lYFgNi#topic-1936806">通过免登码获取用户信息</a>
     *
     * @param corpId 企业id
     * @param code   临时授权码
     * @return 用户信息
     */
    @Override
    @SneakyThrows
    public UserInfo getUserInfo(String corpId, String code) {

        String token = getAccessToken(corpId, dingTalkConfig.isIsv());
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getUserInfo)).newBuilder();
        url.addEncodedQueryParameter("access_token", token);
        url.addEncodedQueryParameter("code", code);

        Request request = new Request.Builder()
                .url(url.build())
                .get()
                .build();

        return execute(request, UserInfo.class);
    }

    /**
     * 获取用户详情
     *
     * @param corpId 企业id
     * @param userId 用户di
     * @return 用户详情
     */
    @Override
    @SneakyThrows
    public UserDetail getUserDetail(String corpId, String userId) {

        String token = getAccessToken(corpId, dingTalkConfig.isIsv());

        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(getUserDetail)).newBuilder();
        url.addEncodedQueryParameter("access_token", token);
        url.addEncodedQueryParameter("userid", userId);

        Request request = new Request.Builder()
                .url(url.build())
                .get()
                .build();

        return execute(request, UserDetail.class);
    }

    /**
     * 根据临时授权码获取用户信息
     *
     * @param corpId 企业id
     * @param code   临时授权码
     * @return 用户信息
     */
    @Override
    public ApiResponse<UserDetail> getDingTalkUserInfoByCode(String corpId, String code) {
        UserInfo userInfo = getUserInfo(corpId, code);
        return ApiResponse.success(getUserDetail(corpId, userInfo.getUserId()));
    }
}
