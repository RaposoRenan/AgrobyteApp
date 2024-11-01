package com.agrobyte.app.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/oauth2/token") // Ajuste o endpoint conforme sua API
    Call<TokenResponse> login(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password,
            @Field("scope") String scope,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}
