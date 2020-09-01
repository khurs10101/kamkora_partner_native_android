package com.khurshid.kamkorapartner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.khurshid.kamkorapartner.model.Partner;

public class SessionManager {
    public static final String USERID = "userid";
    public static final String TOKEN = "token";
    public static final String AUTH = "auth";
    public static final String USEROBJECT = "userObject";

    private static Context context;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void startSession(Context context, Partner partner, String token) {
        Gson gson = new Gson();
        String userJson = gson.toJson(partner, Partner.class);
        SessionManager.context = context;
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear().commit();
        editor.putString(SessionManager.USERID, partner.getId());
        editor.putString(SessionManager.TOKEN, token);
        editor.putString(SessionManager.USEROBJECT, userJson);
        editor.commit();

    }

    public static void endSession() {
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear().commit();
    }

    public static boolean isLoggedIn(Context context) {
        SessionManager.context = context;
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String id = sharedPreferences.getString(SessionManager.USERID, null);
            if (id != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String getLoggedInUserId(Context context) {
        SessionManager.context = context;
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String id = sharedPreferences.getString(SessionManager.USERID, null);
            return id;
        }

        return null;
    }

    public static String getToken(Context context) {
        SessionManager.context = context;
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String token = sharedPreferences.getString(SessionManager.TOKEN, null);
            return token;
        }

        return null;
    }

    public static String getLoggedInUserObject(Context context) {
        sharedPreferences = context.getSharedPreferences(SessionManager.AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(SessionManager.USEROBJECT, null);
        }
        return null;
    }
}
