package com.example.inote.view;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigUtils {
    public static void hideKeyboard(Activity activity) {

        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
        }

    }
    public static String formatDateTIme(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        Date resultdate = new Date(time);
        return sdf.format(resultdate);
    }
}
