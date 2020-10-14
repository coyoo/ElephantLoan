package com.india.elephantloan.utils;

import android.content.Context;


import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;

public class BranchEventHelper {

    /**
     * branch事件监听
     *
     * @param event branch事件
     * @param des   事件描述
     */
    public static void obtainBranchEvent(Context context, BRANCH_STANDARD_EVENT event, String des) {
        new BranchEvent(event).setDescription(des).logEvent(context);
    }

    public static void obtainBranchEvent(Context context, BRANCH_STANDARD_EVENT event) {
        new BranchEvent(event).logEvent(context);
    }

    /**
     * 自定义branch事件
     *
     * @param context
     * @param event     自定义事件
     * @param customDes 自定义事件描述
     */
    public static void obtainBranchEvent(Context context, String event, String customDes) {
        new BranchEvent(event).setCustomerEventAlias(customDes).logEvent(context);
    }
}
