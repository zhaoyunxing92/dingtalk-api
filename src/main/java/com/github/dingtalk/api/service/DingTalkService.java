package com.github.dingtalk.api.service;

import com.alibaba.fastjson.JSON;
import com.github.dingtalk.api.domain.*;
import com.github.dingtalk.api.domain.dingtalk.AdminUserInfo;
import com.github.dingtalk.api.domain.dingtalk.DingTalkResponse;
import com.github.dingtalk.api.domain.dingtalk.UserDetail;
import com.github.dingtalk.api.domain.dingtalk.UserInfo;
import com.github.dingtalk.api.exception.ServiceException;
import com.github.dingtalk.api.vo.User;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉相关服务
 *
 * @author zyx
 * @date 2021-08-24 21:52:29
 */
public interface DingTalkService {

    String api = "https://oapi.dingtalk.com";

    /**
     * 获取token
     */
    String getToken = api + "/gettoken";

    /**
     * 获取微应用后台免登的access_token
     */
    String getSSOToken = api + "/sso/gettoken";

    /**
     * corp token
     */
    String getCorpToken = api + "/service/get_corp_token";

    /**
     * jsapi token
     */
    String getTicketToken = api + "/get_jsapi_ticket";

    /**
     * 用户信息
     */
    String getUserInfo = api + "/user/getuserinfo";

    /**
     * SSO 用户信息
     */
    String getSSOUserInfo = api + "/sso/getuserinfo";

    /**
     * 用户详情
     */
    String getUserDetail = api + "/user/get";

    /**
     * okhttp client
     */
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            //            .cache(new Cache(new File(System.getProperty("user.dir"), ".cache"),
            //                    10 * 1024 * 1024))
            .build();

    /**
     * 获取token
     *
     * @param corpId 企业id
     * @param isv    是否isv
     * @param sso    是否sso
     * @return token
     */
    default String getAccessToken(String corpId, boolean isv, boolean sso) {
        if (isv) {
            return getCorpToken(corpId);
        }
        if (sso) {
            return getSSOToken(corpId);
        }
        return getToken();
    }

    /**
     * 发送请求
     *
     * @param req req
     * @return json 数据体
     * @throws IOException ex
     */
    default <T extends DingTalkResponse> T execute(Request req, Class<T> clazz) throws ServiceException, IOException {
        Response res = client.newCall(req).execute();

        if (Objects.isNull(res.body())) {
            throw new ServiceException(ApiResponse.fail("响应体为空"));
        }

        String json = res.body().string();
        T ding = JSON.parseObject(json, clazz);
        if (!ding.getCode().equals(0)) {
            throw new ServiceException(ApiResponse.fail(ding.getCode(), ding.getMsg()));
        }
        return ding;
    }

    /**
     * 获取企业内部token
     *
     * <a href=https://developers.dingtalk.com/document/app/obtain-orgapp-token>获取企业内部应用的access_token</a>
     *
     * @return token
     */
    String getToken();

    /**
     * <a href="https://developers.dingtalk.com/document/app/obtain-the-ssotoken-for-micro-application-background-logon-free">获取微应用后台免登的access_token</a>
     *
     * @param corpId 企业id
     * @return token
     */
    String getSSOToken(String corpId);


    /**
     * 获取第三方应用授权企业的access_token
     *
     * <a href=https://developers.dingtalk.com/document/app/obtains-the-enterprise-authorized-credential>获取企业内部应用的access_token</a>
     *
     * @param corpId 企业id
     * @return token
     */
    String getCorpToken(String corpId);

    /**
     * 获取jsapi_ticket
     * <a href="https://developers.dingtalk.com/document/app/obtain-jsapi_ticket">获取jsapi_ticket</a>
     *
     * @param corpId 企业的id
     * @return jsapi token
     */
    String getTicketToken(String corpId);

    /**
     * 获取钉钉dd.config
     *
     * @param url    当前网页的URL，不包含#及其后面部分。
     * @param corpId 企业id
     * @return DDConfig
     */
    ApiResponse<DDConfig> generateDingTalkConfig(String url, String corpId);

    /**
     * 根据临时授权码获取用户信息
     *
     * @param corpId 企业id
     * @param code   临时授权码
     * @param sso    是否sso
     * @return 用户信息
     */
    ApiResponse<UserDetail> getUserInfoByCode(String corpId, String code, boolean sso);

    /**
     * 获取用户信息
     *
     * @param corpId 企业id
     * @param code   临时授权码
     * @param sso    是否sso
     * @return 用户信息
     */
    UserInfo getUserInfo(String corpId, String code, boolean sso);

    /**
     * 获取用户详情
     *
     * @param corpId 企业id
     * @param userId 用户di
     * @return 用户详情
     */
    UserDetail getUserDetail(String corpId, String userId);

    /**
     * 获取应用管理身份信息
     *
     * @param code 临时授权码
     * @return userInfo
     */
    ApiResponse<User> getAdminUserInfo(String code);
}
