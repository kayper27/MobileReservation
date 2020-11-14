package com.example.mobilereservation.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobilereservation.model.LoggedInUser;

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

    public static void storeUserLogID(Context context, LoggedInUser user) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCOUNT_ID", user.getAccount_id());
        editor.commit();
    }

    public static String getUserLogID(Context context) {
        return getSharedPreferences(context).getString("ACCOUNT_ID", null);
    }

    public static void storeUserLogType(Context context, LoggedInUser user) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCOUNT_TYPE", user.getAccount_type());
        editor.commit();
    }

    public static String getUserLogType(Context context) {
        return getSharedPreferences(context).getString("ACCOUNT_ID", null);
    }

}