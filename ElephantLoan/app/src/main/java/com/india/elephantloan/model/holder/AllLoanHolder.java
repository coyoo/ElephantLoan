package com.india.elephantloan.model.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseHolder;
import com.india.elephantloan.model.mode.OrderData;

/**
 * Created by zhangzc on 2017/6/12.
 */

public class AllLoanHolder extends BaseHolder<OrderData> {
    ImageView imgUserIcon;
    TextView tvUserName, tvUserQuota, tvUserInterest, tvUserTime, tvUserStage;


    public AllLoanHolder(View itemView) {
        super(itemView);
        imgUserIcon = (ImageView) itemView.findViewById(R.id.list_user_icon);
        tvUserName= (TextView) itemView.findViewById(R.id.list_user_name);
        tvUserQuota = (TextView) itemView.findViewById(R.id.list_user_quota);
        tvUserInterest = (TextView) itemView.findViewById(R.id.list_user_interest);
        tvUserTime = (TextView) itemView.findViewById(R.id.list_user_time);
        tvUserStage = (TextView) itemView.findViewById(R.id.list_user_stage);






    }


    @Override
    public void setData(OrderData orderData, int position) {
        Glide.with(this.itemView.getContext())
                .load(orderData.getUserIcon())
                .into(imgUserIcon);
        tvUserName.setText(orderData.getUserName());
        tvUserQuota.setText(orderData.getUserQuota());
        tvUserInterest.setText(orderData.getUserInterest());
        tvUserTime.setText(orderData.getUserTime());
        tvUserStage.setText(orderData.getUserStage());



    }
}
