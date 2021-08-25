package com.github.dingtalk.api.service.impl;


import com.github.dingtalk.api.DingtalkApiApplicationTests;
import com.github.dingtalk.api.service.DingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-08-25 10:42:33
 */
@Slf4j
public class DingTalkServiceImplTest extends DingtalkApiApplicationTests {

    @Autowired
    private DingTalkService dingTalkService;

    @Test
    public void getToken() {
        String token = dingTalkService.getToken();
        log.info("token={}", token);
    }

    @Test
    public void getCorpToken() {
        String token = dingTalkService.getCorpToken("ding41944dc8209e65a835c2f4657eb6378f");
        log.info("token={}", token);
    }

    @Test
    public void getTicketToken() {
        String token = dingTalkService.getToken();
        String ticketToken = dingTalkService.getTicketToken(token);
        log.info("ticket={}", ticketToken);
    }

    @Test
    public void generateDingTalkConfig() {
    }
}