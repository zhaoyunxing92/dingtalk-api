package com.github.dingtalk.api.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 企业信息
 *
 * @author zhaoyunxing
 * @date: 2021-08-28 14:46
 */
@Getter
@Setter
@AllArgsConstructor
public class CorpInfo {
    /**
     * 企业id信息
     */
    private String id;

    /**
     * 企业名称
     */
    private String name;
}
