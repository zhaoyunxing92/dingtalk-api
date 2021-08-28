package com.github.dingtalk.api.domain.dingtalk;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.dingtalk.api.vo.CorpInfo;
import com.github.dingtalk.api.vo.User;
import lombok.Getter;
import lombok.Setter;

/**
 * sso 登录返回信息
 * <pre>
 *      {
 *     "errcode": 0,
 *     "errmsg": "ok",
 *     "corp_info": {
 *         "corpid": "dingc7c5220402493357f2c783f7214b6d69",
 *         "corp_name": "钉钉小程序开发团队"
 *     },
 *     "user_info": {
 *         "name": "赵云兴",
 *         "avatar": "https://static-legacy.dingtalk.com/media/lADOMeO_Es0CZM0CZA_612_612.jpg",
 *         "userid": "manager164",
 *         "email": "2385585770@qq.com"
 *     },
 *     "is_sys": true
 * }
 * </pre>
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 14:35
 */
@Getter
@Setter
public class AdminUserInfo extends DingTalkResponse {

    /**
     * 企业信息
     */
    @JSONField(name = "corp_info")
    private Corp corp;

    /**
     * 用户信息
     */
    @JSONField(name = "user_info")
    private UserDetail user;

    /**
     * 是否管理员
     */
    @JSONField(name = "is_sys")
    private boolean sys;

    /**
     * 转换数据
     *
     * @return User
     */
    public User conversion() {
        return new User()
                .setAdmin(sys)
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setAvatar(user.getAvatar())
                .setUserId(user.getUserId())
                .setCorpInfo(new CorpInfo(corp.getId(), corp.getName()));
    }
}
