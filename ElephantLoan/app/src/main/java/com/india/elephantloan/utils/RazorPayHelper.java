package com.india.elephantloan.utils;

import android.app.Activity;
import android.content.Context;

import com.india.elephantloan.constant.Constants;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class RazorPayHelper {

    public static void init(Context context) {
        Checkout.preload(context);
    }

    public static void startPayment(Activity activity, String orderId, String amount, String email, String contact) {
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", Constants.APP_NAME);
            options.put("description", Constants.RAZORPAY_NAME);
            options.put("order_id", orderId);//from response of step 3.
            options.put("image", Constants.IMAGE);
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("theme.color", "#39B54A");

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", contact);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
