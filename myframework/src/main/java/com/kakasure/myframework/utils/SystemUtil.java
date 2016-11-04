package com.kakasure.myframework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.kakasure.myframework.MyFramework;
import com.kakasure.myframework.view.MyToast;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统的工具类，封装了调用系统功能接口的方法
 * Created by danke on 2015/12/30.
 */
public class SystemUtil {
    private static int memorySize;
    private static String imei;
    private static String phoneNumber;
    private static String version;
    private static int versionCode;
    private static String macId;
    private static WifiManager wifiManager;
    private static ConnectivityManager connMgr;
    private static TelephonyManager phoneMgr;

    private SystemUtil() {

    }

    /**
     * 获取系统内存大小
     *
     * @param appContext
     * @return
     */
    public static int getMemorySize(Context appContext) {
        if (memorySize <= 0) {
            memorySize = 1024 * 1024 *
                    ((ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        }
        return memorySize;
    }

    /**
     * 获取设备IMEI号
     *
     * @return
     */
    public static String getIMEI() {
        if (imei == null) {
            getSystemInfo();
        }
        return imei;
    }

    /**
     * 获取用户手机号
     *
     * @return
     */
    public static String getPhoneNumber() {
        if (phoneNumber == null) {
            getSystemInfo();
        }
        return phoneNumber;
    }

    /**
     * 获取Android设备ID
     *
     * @return
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(MyFramework.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取当前版本名称
     *
     * @return
     */
    public static String getVersion() {
        if (version == null) {
            getSystemInfo();
        }
        return version;
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public static int getVersionCode() {
        if (versionCode == 0) {
            getSystemInfo();
        }
        return versionCode;
    }

    /**
     * 获取Mac
     * @return
     */
    public static String getMacId() {
        if (macId == null) {
            macId = ((WifiManager) MyFramework.getAppContext().getSystemService(Context.WIFI_SERVICE)).
                    getConnectionInfo().getMacAddress();
        }
        return macId;
    }

    /**
     * 获取IP地址
     * @return
     */
    public static String getIpAddress() {
        wifiManager = (wifiManager == null) ? (WifiManager)MyFramework.getAppContext().getSystemService(Context.WIFI_SERVICE) : wifiManager;
        int ipCode = wifiManager.getConnectionInfo().getIpAddress();
        String ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(
                    String.format("%d.%d.%d.%d", (ipCode & 0xff),
                            (ipCode >> 8 & 0xff), (ipCode >> 16 & 0xff),
                            (ipCode >> 24 & 0xff))).getHostAddress();
        } catch (UnknownHostException e) {
            ipAddress = "unknown host";
        }
        return ipAddress;
    }

    /**
     * 判断网络是否可用
     * @return
     */
    public static boolean isNetActive() {
        connMgr = (connMgr == null) ? (ConnectivityManager) MyFramework.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) : connMgr;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统基本信息
     */
    private static void getSystemInfo() {
        Context appContext = MyFramework.getAppContext();
        phoneMgr = (phoneMgr == null) ?
                (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE) : phoneMgr;

        PackageManager manager = appContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(appContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        imei = phoneMgr.getDeviceId();
        phoneNumber = phoneMgr.getLine1Number();
        version = (info != null) ? info.versionName : "unknow";
        versionCode = (info != null) ? info.versionCode : -1;
    }

    /**
     * 调用系统浏览器打开URL
     *
     * @param context
     * @param url
     */
    public static void openUrl(Context context, String url) {
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri
                    .parse(url)));
        } else
            MyToast.showBottom("URL地址无效");
    }

    /**
     * SD是否装载
     *
     * @return
     */
    public static boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取渠道名
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Activity ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 获取cpu 架构
     * arm64-v8a、armeabi、armeabi-v7a、x86、x86_64
     * @return
     */
    public static String getCpuName() {
        String arch = "";//cpu类型
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getDeclaredMethod("get", new Class[] {String.class});
            arch = (String)get.invoke(clazz, new Object[] {"ro.product.cpu.abi"});
        } catch(Exception e) {
            e.printStackTrace();
        }
        MyLog.d("arch：" + arch);
        return arch;
    }

    /**
     * 判断是否为x86，x86_64的cpu架构
     * @return
     */
    public static boolean isInterCpu() {
        String cpuName = getCpuName();
        return "x86".equals(cpuName) || "x86_64".equals(cpuName);
    }
}
