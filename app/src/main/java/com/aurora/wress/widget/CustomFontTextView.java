package com.aurora.wress.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aurora.wress.utils.CustomFontUtils;

/**
 * Created by duygu on 26.10.2015.
 */
public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context) {
        super(context);

        CustomFontUtils.applyCustomFont(this, context, null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

}
