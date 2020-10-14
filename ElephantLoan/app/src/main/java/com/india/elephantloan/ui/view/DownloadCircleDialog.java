package com.india.elephantloan.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.india.elephantloan.R;


public class DownloadCircleDialog extends Dialog {

    DownloadCircleView circleView;
    TextView tvMsg;
    ImageView imageView;

    public DownloadCircleDialog(Context context) {
        super(context, R.style.Theme_Ios_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_circle_dialog_layout);
        this.setCancelable(false);//设置点击弹出框外部，无法取消对话框
        circleView = findViewById(R.id.circle_view);
        tvMsg = findViewById(R.id.tv_msg);
        imageView=findViewById(R.id.iv_logo);
    }

    public void setProgress(int progress) {
        circleView.setProgress(progress);
    }

    public void setMsg(String msg) {
        tvMsg.setText(msg);
    }

    public void setImg(String img) {
        Glide.with(getContext())
                .load(img)
                .into(imageView);
    }
}
