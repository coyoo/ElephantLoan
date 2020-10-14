package com.india.elephantloan.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.ui.view.DownloadCircleDialog;
import com.india.elephantloan.utils.AppUtil;
import com.india.elephantloan.utils.DownloadUtils;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.SDCardUtils;
import com.india.elephantloan.utils.TextUtils;
import com.india.elephantloan.utils.UserUtil;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;

public class WebDownloadActivity extends BaseActivity {

    WebView webView;
    LinearLayout tvback;
    String title;
    String productId;
    String icon;
    DownloadCircleDialog dialogProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.setTranslucentStatus(this);

        mLoadingPager.showLoading();
        dialogProgress = new DownloadCircleDialog(this);
        String url = getIntent().getStringExtra("url");
        icon = getIntent().getStringExtra("icon");
        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("apk")) {
                    showNewVersion(url, title, productId, icon);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.e("TAG", url);
                mLoadingPager.showContent();
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_web;
    }


    @Override
    protected void initView() {
        setBack();
        tvback = findViewById(R.id.tv_jiantou);
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        productId = getIntent().getStringExtra("productId");
        setCusTomeTitle(title);
        postProductClick();

    }

    @Override
    protected void initListener() {
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent =new Intent(WebDownloadActivity.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });

    }

    //1.权限申请，通过后开始下载
    public void showNewVersion(final String url, final String name, final String productId, final String imgUrl) {
        AndPermission.with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> data) {

                        Log.e(TAG, "以获得权限");

                        new AlertDialog.Builder(WebDownloadActivity.this).setTitle(name).setMessage("Do you want to download " + name + " now ?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadApk(WebDownloadActivity.this, url, name, imgUrl);
                                        //postProductClick();
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();

                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> data) {
                        Log.e(TAG, "未获得权限" + data.toString());
                    }
                }).start();
    }
    //2.开始下载apk

    public void downloadApk(final Activity context, String down_url, final String name, String imgUrl) {
        dialogProgress.show();
        dialogProgress.setImg(imgUrl);
        DownloadUtils.getInstance().download(down_url, SDCardUtils.getSDCardPath(), name + ".apk", new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                dialogProgress.dismiss();
//                Log.i(TAG,"恭喜你下载成功，开始安装！==" + SDCardUtils.getSDCardPath() + name+".apk");
                MyToast.show(getApplicationContext(), "Congratulations on your successful download and installation !");
                String successDownloadApkPath = SDCardUtils.getSDCardPath() + name + ".apk";
                installApkO(getApplicationContext(), successDownloadApkPath);
            }

            @Override
            public void onDownloading(ProgressInfo progressInfo) {
                dialogProgress.setProgress(progressInfo.getPercent());
                boolean finish = progressInfo.isFinish();
                if (!finish) {
                    long speed = progressInfo.getSpeed();
                    dialogProgress.setMsg("(" + (speed > 0 ? TextUtils.formatFileSize(speed) : speed) + "/s) Downloading...");
                } else {
                    dialogProgress.setMsg("Download complete!");
                }
            }

            @Override
            public void onDownloadFailed() {
                dialogProgress.dismiss();
                MyToast.show(getApplicationContext(), "Download failed!");
            }
        });

    }

    // 3.下载成功，开始安装,兼容8.0安装位置来源的权限
    private void installApkO(Context context, String downloadApkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否有安装位置来源的权限
            boolean haveInstallPermission = SDCardUtils.isSDCardEnable();
            if (haveInstallPermission) {
                Log.i(TAG, "8.0手机已经拥有安装未知来源应用的权限，直接安装！");
                AppUtil.installApk(context, downloadApkPath);
            } else {
                showMyDialog();
            }
        } else {
            AppUtil.installApk(context, downloadApkPath);
        }
    }


    //4.开启了安装未知来源应用权限后，再次进行步骤3的安装。
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            Log.i(TAG, "设置了安装未知应用后的回调。。。");
            String successDownloadApkPath = SDCardUtils.getSDCardPath() + "QQ.apk";
            installApkO(getApplicationContext(), successDownloadApkPath);
        }
    }


    private void showMyDialog() {
        // 创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle("Tips");
        // 设置对话框消息
        isExit.setMessage("To install an application, you need to open the permission to install an application from an unknown source. Please open the permission in the settings");
        // 添加选择按钮并注册监听
        isExit.setButton("OK", listener);
        isExit.setButton2("CANCEL", listener);
        // 显示对话框
        isExit.show();
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    Uri packageUri = Uri.parse("package:" + AppUtil.getPackageName(getApplicationContext()));
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                    startActivityForResult(intent, 10086);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    private void postProductClick() {
        String url = Constants.POST_PRODUCT;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("productId", productId)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader(Constants.CLIENT_USER_SESSION, UserUtil.getSession())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                String connect = response.body().string();
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(connect);
                        int yes = object.getInt("status");

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "onResponse: " + connect);
            }
        });

    }
}
