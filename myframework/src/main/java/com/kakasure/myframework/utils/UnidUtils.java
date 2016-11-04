package com.kakasure.myframework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * 功能：获取手机唯一标识
 * Created by danke on 2016/2/4.
 */
public class UnidUtils {

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * 渠道标志为：
     * 1，andriod（a）
     * 识别符来源标志：
     * 1， IMEI（imei）；
     * 2， wifi mac地址（wifi）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {


        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("A");

        try {

            //IMEI（imei）
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            MyLogUtils.v("DeviceIMEI: " + tmDevice);
            tmSerial = "" + tm.getSimSerialNumber();
            MyLogUtils.v("GSM devices Serial Number[simcard]: " + tmSerial);
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            MyLogUtils.v("androidId CDMA devices: " + androidId);

            if (!StringUtil.isNull(tmDevice) && !StringUtil.isNull(tmSerial) && !StringUtil.isNull(androidId)) {
                UUID deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                deviceId.append("IMEI");
                deviceId.append(deviceUuid.toString());
                return deviceId.toString();
            }

            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!StringUtil.isNull(wifiMac)) {
                deviceId.append("WIFI");
                deviceId.append(wifiMac);
                MyLogUtils.e("getDeviceId : " + deviceId.toString());
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!StringUtil.isNull(sn)) {
                deviceId.append("SN");
                deviceId.append(sn);
                MyLogUtils.e("getDeviceId : " + deviceId.toString());
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!StringUtil.isNull(uuid)) {
                deviceId.append("ID");
                deviceId.append(uuid);
                MyLogUtils.e("getDeviceId : " + deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("ID").append(getUUID(context));
        }

        MyLogUtils.e("getDeviceId : " + deviceId.toString());

        return deviceId.toString();
    }


    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        String uuid = null;
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("UUID", "");
        }

        if (StringUtil.isNull(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("UUID", uuid).commit();
        }

        MyLogUtils.e("getDeviceId : " + uuid);
        return uuid;
    }
}
