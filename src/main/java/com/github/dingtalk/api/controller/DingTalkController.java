package com.github.dingtalk.api.controller;

import com.github.dingtalk.api.domain.ApiResponse;
import com.github.dingtalk.api.domain.DDConfig;
import com.github.dingtalk.api.domain.dingtalk.AdminUserInfo;
import com.github.dingtalk.api.domain.dingtalk.UserDetail;
import com.github.dingtalk.api.domain.dingtalk.UserInfo;
import com.github.dingtalk.api.service.DingTalkService;
import com.github.dingtalk.api.vo.User;
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
     * 企业内部应用免登
     *
     * @return config
     */
    @GetMapping("/login")
    public ApiResponse<UserDetail> login(@RequestParam(name = "code") String authCode) {

        return dingTalkService.getUserInfoByCode("", authCode, false);
    }

    /**
     * 应用管理后台免登
     *
     * @param authCode 授权码
     * @return 用户信息
     */
    @GetMapping("/sso/login")
    public ApiResponse<User> ssoLogin(@RequestParam(name = "code") String authCode) {
        return dingTalkService.getAdminUserInfo(authCode);
    }

    /**
     * 获取dd config
     *
     * @param corpId 企业id
     * @return config
     */
    @GetMapping("/dd/config")
    public ApiResponse<DDConfig> getDingTalkConfig(HttpServletRequest request,
                                                   @RequestParam(name = "corpId") String corpId) {

        String domain = request.getRequestURL().toString();
        String query = request.getQueryString();

        String url = String.join("?", domain, query);

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

        return dingTalkService.getUserInfoByCode(corpId, code, false);
    }
}
