package com.github.dingtalk.api.controller;

import com.github.dingtalk.api.domain.ApiResponse;
import com.github.dingtalk.api.domain.DDConfig;
import com.github.dingtalk.api.domain.dingtalk.UserDetail;
import com.github.dingtalk.api.domain.dingtalk.UserInfo;
import com.github.dingtalk.api.service.DingTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取dd config
     *
     * @param corpId 企业id
     * @return config
     */
    @GetMapping("/dd/config/{corpId}")
    public ApiResponse<DDConfig> getDingTalkConfig(HttpServletRequest request,
                                                   @PathVariable(name = "corpId") String corpId) {

        String url = request.getRequestURL().toString();

        return dingTalkService.generateDingTalkConfig(url, corpId);
    }

    /**
     * 根据临时授权码获取用户信息
     *
     * @param corpId 企业id
     * @param code   临时授权码
     * @return 用户信息
     */
    @GetMapping("/user/detail")
    public ApiResponse<UserDetail> getDingTalkUserInfo(@RequestParam String corpId, @RequestParam String code) {

        return dingTalkService.getDingTalkUserInfoByCode(corpId, code);
    }
}
