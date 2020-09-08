package com.india.elephantloan.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;


/**
 * Created by wangxd on 2019\1\2 0002.
 */
public class RestartAppService extends Service {
    private Handler handler;
    private String packageName;
    public RestartAppService(){
        handler = new Handler();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        packageName = intent.getStringExtra("packageName");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                RestartAppService.this.stopSelf();
            }
        },3000);
        return super.onStartCommand(intent, flags, startId);
    }
}
