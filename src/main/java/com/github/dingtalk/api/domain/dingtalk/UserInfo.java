package com.github.dingtalk.api.domain.dingtalk;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息返回
 *
 * @author zyx
 * @date 2021-08-26 15:10:52
 */
@Getter
@Setter
public class UserInfo extends DingTalkResponse {

    /**
     * 员工在当前企业内的唯一标识，也称staffId。
     */
    @JSONField(name = "unionid")
    private String userId;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 设备ID。
     */
    private String deviceId;

    /**
     * 是否是管理员。
     * <p>
     * true：是
     * <p>
     * false：不是
     */
    @JSONField(name = "is_sys")
    private boolean admin;

    /**
     * 级别。
     * <p>
     * 1：主管理员
     * <p>
     * 2：子管理员
     * <p>
     * 100：老板
     * <p>
     * 0：其他（如普通员工）
     */
    @JSONField(name = "sys_level")
    private Integer level;


}
