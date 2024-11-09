package com.agrobyte.app.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import android.util.Base64;

import com.agrobyte.app.model.Colheita;
import com.agrobyte.app.model.ColheitaRequest;
import com.agrobyte.app.model.ColheitaResponse;
import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.InsumoResponse;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.ProducaoResponse;
import com.agrobyte.app.model.Produto;
import com.agrobyte.app.model.ProdutoResponse;

import java.util.List;

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

    @GET("/insumo")
    Call<InsumoResponse> getInsumos();

    @GET("/insumo/{id}")
    Call<Insumo> getInsumoById(@Path("id") int id);

    @POST("/insumo")
    Call<Insumo> createInsumo(@Body Insumo insumo);

    @PUT("/insumo/{id}")
    Call<Insumo> updateInsumo(@Path("id") int id, @Body Insumo insumo);

    @DELETE("/insumo/{id}")
    Call<Void> deleteInsumo(@Path("id") int id);


    @GET("/produto/{id}")
    Call<Produto> getProdutoById(@Path("id") int id);

    @GET("/produto")
    Call<ProdutoResponse> getProdutos();

    @POST("/produto")
    Call<Produto> createProduto(@Body Produto produto);

    @PUT("/produto/{id}")
    Call<Produto> updateProduto(@Path("id") int id, @Body Produto produto);

    @DELETE("/produto/{id}")
    Call<Void> deleteProduto(@Path("id") int id);

    // Requisição GET para listar produções
    @GET("producao")
    Call<ProducaoResponse> getProducoes();

    // Requisição PUT para atualizar o status das produções
    @PUT("producao/atualizar-status")
    Call<List<Producao>> updateStatusProducoes();

    // Requisição POST para criar uma nova produção
    @POST("producao")
    Call<Producao> createProducao(@Body Producao producao);

    // Requisição PUT para atualizar uma produção existente
    @PUT("producao/{id}")
    Call<Producao> updateProducao(@Path("id") int id, @Body Producao producao);

    // Requisição DELETE para excluir uma produção
    @DELETE("producao/{id}")
    Call<Void> deleteProducao(@Path("id") int id);

    // Requisição POST para realizar colheita
    @POST("colheita/realizar")
    Call<Void> realizarColheita(@Body ColheitaRequest colheitaRequest);

    // Requisição GET para listar produtos
    @GET("produto")
    Call<List<Produto>> getListaProdutos();

    // Requisição GET para listar insumos
    @GET("insumo")
    Call<List<Insumo>> getListaInsumos();

}