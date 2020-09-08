package com.india.elephantloan.net;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.utils.LogUtils;
import com.india.elephantloan.utils.UIUtils;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by ChengYi
 * Copyright 2014-2016 丞易软件 All rights reserved.
 */
public class OkHttpUtil {

    /**
     * post提交
     */
    private static RequestQueue mQueue;

    static {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(UIUtils.getContext());
        }
    }

    public static void postSubmitForm(String url, HttpParams params, final OnDownDataListener onDownDataListener) {

        params = addParam(params);

        String s = getString(params);
        LogUtils.d("url", url + s);
        final String finalUrl = url;
        final Map<String, String> finalParams = params.getParams();
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(response + "=");
                onDownDataListener.onResponse(finalUrl, response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return finalParams;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(300 * 1000, 1, 1.0f));
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(UIUtils.getContext());
        }
        mQueue.add(request);


    }


    public static void getSubmitForm(String url, HttpParams params, final OnDownDataListener onDownDataListener) {

        params = addParam(params);

        String s = getString(params);
        if (!url.startsWith("http")) {
            url = Constants.BASE_URL + url;
        }
        LogUtils.d("url", url + s);
        final String finalUrl = url;
        final Map<String, String> finalParams = params.getParams();
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(response + "=");
                onDownDataListener.onResponse(finalUrl, response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return finalParams;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(300 * 1000, 1, 1.0f));
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(UIUtils.getContext());
        }
        mQueue.add(request);


    }
    @NonNull
    private static HttpParams addParam(HttpParams params) {

        return NetUtil.addParam(params);
    }


    public interface OnDownDataListener {

        void onResponse(String url, String response);

        void onFailure(String url, String error);

    }

//    public static void postSubmitForm(String url, final Fragment context, final INet iNet, final int tag, final Class clazz, HttpParams params, final boolean flag) {
//        postSubmitForm(url, context.getActivity(), iNet, tag, clazz, params, flag);
//
//
//    }
//
//    public static void postSubmitForm(String url, final Context context, final INet iNet, final int tag, final Class clazz, HttpParams params, final boolean flag) {
//
//
//        params = addParam(params);
//
//        String s = getString(params);
//
//        if (url.startsWith("http")) {
//        } else {
//            url = Constants.BASE_URL + url;
//        }
//        LogUtils.d("apiUrl", url + "?" + s);
//
//        final Map<String, String> finalParams = params.getParams();
//        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JsonUtils.loginIntercept(context, response.toString());
//                JsonParse.parse(iNet, tag, clazz, response, flag, context);
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                iNet.response(tag, error.toString());
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return finalParams;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(300 * 1000, 1, 1.0f));
//
//        if (mQueue == null) {
//            mQueue = Volley.newRequestQueue(context);
//        }
//        mQueue.add(request);
//
//
//    }

    @NonNull
    public static String getString(HttpParams params) {
        StringBuffer s = new StringBuffer();
        String str="";
        if (params.getParams().size() > 0) {
            if(params.getHeaders().size() <= 0){
                s.append("?");
            }
            for (String key : params.getParams().keySet()) {
                String value = params.get(key);
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                s.append(key);
                s.append("=");
                s.append(value);
                s.append("&");

            }
        }
        if (params.getHeaders().size() > 0) {
            s.append("?");
            for (String key : params.getHeaders().keySet()) {
                String value = params.get(key);
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                s.append(key);
                s.append("=");
                s.append(value);
                s.append("&");

            }

        }
        str= s.toString();
        if (str.length() > 1 && str.endsWith("&")) {
            str = str.substring(0, s.length() - 1);
        }
        return str;
    }


    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
