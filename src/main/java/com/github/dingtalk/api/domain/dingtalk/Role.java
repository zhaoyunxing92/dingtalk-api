package com.github.dingtalk.api.domain.dingtalk;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户所在角色列表。
 * * {
 * * "id": 1299380990,
 * * "name": "主管理员",
 * * "groupName": "默认",
 * * "type": 101
 * * }
 *
 * @author zyx
 * @date 2021-08-26 15:21:05
 */
@Getter
@Setter
public class Role {

    private Long id;

    private String name;

    private String groupName;

    private Integer type;
}
