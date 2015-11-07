package com.aurora.wress.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import com.aurora.wress.utils.CustomFontUtils;

/**
 * Created by duygu on 27.10.2015.
 */
public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context) {
        super(context);

        CustomFontUtils.applyCustomFont(this, context, null);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

}
