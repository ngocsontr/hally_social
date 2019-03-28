package com.hally.influencerai.utils;

import android.util.Log;

import com.hally.influencerai.Constants;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public class CryptUtil {
    private static final String TAG = CryptUtil.class.getName();

    private final static String HEX = "0123456789ABCDEF";
    private static final byte[] KEY_VALUE =
            new byte[]{'c', 'o', 'd', 'i', 'n', 'g', 'a', 'f', 'f', 'a', 'i', 'r', 's', 'c', 'o', 'm'};

    public static String encrypt(String cleartext) {
        byte[] rawKey = getRawKey();
        byte[] result = new byte[0];
        try {
            result = encrypt(rawKey, cleartext.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(result);
    }

    public static String decrypt(String encrypted) {
        byte[] enc = toByte(encrypted);
        byte[] result = new byte[0];
        try {
            result = decrypt(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(result);
    }

    private static byte[] getRawKey() {
        SecretKey key = new SecretKeySpec(KEY_VALUE, "AES");
        return key.getEncoded();
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] encrypted)
            throws Exception {
        SecretKey skeySpec = new SecretKeySpec(KEY_VALUE, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    public static String SHA1(String param) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            param += formatter.format(Calendar.getInstance().getTime());
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(param.getBytes("UTF-8"));
            StringBuilder buffer = new StringBuilder();
            for (byte b : messageDigest.digest()) {
                buffer.append(String.format(Locale.getDefault(), "%02x", b));
            }
            if (Constants.DEBUG) Log.d(TAG, "param : " + param + " , SHA1 : " + buffer);
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return "";
        }
    }
}
