package com.mambure.newsAssistant.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import androidx.core.net.ConnectivityManagerCompat;

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = context.getSystemService(ConnectivityManager.class);
         return (cm != null ? cm.getActiveNetwork() : null) != null;
    }
}
