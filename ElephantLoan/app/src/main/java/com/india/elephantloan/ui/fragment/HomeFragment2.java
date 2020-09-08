package com.india.elephantloan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseFragment;
import com.india.elephantloan.base.MyApplication;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.model.mode.OrderData;
import com.india.elephantloan.present.adapter.OrderListAdapter;
import com.india.elephantloan.ui.activity.GetLoanActivity;
import com.india.elephantloan.ui.activity.LoginActivity;
import com.india.elephantloan.ui.activity.MainActivity;
import com.india.elephantloan.ui.activity.WebDownloadActivity;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UIUtils;
import com.india.elephantloan.utils.UserUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;

public class HomeFragment2 extends BaseFragment {

    Button btnApply;
    OrderListAdapter adapter;
    private ListView listView;
    private List<OrderData> listDataList = new ArrayList<OrderData>();

    @Override
    protected int setContentViewLayout() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        btnApply=view.findViewById(R.id.home_apply_btn);
        listView = (ListView) getActivity().findViewById(R.id.find_listView);
    }

    @Override
    protected void initData() {
        if(!UIUtils.isLogin()){
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
            MainActivity mainActivity= (MainActivity) getActivity();
            mainActivity.mBottomBarLayout.setCurrentItem(0);
        }else{
            mLoadingPager.showLoading();
            getAllLoanMessage();
        }


    }

    @Override
    protected void initListener() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UIUtils.isLogin()){
                    MainActivity mainActivity= (MainActivity) getActivity();
                    mainActivity.mBottomBarLayout.setCurrentItem(1);
                }else{
                    Intent intent=new Intent(MyApplication.getInstance(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(UIUtils.isLogin()){
                    Intent intent = new Intent(getActivity(), WebDownloadActivity.class);
                    if (position - 1 >= listDataList.size()) {
                        return;
                    }
                    OrderData data1 = listDataList.get(position);
                    intent.putExtra("url", data1.getIntentUrl());
                    intent.putExtra("title",data1.getUserName());
                    intent.putExtra("productId",data1.getProductId());
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(MyApplication.getInstance(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        } );


    }

    private void initListData() {
        adapter = new OrderListAdapter(getActivity(),R.layout.item_order_content,listDataList );
        listView.setAdapter(adapter);
        setListHeight();
    }

    private void getAllLoanMessage(){
        String url = Constants.GET_ALL_LOAN;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .addHeader(Constants.CLIENT_USER_SESSION,UserUtil.getSession())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                String content=response.body().string();
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(content);
                        int yes = object.getInt("status");
                        if(yes==200){
                            listDataList = new ArrayList<OrderData>();
                            JSONObject objectData =object.getJSONObject("data");
                            JSONArray jsonArray=objectData.getJSONArray("list");
                            int lenth=objectData.getInt("total");
                            for (int i = 0; i < 2; i++) {// 最后一个不显示
                                String UserIcon=jsonArray.getJSONObject(i).getString("productLogo");
                                String UserName=jsonArray.getJSONObject(i).getString("productName");
                                String UserQuota= jsonArray.getJSONObject(i).getString("maxAmount");
                                String UserInterest=jsonArray.getJSONObject(i).getString("showRate")+"%";
                                String UserTime=jsonArray.getJSONObject(i).getString("maxPeriod");
                                String UserStage=jsonArray.getJSONObject(i).getString("isQuota");
                                String IntentUrl=jsonArray.getJSONObject(i).getString("linkAddress");
                                String ProductID=jsonArray.getJSONObject(i).getString("id");
                                if(UserStage.equals("0")){
                                    UserStage="不限额";
                                }else if(UserStage.equals("1")){
                                    UserStage="限额";
                                }
                                OrderData data = new OrderData(UserIcon,UserName,UserQuota,UserInterest,UserTime,UserStage,IntentUrl,ProductID);
                                listDataList.add(data);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initListData();
                                    mLoadingPager.showContent();
                                }
                            });
                        }else{
                            MyToast.show(getActivity(), "Network exception, please try again later.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "onResponse: " + content);
            }
        });
    }

    private void setListHeight(){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < 2; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
        } else {// 重新显示到最前端中
            initData();
        }

    }

    private void getMsg(){
            String url = Constants.GET_USER_MSG;
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .addHeader(Constants.CLIENT_USER_SESSION, UserUtil.getSession())
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                    String content=response.body().string();
                    if (response != null) {
                        try {
                            JSONObject object = new JSONObject(content);
                            int yes = object.getInt("status");
                            if(yes==200){
                                JSONObject objectData =object.getJSONObject("data");
                                if((!objectData.getString("infoAuthStatus").isEmpty())&&(!objectData.getString("infoAuthStatus").equals("null"))){
                                   String strInfoAuthStatus=objectData.getString("infoAuthStatus");
                                    if (strInfoAuthStatus.equals("1")){
                                        Intent intent=new Intent(getActivity(), GetLoanActivity.class);
                                        startActivity(intent);
                                    }else{
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                MainActivity mainActivity= (MainActivity) getActivity();
                                                mainActivity.mBottomBarLayout.setCurrentItem(1);
                                            }});
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "onResponse: " + content);
                }
            });
        }
}
