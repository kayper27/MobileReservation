package com.example.mobilereservation.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    /**
     * Storing API Key in shared preferences to
     * add it in header part of every retrofit request
     */
    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }

    public static String getApiKey(Context context) {
        return getSharedPreferences(context).getString("API_KEY", null);
    }

    public static void storeUserLogID(Context context, String userid) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCOUNT_ID", userid);
        editor.commit();
    }

    public static String getUserLogID(Context context) {
        return getSharedPreferences(context).getString("ACCOUNT_ID", null);
    }

    public static void storeUserLogType(Context context, String usertype) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCOUNT_TYPE", usertype);
        editor.commit();
    }

    public static String getUserLogType(Context context) {
        return getSharedPreferences(context).getString("ACCOUNT_TYPE", null);
    }

}