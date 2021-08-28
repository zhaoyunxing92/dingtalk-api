package com.github.dingtalk.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.dingtalk.api.service.DingTalkEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 审批实例开始、结束
 * <a href="https://developers.dingtalk.com/document/app/approval-events-2">审批事件</a>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 22:31
 */
@Slf4j
@Service("bpms_instance_change")
public class BpmsInstanceChangeProcessor implements DingTalkEventProcessor {
    /**
     * 消息处理
     *
     * @param json json数据
     */
    @Override
    public void dispose(JSONObject json) {
        log.info("bpms_instance_change={}", json.toJSONString());
    }
}
