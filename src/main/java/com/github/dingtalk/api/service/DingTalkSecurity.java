package com.github.dingtalk.api.service;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Random;

/**
 * 安全类全部负责加密
 *
 * @author zyx
 * @date 2021-08-25 11:00:12
 */
public interface DingTalkSecurity {

    /**
     * 随机字符串
     *
     * @param count 长度
     * @return 随机字符串
     */
    default String getRandomStr(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字节数组转化成十六进制字符串
     *
     * @param hash 字节数组
     * @return 十六进制字符串
     */
    default String byteToHex(final byte[] hash) {
        Formatter fmt = new Formatter();
        for (byte b : hash) {
            fmt.format("%02x", b);
        }
        String result = fmt.toString();
        fmt.close();
        return result;
    }

    /**
     * 因为ios端上传递的url是encode过的，android是原始的url。开发者使用的也是原始url,
     * 所以需要把参数进行一般urlDecode
     *
     * @param url url
     * @return decode url
     * @throws Exception ex
     */
    default String decodeUrl(String url) throws Exception {
        URL uri = new URL(url);
        StringBuilder urlBuffer = new StringBuilder();
        urlBuffer.append(uri.getProtocol());
        urlBuffer.append(":");
        if (uri.getAuthority() != null && uri.getAuthority().length() > 0) {
            urlBuffer.append("//");
            urlBuffer.append(uri.getAuthority());
        }
        if (uri.getPort() <= -1) {
            urlBuffer.append(":80");
        }
        if (uri.getPath() != null) {
            urlBuffer.append(uri.getPath());
        }
        if (uri.getQuery() != null) {
            urlBuffer.append('?');
            urlBuffer.append(URLDecoder.decode(uri.getQuery(), "utf-8"));
        }
        return urlBuffer.toString();
    }

    /**
     * 第三方访问接口的签名计算方法
     *
     * @param suitSecret  套件秘钥
     * @param suiteTicket ticket
     * @param timestamp   时间戳
     * @return 签名
     */
    default String signature(String suitSecret, String suiteTicket, long timestamp) throws Exception {

        String stringToSign = timestamp + "\n" + suiteTicket;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(suitSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));

        String sign = new String(Base64.encodeBase64(signData));
        return URLEncoder.encode(sign, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("~", "%7E")
                .replace("/", "%2F");
    }


    /**
     * 计算dd.config的签名参数
     *
     * @param ticket    通过微应用appKey获取的jsticket
     * @param nonce     自定义固定字符串
     * @param timeStamp 当前时间戳
     * @param url       调用dd.config的当前页面URL
     * @return 签名
     * @throws Exception ex
     */
    default String signature(String ticket, String nonce, long timeStamp, String url) throws Exception {
        try {
            String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonce + "&timestamp=" + timeStamp + "&url=" + decodeUrl(url);
            ;
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(plain.getBytes(StandardCharsets.UTF_8));
            return byteToHex(crypt.digest());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
