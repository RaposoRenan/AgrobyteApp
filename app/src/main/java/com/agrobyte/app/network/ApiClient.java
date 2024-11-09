package com.agrobyte.app.network;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static ApiService apiServiceWithoutAuth;  // Para login
    private static ApiService apiServiceWithAuth;     // Para uso após o login

    // Retorna o serviço de API sem AuthInterceptor (para a requisição de login)
    public static ApiService getApiServiceWithoutAuth() {
        if (apiServiceWithoutAuth == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") // Ajuste a URL para o seu caso
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            apiServiceWithoutAuth = retrofit.create(ApiService.class);
        }
        return apiServiceWithoutAuth;
    }

    // Retorna o serviço de API com AuthInterceptor (para uso após o login)
    public static ApiService getApiServiceWithAuth(Context context) {
        if (apiServiceWithAuth == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            apiServiceWithAuth = retrofit.create(ApiService.class);
        }
        return apiServiceWithAuth;
    }
}