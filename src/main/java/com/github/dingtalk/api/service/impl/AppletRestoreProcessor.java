package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.dingtalk.api.service.DingTalkEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <a href="https://developers.dingtalk.com/document/app/enable-and-disable-application-events-1">启用应用事件</a>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:31
 */
@Slf4j
@Service("org_micro_app_restore")
public class AppletRestoreProcessor implements DingTalkEventProcessor {
    /**
     * 消息处理
     *
     * @param json json数据
     */
    @Override
    public void dispose(JSONObject json) {
        log.info("org_micro_app_restore={}", json.toJSONString());
    }
}
