package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.dingtalk.api.service.DingTalkEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <a href="https://developers.dingtalk.com/document/app/push-suite_ticket-events-1">推送suite_ticket事件</a>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:31
 */
@Slf4j
@Service("suite_ticket")
public class SuiteTicketProcessor implements DingTalkEventProcessor {

    /**
     * 消息处理
     *
     * @param json json数据
     */
    @Override
    public void dispose(JSONObject json) {
        log.info("suite_ticket={}", json.toJSONString());
    }
}
