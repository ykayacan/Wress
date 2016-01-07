package com.aurora.wress.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by duygu on 28.10.2015.
 */
public class PrefUtil {

    /**
     * Boolean indicating whether ToS has been accepted
     */
    public static final String PREF_TOS_ACCEPTED = "pref_tos_accepted";

    /**
     * Is tos accepted boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }

    /**
     * Mark tos accepted.
     *
     * @param context the context
     */
    public static void markTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, true).apply();
    }

}
