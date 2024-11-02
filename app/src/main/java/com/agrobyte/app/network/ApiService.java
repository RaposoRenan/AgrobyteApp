package com.agrobyte.app.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import android.util.Base64;

public interface ApiService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TokenResponse> login(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );
}