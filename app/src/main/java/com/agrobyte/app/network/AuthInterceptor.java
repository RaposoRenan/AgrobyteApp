package com.agrobyte.app.network;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = preferences.getString("access_token", null);

        Request request = chain.request();

        if (token != null && !token.isEmpty()) {
            Request.Builder builder = request.newBuilder()
                    .header("Authorization", "Bearer " + token);
            request = builder.build();
        }

        return chain.proceed(request);
    }
}
