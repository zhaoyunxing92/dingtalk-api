package com.github.dingtalk.api.service;

import com.alibaba.fastjson.JSON;
import com.github.dingtalk.api.domain.*;
import com.github.dingtalk.api.exception.ServiceException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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
     * corp token
     */
    String getCorpToken = api + "/service/get_corp_token";

    /**
     * jsapi token
     */
    String getTicketToken = api + "/get_jsapi_ticket";

    /**
     * okhttp client
     */
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .cache(new Cache(new File(System.getProperty("user.dir"), ".cache"),
                    10 * 1024 * 1024))
            .build();

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
     * @param token 企业的token
     * @return jsapi token
     */
    String getTicketToken(String token);

    /**
     * 获取钉钉dd.config
     *
     * @param url    当前网页的URL，不包含#及其后面部分。
     * @param corpId 企业id
     * @return DDConfig
     */
    ApiResponse<DDConfig> generateDingTalkConfig(String url, String corpId);
}
