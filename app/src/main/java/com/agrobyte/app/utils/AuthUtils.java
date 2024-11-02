package com.agrobyte.app.utils;

import android.util.Base64;

public class AuthUtils {
    public static String getBasicAuthHeader(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}
