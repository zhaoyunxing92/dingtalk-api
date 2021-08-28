package com.github.dingtalk.api.domain.dingtalk;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 企业信息
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 14:33
 */
@Setter
@Getter
public class Corp {

    /**
     * 企业id信息
     */
    @JSONField(name = "corpid")
    private String id;

    /**
     * 企业名称
     */
    @JSONField(name = "corp_name")
    private String name;
}
