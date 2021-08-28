package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.dingtalk.api.service.DingTalkEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <a href="https://developers.dingtalk.com/document/app/app-callback-url-update-1">应用回调地址更新</a>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:31
 */
@Slf4j
@Service("check_update_suite_url")
public class CheckUpdateSuiteProcessor implements DingTalkEventProcessor {
    /**
     * 消息处理
     *
     * @param json json数据
     */
    @Override
    public void dispose(JSONObject json) {
        log.info("check_update_suite_url={}", json.toJSONString());
    }
}
