package com.aurora.wress.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import com.aurora.wress.utils.CustomFontUtils;

public class CustomFontButton extends AppCompatButton {

    public CustomFontButton(Context context) {
        super(context);

        CustomFontUtils.applyCustomFont(this, context, null);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }
}
