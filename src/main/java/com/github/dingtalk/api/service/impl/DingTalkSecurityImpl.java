package com.github.dingtalk.api.service.impl;

import com.github.dingtalk.api.domain.DingTalkConfig;
import com.github.dingtalk.api.domain.DingTalkEventEncrypt;
import com.github.dingtalk.api.exception.ServiceException;
import com.github.dingtalk.api.service.DingTalkSecurity;
import com.github.dingtalk.api.service.PKCS7Padding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 安全类全部负责加密
 *
 * @author zyx
 * @date 2021-08-25 11:03:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingTalkSecurityImpl implements DingTalkSecurity {

    private final DingTalkConfig dingTalkConfig;

    /**
     * 获取事件回调签名
     *
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   加密消息体
     * @return 签名
     */
    @Override
    public String eventSignature(String timestamp, String nonce, String encrypt) {

        try {
            String str = Stream.of(dingTalkConfig.getToken(), timestamp, nonce, encrypt).sorted().reduce(String::concat).toString();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexStr = new StringBuilder();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 255);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }

                hexStr.append(shaHex);
            }
            return hexStr.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.error("事件回调签名异常:", ex);
            throw new ServiceException("事件回调签名异常");
        }
    }

    /**
     * 消息解密
     *
     * @param text 加密字符串
     * @return str
     */
    @Override
    public String decrypt(String text) {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            byte[] aes = dingTalkConfig.getAes().getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(aes, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aes, 0, 16));
            cipher.init(2, keySpec, iv);

            byte[] networkOrder = Base64.decodeBase64(text);
            byte[] originalArr = cipher.doFinal(networkOrder);

            byte[] bytes = PKCS7Padding.removePaddingBytes(originalArr);
            networkOrder = Arrays.copyOfRange(bytes, 16, 20);
            int plainTextLength = bytes2int(networkOrder);

            String plainText = new String(Arrays.copyOfRange(bytes, 20, 20 + plainTextLength));
            String corpId = new String(Arrays.copyOfRange(bytes, 20 + plainTextLength, bytes.length));

            if (!dingTalkConfig.getCorpId().equals(corpId)) {
                log.error("钉钉消息解密异常,企业id不匹配,解密={},配置={}", corpId, dingTalkConfig.getCorpId());
                throw new ServiceException("钉钉消息解密异常,企业id不匹配");
            }

            return plainText;
        } catch (Exception ex) {
            log.error("钉钉消息解密异常:", ex);
            throw new ServiceException("钉钉消息解密异常");
        }
    }

    /**
     * 加密
     *
     * @param random    随机字符串
     * @param plaintext 字符串
     * @return str
     */
    @Override
    public String encrypt(String random, String plaintext) {
        try {
            byte[] randomBytes = random.getBytes(StandardCharsets.UTF_8);
            byte[] plainTextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] lengthByte = int2Bytes(plainTextBytes.length);
            byte[] corpIdBytes = dingTalkConfig.getCorpId().getBytes(StandardCharsets.UTF_8);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(randomBytes);
            byteStream.write(lengthByte);
            byteStream.write(plainTextBytes);
            byteStream.write(corpIdBytes);
            byte[] padBytes = PKCS7Padding.getPaddingBytes(byteStream.size());

            byteStream.write(padBytes);
            byte[] unencrypted = byteStream.toByteArray();
            byteStream.close();

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

            byte[] aes = dingTalkConfig.getAes().getBytes(StandardCharsets.UTF_8);

            SecretKeySpec keySpec = new SecretKeySpec(aes, "AES");
            IvParameterSpec iv = new IvParameterSpec(aes, 0, 16);
            cipher.init(1, keySpec, iv);
            byte[] encrypted = cipher.doFinal(unencrypted);
            return new Base64().encodeToString(encrypted);
        } catch (Exception var15) {
            throw new ServiceException("钉钉消息加密异常");
        }
    }

    /**
     * 返回success的加密信息表示回调处理成功
     *
     * @return success
     */
    @Override
    public DingTalkEventEncrypt getSuccessEventEncrypt() {
        String nonce = getRandomStr(8);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        String encrypt = encrypt(nonce, "success");
        String signature = eventSignature(timestamp, nonce, encrypt);

        return new DingTalkEventEncrypt()
                .setNonce(nonce)
                .setEncrypt(encrypt)
                .setSignature(signature)
                .setTimeStamp(timestamp);
    }
}
