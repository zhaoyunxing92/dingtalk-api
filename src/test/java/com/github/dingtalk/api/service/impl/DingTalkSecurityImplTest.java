package com.github.dingtalk.api.service.impl;


import com.github.dingtalk.api.DingtalkApiApplicationTests;
import com.github.dingtalk.api.service.DingTalkSecurity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-08-25 11:07:14
 */
@Slf4j
public class DingTalkSecurityImplTest extends DingtalkApiApplicationTests {

    @Autowired
    private DingTalkSecurity dingTalkSecurity;

    @Test
    public void signature() throws Exception {

        String secret = "c9RMIAGoL_K26gxkw4LrLGf2ERSQVqMBd1K_jclUIT3WM249uVCwf0Ug2RpG4dBh";
        String ticket = "SEx31XX20YLqvcLVpnwObQHIbvv85ZD6UGXCphXEmp0zCNNfj6VMMklvZjVNDCf99rrKc476XMKS56d5p6iCOU";
        String signature = dingTalkSecurity.signature(secret, ticket, 1629879099342L);

        Assert.isTrue(signature.equals("auGsFduirwTx3fYqswR8SCuaKbHpHjJzUB%2FcIAWHGuw%3D"), "签名计算错误");
    }
}