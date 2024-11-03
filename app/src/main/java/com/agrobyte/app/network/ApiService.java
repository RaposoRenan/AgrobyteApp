package com.agrobyte.app.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import android.util.Base64;

import com.agrobyte.app.model.Colheita;
import com.agrobyte.app.model.ColheitaResponse;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.Produto;

public interface ApiService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TokenResponse> login(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/colheita")
    Call<ColheitaResponse> getColheitas();

    // Para obter uma colheita específica por ID
    @GET("/colheita/{id}")
    Call<Colheita> getColheitaById(@Path("id") int id);

    // Para obter a produção por ID
    @GET("/producao/{id}")
    Call<Producao> getProducaoById(@Path("id") int id);

    // Para obter o produto por ID
    @GET("/produto/{id}")
    Call<Produto> getProdutoById(@Path("id") int id);

}