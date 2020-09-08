package com.india.elephantloan.present.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.india.elephantloan.R;
import com.india.elephantloan.model.mode.OrderData;

import java.util.List;

public class OrderListAdapter extends ArrayAdapter {
    private final int resourceId;

    public OrderListAdapter(@NonNull Context context, int resource, @NonNull List<OrderData> objects) {
        super(context, resource,objects);
        this.resourceId = resource;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderData orderData = (OrderData) getItem(position); // 获取当前项的ElevenChooseFiveNumberDate实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView imgUserIcon = (ImageView) view.findViewById(R.id.list_user_icon);
        TextView  tvUserName= (TextView) view.findViewById(R.id.list_user_name);
        TextView tvUserQuota = (TextView) view.findViewById(R.id.list_user_quota);
        TextView tvUserInterest = (TextView) view.findViewById(R.id.list_user_interest);
        TextView tvUserTime = (TextView) view.findViewById(R.id.list_user_time);
        TextView tvUserStage = (TextView) view.findViewById(R.id.list_user_stage);
        Glide.with(getContext())
                .load(orderData.getUserIcon())
                .into(imgUserIcon);
        tvUserName.setText(orderData.getUserName());
        tvUserQuota.setText(orderData.getUserQuota());
        tvUserInterest.setText(orderData.getUserInterest());
        tvUserTime.setText(orderData.getUserTime());
        tvUserStage.setText(orderData.getUserStage());
        return view;
    }

}
