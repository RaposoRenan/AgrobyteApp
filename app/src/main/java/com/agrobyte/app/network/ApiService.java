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

public interface ApiService {
    // Defina os endpoints da API aqui
    // Por exemplo:
    @GET("{8080}/insumo/2")
     InsumoById(@Path("8080") String 8080);

    @GET("{8080}/insumo")
     Insumo(@Path("8080") String 8080);

    @POST("{8080}/insumo")
     NewInsumo(@Path("8080") String 8080, @Body String body);

    @PUT("{8080}/insumo/11")
     Insumo(@Path("8080") String 8080, @Body String body);

    @DELETE("{8080}/insumo/11")
     InsumoById(@Path("8080") String 8080);

    @GET("{8080}/produto/2")
     ProdutoById(@Path("8080") String 8080);

    @GET("{8080}/produto")
     Produto(@Path("8080") String 8080);

    @POST("{8080}/produto")
     NewProduto(@Path("8080") String 8080, @Body String body);

    @PUT("{8080}/produto/2")
     Produto(@Path("8080") String 8080, @Body String body);

    @DELETE("{8080}/produto/1")
     ProdutoById(@Path("8080") String 8080);

    @GET("{8080}/producao/10")
     ProducaoById(@Path("8080") String 8080);

    @DELETE("{8080}/producao/4")
     Producao(@Path("8080") String 8080);

    @POST("{8080}/producao")
     Producao(@Path("8080") String 8080, @Body String body);

    @PUT("{8080}/producao/11")
     Producoes(@Path("8080") String 8080, @Body String body);

    @PUT("{8080}/producao/atualizar-status")
     AtualizarStatus(@Path("8080") String 8080, @Body String body);

    @GET("{8080}/producao")
     Producao(@Path("8080") String 8080);

    @POST("{8080}/colheita/realizar")
     RealizarColheita(@Path("8080") String 8080, @Body String body);

    @FormUrlEncoded
    @POST("{{ashost}}/oauth2/token")
     Login(@Path("ashost") String ashost, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);



}
