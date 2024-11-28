package com.example.tp4hembert;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    public static void appUnavailable(Context context){
        Toast.makeText(context, R.string.error_app_unavailable, Toast.LENGTH_LONG).show();
    }

    public static void showError(Context context, String error){
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }
}
