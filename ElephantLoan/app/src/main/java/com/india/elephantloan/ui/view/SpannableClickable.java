package com.india.elephantloan.ui.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.india.elephantloan.R;
import com.india.elephantloan.utils.UIUtils;


/**
 * Created by zhangzc on 2017/7/24.
 */

public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_8290AF;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = UIUtils.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
