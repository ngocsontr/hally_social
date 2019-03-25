package com.hally.influencerai.utils;

import android.util.Log;

import com.hally.influencerai.Constants;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public class CryptUtil {
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
            if (Constants.DEBUG) Log.d("CryptUtil", "param : " + param + " , SHA1 : " + buffer);
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return "";
        }
    }
}
