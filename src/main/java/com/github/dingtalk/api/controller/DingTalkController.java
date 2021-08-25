package com.github.dingtalk.api.controller;

import com.github.dingtalk.api.domain.ApiResponse;
import com.github.dingtalk.api.domain.DDConfig;
import com.github.dingtalk.api.service.DingTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉控制器
 *
 * @author zyx
 * @date 2021-08-24 21:51:22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dingtalk")
public class DingTalkController {

    private final DingTalkService dingTalkService;

    @GetMapping("/config")
    public ApiResponse<DDConfig> getDingTalkConfig(String corpId) {

        return dingTalkService.generateDingTalkConfig("",corpId);
    }
}
