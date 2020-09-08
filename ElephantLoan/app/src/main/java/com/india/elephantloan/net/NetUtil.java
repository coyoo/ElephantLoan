package com.india.elephantloan.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Browser;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.ui.view.SpannableClickable;
import com.india.elephantloan.utils.LogUtils;
import com.india.elephantloan.utils.UserUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zhangzc on 2017/5/17.
 */

public class NetUtil {

    private static boolean isWifiEnable(Context ctx) {
        WifiManager wifi = (WifiManager) ctx
                .getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * 检测网络连接是否可用
     *
     * @param ctx
     * @return true 可用; false 不可用
     */
    public static boolean isNetworkAvailable(Context ctx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (isWifiEnable(ctx)) {
                return true;
            }
            if (cm == null) {
                return false;
            }
            // 取当前默认的连接信息
            NetworkInfo netinfo = cm.getActiveNetworkInfo();
            if (netinfo != null && netinfo.isConnected()) {
                return true;
            }
            NetworkInfo.State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState();

            if (mobile == NetworkInfo.State.CONNECTED) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    @NonNull
    public static HttpParams addParam(HttpParams params) {
        if (params == null) {
            params = new HttpParams();
        }
//        if (!params.getParams().containsKey(Constants.APP_VERSION)) {
//            params.getParams().put(Constants.APP_VERSION, Constants.APPVERSION);
//            LogUtils.i("自动添加"+Constants.APP_VERSION,Constants.APPVERSION);
//        }
//        if (!params.getParams().containsKey(Constants.VERSION)) {
//            params.put(Constants.VERSION, Constants.VERSIONNAME);
//            LogUtils.i("自动添加"+Constants.VERSION,Constants.VERSIONNAME);
//        }
//        if (!params.getParams().containsKey(Constants.REQUESTTYPE)) {
//            params.put(Constants.REQUESTTYPE, CaipiaoConst.REQUESTTYPE);
//            LogUtils.i("自动添加"+Constants.REQUESTTYPE,CaipiaoConst.REQUESTTYPE);
//        }
//        if (!params.getParams().containsKey(Constants.LISTTYPE)) {
//            params.put(Constants.LISTTYPE, CaipiaoConst.LISTTYPE);
//            LogUtils.i("自动添加"+Constants.LISTTYPE,CaipiaoConst.LISTTYPE);
//        }
//
        if (!params.getParams().containsKey(Constants.CLIENT_USER_SESSION) && !TextUtils.isEmpty(UserUtil.getSession())) {
            params.put(Constants.CLIENT_USER_SESSION, UserUtil.getSession());
            LogUtils.i("自动添加"+Constants.CLIENT_USER_SESSION, UserUtil.getSession());
        }

        return params;
    }



    public static SpannableStringBuilder formatUrlString(String contentStr){

        SpannableStringBuilder sp;
        if(!TextUtils.isEmpty(contentStr)){

            sp = new SpannableStringBuilder(contentStr);
            try {
                //处理url匹配
                Pattern urlPattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                        "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
                Matcher urlMatcher = urlPattern.matcher(contentStr);

                while (urlMatcher.find()) {
                    final String url = urlMatcher.group();
                    if(!TextUtils.isEmpty(url)){
                        sp.setSpan(new SpannableClickable(){
                            @Override
                            public void onClick(View widget) {
                                Uri uri = Uri.parse(url);
                                Context context = widget.getContext();
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                context.startActivity(intent);
                            }
                        }, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //处理电话匹配
                Pattern phonePattern = Pattern.compile("[1][34578][0-9]{9}");
                Matcher phoneMatcher = phonePattern.matcher(contentStr);
                while (phoneMatcher.find()) {
                    final String phone = phoneMatcher.group();
                    if(!TextUtils.isEmpty(phone)){
                        sp.setSpan(new SpannableClickable(){
                            @Override
                            public void onClick(View widget) {
                                Context context = widget.getContext();
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }, phoneMatcher.start(), phoneMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            sp = new SpannableStringBuilder();
        }
        return sp;
    }
    /**
     * gps获取ip
     * @return
     */
    public static String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return null;
    }

    /**
     * wifi获取ip
     * @param context
     * @return
     */
    public static String getIp(Context context){
        try {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager)context. getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 格式化ip地址（192.168.11.1）
     * @param i
     * @return
     */
    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
    /**
     *  3G/4g网络IP
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取本机的ip地址（3中方法都包括）
     * @param context
     * @return
     */
    public static String getIpAdress(Context context){
        String ip = null;
        try {
            ip=getIp(context);
            if (ip==null){
                ip = getIpAddress();
                if (ip==null){
                    ip = getLocalIpAddress();
                }
            }
        } catch (Exception e) {
        }
        LogUtils.d("IpAdressUtils","ip="+ip);
        return ip;
    }
}
