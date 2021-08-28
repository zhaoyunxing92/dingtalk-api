package com.github.dingtalk.api.domain.dingtalk;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.dingtalk.api.vo.CorpInfo;
import com.github.dingtalk.api.vo.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户详情
 *
 * @author zyx
 * @date 2021-08-26 15:46:32
 */
@Getter
@Setter
public class UserDetail extends DingTalkResponse {

    /**
     * 员工在当前开发者企业账号范围内的唯一标识，系统生成，固定值，不会改变。
     */
    @JSONField(name = "unionid")
    private String unionId;

    private String openId;

    @JSONField(name = "userid")
    private String userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 职位信息。
     */
    private String position;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 国家地区码
     */
    private String stateCode;

    /**
     * 邮件
     */
    private String email;

    /**
     * 是否实名认证：
     * <p>
     * true：是
     * <p>
     * false：否
     */
    private Boolean realAuthed;

    /**
     * 用户所在角色列表。
     */
    private List<Role> roles;

    /**
     * 转换数据
     *
     * @return User
     */
    public User conversion() {
        return new User()
                .setName(getName())
                .setEmail(getEmail())
                .setAvatar(getAvatar())
                .setUserId(getUserId());
    }
}
