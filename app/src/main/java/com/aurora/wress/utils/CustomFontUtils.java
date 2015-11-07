package com.aurora.wress.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aurora.wress.R;

public class CustomFontUtils {

    public static void applyCustomFont(TextView customFontTextView,
                                       Context context, AttributeSet attrs) {
        TypedArray attributeArray =
                context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_font);

        Typeface customFont = FontCache.getTypeface(fontName, context);
        customFontTextView.setTypeface(customFont);

        attributeArray.recycle();
    }

}
