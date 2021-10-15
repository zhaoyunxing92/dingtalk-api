package com.github.dingtalk.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.dingtalk.api.service.DingTalkEventProcessor;
import com.github.dingtalk.api.service.DingTalkSecurity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 钉钉事件回调处理器
 * <a href="https://developers.dingtalk.com/document/app/configure-event-subcription">配置事件订阅</a>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 20:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dingtalk/event")
public class DingTalkEventController {

    private final DingTalkSecurity dingTalkSecurity;

    private final Map<String, DingTalkEventProcessor> dingTalkEventProcessor;

    /**
     * 配置SyncHTTP推送
     * <a href="https://developers.dingtalk.com/document/app/configure-synchttp-push?spm=a2q3p.21071111.0.0.3ea265eeJj36LM">配置SyncHTTP推送</a>
     * <p>
     * 钉钉事件回调处理器
     * <a href="https://developers.dingtalk.com/document/app/configure-event-subcription">配置事件订阅</a>
     * <p>
     * 返回success的加密信息表示回调处理成功
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机串
     * @param json      推送数据
     * @return abc
     */
    @PostMapping(path = {"/sync"})
    public String syncCallback(@RequestParam(required = false) String signature,
                               @RequestParam(required = false) String timestamp,
                               @RequestParam(required = false) String nonce,
                               @RequestBody(required = false) JSONObject json) {
        try {
            String encrypt = dingTalkSecurity.decryptMessage(signature, timestamp, nonce, json.getString("encrypt"));
            JSONObject obj = JSON.parseObject(encrypt);
            String eventType = obj.getString("EventType");
            DingTalkEventProcessor processor = dingTalkEventProcessor.get(eventType);
            if (processor == null) {
                log.warn("钉钉事件回调未匹配到对应的【{}】处理器", eventType);
                return dingTalkSecurity.getSuccessEventEncrypt(timestamp, nonce);
            }
            processor.dispose(obj);
            return dingTalkSecurity.getSuccessEventEncrypt(timestamp, nonce);
        } catch (Exception ex) {
            log.error("钉钉回调异常：", ex);
            return "";
        }
    }
}
