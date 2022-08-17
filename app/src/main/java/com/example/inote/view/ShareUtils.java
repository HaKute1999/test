package com.example.inote.view;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtils {

    private static SharedPreferences prefs;

    public final static  String PASSCODE = "PASSCODE";
    public final static  String CONFIG_DARK = "CONFIG_DARK";
    public final static  String VIEW_AS_GALLERY = "VIEW_AS_GALLERY";
    public final static  String CONFIG_SIZE_IMAGE = "CONFIG_SIZE_IMAGE";
    public final static  String CHECK_LIST = "CHECK_LIST" +
            "" +
            "" +
            "" +
            "";
    //direction

    public static boolean isPreferencesSet(Context context){
        int exactNumberOfPreferences = 0;
        return (prefs.getAll().size() == exactNumberOfPreferences);
    }
    public ShareUtils(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    public final static String PREFS_NAME = "inote";

    public static boolean checkExist( String key)
    {
        if(prefs.contains(key)){
            return true;
        }
        else {
            return false;
        }
    }
    public static void removeKey(String key){
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void setInt( String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public  static int getInt(String key,int value) {
        return prefs.getInt(key, value);
    }

    public static void setFloat( String key, float value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public  static float getFloat(String key,float value) {
        return prefs.getFloat(key, value);
    }

    public static void setStr(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStr(String key,String defaut) {
        return prefs.getString(key,defaut);
    }

    public static void setBool(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(String key) {
        return prefs.getBoolean(key,false);
    }
    public static void setLong( String key, long value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String key,long value) {
        return prefs.getLong(key, value) ;
    }

}