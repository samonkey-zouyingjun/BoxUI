package com.zouyingjun.samonkey.boxui.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Tony J on 2017/1/4.
 */

public class Utils {

    /**
     * 获取当前无线ip
     *
     * @return
     */
    public static String getWifiIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }


    /**
     * 将int型ip转换成String型ip
     *
     * @param i
     * @return
     */
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 秒转时间
     * 60s
     * 01：00
     *
     * **/
    public static String formateTime(int sec){
        if(sec <0) return "参数错误";
        String time="";
        int i = sec / 60;
        if(i<=9) time = 0+""+i+":";
        else time = i+":";
        int s = sec - i*60;
        if(s<=9) time += "0"+s;
        else time +=s;
        return time;
    }

    /**
     * 判断是否接入WIFI
     *
     * */
    public static boolean checkWifi(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }


}
