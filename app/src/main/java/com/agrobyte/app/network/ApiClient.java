package com.agrobyte.app.network;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Atualize conforme necessário

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {
            // Configurar o interceptador para adicionar o token de autenticação
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            // Adicionar logging para depuração
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logging);

            // Adicionar interceptador para autenticação, se necessário
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Obter o token armazenado (implemente conforme sua lógica)
                    String token = getToken(context);

                    // Adicionar o token no header, se disponível
                    if (token != null && !token.isEmpty()) {
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }

                    return chain.proceed(original);
                }
            });

            OkHttpClient client = clientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit.create(ApiService.class);
    }

    // Implementação fictícia para obter o token
    private static String getToken(Context context) {
        // Implemente a lógica para obter o token armazenado, por exemplo, SharedPreferences
        return "SEU_TOKEN_AQUI"; // Substitua pelo token real
    }

    // Se você precisa obter o ApiService sem autenticação (por exemplo, para login)
    public static ApiService getApiServiceWithoutAuth() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
