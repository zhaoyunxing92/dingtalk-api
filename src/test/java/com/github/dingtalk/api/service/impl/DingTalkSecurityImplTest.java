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

    @Test
    public void testJsApiSignature() throws Exception {
        String url = "http://test4.weixin.wtoip.com:80/wtoip/dingding/dzmp";
        String nonce = "abcde12345";
        long timeStamp = 1463125744L;

        String ticket = "gUsHOoPPzLVZKVkClnESg88m7qMV4c0Ys9VGsMigqzZU7gA8PeoNzHODmYPZ85TYuoZryXuqEUFlXLN1OPEixm";

        String signature = dingTalkSecurity.signature(ticket, nonce, timeStamp, url);

        Assert.isTrue(signature.equals("d14dfc1d0d98cad2438e664723e8a9d8633b443f"), "签名计算错误");
    }

    /**
     * 消息解密 https://developers.dingtalk.com/document/app/configure-event-subcription
     * <p>
     * toke:123456
     * ase: 1234567890123456789012345678901234567890123
     * appkey: dingsnotzck6pm5veliw
     * signature: 9a95a004dd16f5c307e849b994173f76aa26e5eb
     * timestamp: 1614767836
     * nonce: A7Co0cJLMzIDtMMI
     * encrypt: YvkvaGe4hQxd3VxRmEty0dVlnCOAqwf56xwTRHDHoOURqhalbmBJQk5FNcRk42Gl5T0YQXZNwpwWSm1xAFJ5ZA==
     * 解密出 success
     *
     * @return str
     */
    @Test
    public void testDecrypt() {

        String encrypt = dingTalkSecurity.encrypt("A7Co0cJLMzIDtMMI","success");

        String text = dingTalkSecurity.decryptMessage("9a95a004dd16f5c307e849b994173f76aa26e5eb", "1614767836", "A7Co0cJLMzIDtMMI", "YvkvaGe4hQxd3VxRmEty0dVlnCOAqwf56xwTRHDHoOURqhalbmBJQk5FNcRk42Gl5T0YQXZNwpwWSm1xAFJ5ZA==");

        log.info("encrypt={}，eq：{}", encrypt,encrypt.equals("YvkvaGe4hQxd3VxRmEty0dVlnCOAqwf56xwTRHDHoOURqhalbmBJQk5FNcRk42Gl5T0YQXZNwpwWSm1xAFJ5ZA=="));
        log.info("text={},eq:{}", text,text.equals("success"));
    }

}