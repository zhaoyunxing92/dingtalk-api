package com.github.dingtalk.api.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author zhaoyunxing
 * @date: 2021-08-28 22:00
 */
public class PKCS7Padding {

    public static byte[] getPaddingBytes(int count) {
        int amountToPad = 32 - count % 32;
        if (amountToPad == 0) {
            amountToPad = 32;
        }

        char padChr = chr(amountToPad);
        StringBuilder tmp = new StringBuilder();

        for (int index = 0; index < amountToPad; ++index) {
            tmp.append(padChr);
        }
        return tmp.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] removePaddingBytes(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    static char chr(int a) {
        byte target = (byte) (a & 255);
        return (char) target;
    }
}
