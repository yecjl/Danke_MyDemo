package com.kakasure.myframework.utils;

import android.annotation.SuppressLint;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressLint("DefaultLocale")
public class MD5Parse {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static String parseStrToMd5L16(String paramString) {
        String str = parseStrToMd5L32(paramString);
        if (str != null)
            str = str.substring(8, 24);
        return str;
    }

    public static String parseStrToMd5L32(String str) {
        try {
            byte[] arr = MessageDigest.getInstance("MD5")
                    .digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : arr) {
                int k = 0xFF & b;
                if (k < 16)
                    sb.append(0);
                sb.append(Integer.toHexString(k));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseStrToMd5U16(String param) {
        String str = parseStrToMd5L32(param);
        if (str != null)
            str = str.toUpperCase().substring(8, 24);
        return str;
    }

    public static String parseStrToMd5U32(String param) {
        String str = parseStrToMd5L32(param);
        if (str != null)
            str = str.toUpperCase();
        return str;
    }

    public static String md5sum(String filename) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            return toHexString(md5.digest()).toLowerCase();
        } catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

}