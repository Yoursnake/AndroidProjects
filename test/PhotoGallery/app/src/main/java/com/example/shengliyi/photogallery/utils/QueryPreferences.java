package com.example.shengliyi.photogallery.utils;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by shengliyi on 2017/5/26.
 */

public class QueryPreferences {

    private static final String SEARCH_QUERY = "searchQuery";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static String getSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SEARCH_QUERY, null);
    }

    public static void setSearchQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SEARCH_QUERY, query)
                .apply();
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
}
