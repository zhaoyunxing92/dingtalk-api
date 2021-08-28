package com.github.dingtalk.api.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 钉钉消息处理器
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:27
 */
public interface DingTalkEventProcessor {
    /**
     * 消息处理
     *
     * @param json json数据
     */
    void dispose(JSONObject json);
}
