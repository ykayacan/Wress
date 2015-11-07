package com.aurora.wress.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duygu on 26.10.2015.
 * <p/>
 * See http://stackoverflow.com/a/16902532/814353
 * <p/>
 * We must cache the TypeFace, otherwise memory leak may occur.
 */
public class FontCache {

    private static Map<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }

}
