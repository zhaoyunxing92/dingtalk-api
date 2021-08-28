package com.github.dingtalk.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户信息
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 14:44
 */
@Getter
@Setter
@Accessors(chain = true)
public class User {

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 是否管理员
     */
    private boolean admin;

    /**
     * 企业信息
     */
    private CorpInfo corpInfo;

}
