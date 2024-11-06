package com.agrobyte.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.InsumoQuantidade;
import com.agrobyte.app.model.InsumoResponse;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.Produto;
import com.agrobyte.app.model.ProducaoRequest;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;
import com.agrobyte.app.model.ProdutoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Activity para adicionar uma nova produção
public class AddProducaoActivity extends AppCompatActivity {

    private Spinner spinnerProduto;
    private EditText etTempoPlantio, etQuantidadePrevista;
    private Button btnAdicionarInsumo, btnSalvar, btnVoltar;
    private RecyclerView rvInsumos;
    private InsumoQuantidadeAdapter insumoQuantidadeAdapter;
    private List<InsumoQuantidade> insumosList = new ArrayList<>();
    private ApiService apiService;

    private List<Produto> produtosDisponiveis = new ArrayList<>();
    private List<Insumo> insumosDisponiveis = new ArrayList<>(); // Lista global de insumos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producao);

        // Inicializar componentes
        spinnerProduto = findViewById(R.id.spinnerProduto);
        etTempoPlantio = findViewById(R.id.etTempoPlantio);
        etQuantidadePrevista = findViewById(R.id.etQuantidadePrevista);
        btnAdicionarInsumo = findViewById(R.id.btnAdicionarInsumo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvInsumos = findViewById(R.id.rvInsumos);

        // Configurar RecyclerView para Insumos
        insumoQuantidadeAdapter = new InsumoQuantidadeAdapter(insumosList, insumosDisponiveis);
        rvInsumos.setLayoutManager(new LinearLayoutManager(this));
        rvInsumos.setAdapter(insumoQuantidadeAdapter);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Carregar produtos para o Spinner
        carregarProdutos();

        // Carregar insumos disponíveis para utilizar no adaptador
        carregarInsumosDisponiveisGlobal();

        // Configurar ações dos botões
        btnAdicionarInsumo.setOnClickListener(v -> adicionarInsumo());

        btnSalvar.setOnClickListener(v -> salvarNovaProducao());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarProdutos() {
        Call<ProdutoResponse> call = apiService.getProdutos();
        call.enqueue(new Callback<ProdutoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProdutoResponse> call, @NonNull Response<ProdutoResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    produtosDisponiveis = response.body().getContent();
                    // Configurar Adapter para Spinner
                    ProdutoSpinnerAdapter spinnerAdapter = new ProdutoSpinnerAdapter(AddProducaoActivity.this, produtosDisponiveis);
                    spinnerProduto.setAdapter(spinnerAdapter);
                } else {
                    Toast.makeText(AddProducaoActivity.this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProdutoResponse> call, @NonNull Throwable t) {
                Toast.makeText(AddProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarInsumosDisponiveisGlobal() {
        Call<InsumoResponse> call = apiService.getInsumos();
        call.enqueue(new Callback<InsumoResponse>() {
            @Override
            public void onResponse(@NonNull Call<InsumoResponse> call, @NonNull Response<InsumoResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    insumosDisponiveis = response.body().getContent();
                    insumoQuantidadeAdapter.notifyDataSetChanged(); // Atualizar adaptador com insumos disponíveis
                } else {
                    Toast.makeText(AddProducaoActivity.this, "Erro ao carregar insumos disponíveis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<InsumoResponse> call, @NonNull Throwable t) {
                Toast.makeText(AddProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adicionarInsumo() {
        // Adicionar um novo InsumoQuantidade à lista
        insumosList.add(new InsumoQuantidade());
        insumoQuantidadeAdapter.notifyItemInserted(insumosList.size() - 1);
    }

    private void salvarNovaProducao() {
        // Obter dados dos campos
        Produto produtoSelecionado = (Produto) spinnerProduto.getSelectedItem();
        String tempoPlantioStr = etTempoPlantio.getText().toString().trim();
        String quantidadePrevistaStr = etQuantidadePrevista.getText().toString().trim();

        if(produtoSelecionado == null){
            Toast.makeText(this, "Selecione um produto", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tempoPlantioStr.isEmpty() || quantidadePrevistaStr.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int tempoPlantio, quantidadePrevista;
        try {
            tempoPlantio = Integer.parseInt(tempoPlantioStr);
            quantidadePrevista = Integer.parseInt(quantidadePrevistaStr);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se pelo menos um insumo foi adicionado
        if(insumosList.isEmpty()){
            Toast.makeText(this, "Adicione pelo menos um insumo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar insumos
        for(int i = 0; i < insumosList.size(); i++){
            InsumoQuantidade iq = insumosList.get(i);
            if(iq.getId() == 0){
                Toast.makeText(this, "Selecione um insumo válido na posição " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
            if(iq.getQuantidade() <= 0){
                Toast.makeText(this, "Insira uma quantidade válida para o insumo na posição " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Criação do objeto ProducaoRequest
        ProducaoRequest producaoRequest = new ProducaoRequest();
        producaoRequest.setProduto(produtoSelecionado);
        producaoRequest.setTempoPlantio(tempoPlantio);
        producaoRequest.setQuantidadePrevista(quantidadePrevista);
        producaoRequest.setInsumos(insumosList);

        // Enviar requisição para criar produção
        Call<Producao> call = apiService.createProducao(producaoRequest);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(AddProducaoActivity.this, "Produção criada com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddProducaoActivity.this, "Erro ao criar produção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(AddProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
