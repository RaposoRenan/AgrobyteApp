package com.agrobyte.app;

import android.os.Bundle;
import android.view.View;
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
import com.agrobyte.app.model.Produto;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.ProducaoRequest;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProducaoActivity extends AppCompatActivity {

    private Spinner spinnerProduto;
    private EditText etTempoPlantio, etQuantidadePrevista;
    private Button btnAdicionarInsumo, btnSalvar, btnVoltar;
    private RecyclerView rvInsumos;
    private InsumoQuantidadeAdapter insumoAdapter;
    private List<InsumoQuantidade> insumosList = new ArrayList<>();
    private ApiService apiService;

    private int producaoId;
    private Producao producao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producao); // Reutilizar o layout

        // Inicializar componentes
        spinnerProduto = findViewById(R.id.spinnerProduto);
        etTempoPlantio = findViewById(R.id.etTempoPlantio);
        etQuantidadePrevista = findViewById(R.id.etQuantidadePrevista);
        btnAdicionarInsumo = findViewById(R.id.btnAdicionarInsumo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvInsumos = findViewById(R.id.rvInsumos);

        // Configurar RecyclerView para Insumos
        insumoAdapter = new InsumoQuantidadeAdapter(insumosList);
        rvInsumos.setLayoutManager(new LinearLayoutManager(this));
        rvInsumos.setAdapter(insumoAdapter);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Obter o ID da produção a ser editada
        producaoId = getIntent().getIntExtra("producao_id", -1);

        if (producaoId != -1) {
            fetchProducaoDetails();
        } else {
            Toast.makeText(this, "ID de produção inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar ações dos botões
        btnAdicionarInsumo.setOnClickListener(v -> adicionarInsumo());

        btnSalvar.setOnClickListener(v -> salvarAlteracoesProducao());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void fetchProducaoDetails() {
        Call<Producao> call = apiService.getProducaoById(producaoId);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producao = response.body();
                    populateFields(producao);
                } else {
                    Toast.makeText(EditProducaoActivity.this, "Erro ao carregar produção", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(EditProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateFields(Producao producao) {
        // Preencher os campos com os dados da produção
        // Implementar similar à `AddProducaoActivity`
        // Preencher o Spinner de Produto
        // Preencher os campos de tempo plantio e quantidade prevista
        // Preencher a lista de insumos
    }

    private void salvarAlteracoesProducao() {
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

        // Obter insumos
        // Implementar conforme a lógica de adicionar insumos na produção

        // Criação do objeto ProducaoRequest
        ProducaoRequest producaoRequest = new ProducaoRequest();
        producaoRequest.setProduto(produtoSelecionado);
        producaoRequest.setTempoPlantio(tempoPlantio);
        producaoRequest.setQuantidadePrevista(quantidadePrevista);
        producaoRequest.setInsumos(insumosList);

        // Enviar requisição para atualizar produção
        Call<Producao> call = apiService.updateProducao(producaoId, producaoRequest);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(EditProducaoActivity.this, "Produção atualizada com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditProducaoActivity.this, "Erro ao atualizar produção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(EditProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adicionarInsumo() {
        // Implementar a lógica para adicionar insumos semelhante à `AddProducaoActivity`
    }
    private void populateFields(Producao producao) {
        // Preencher o Spinner de Produto
        ProdutoSpinnerAdapter spinnerAdapter = new ProdutoSpinnerAdapter(this, produtosDisponiveis);
        spinnerProduto.setAdapter(spinnerAdapter);

        if (producao.getProduto() != null) {
            for (int i = 0; i < produtosDisponiveis.size(); i++) {
                if (produtosDisponiveis.get(i).getId() == producao.getProduto().getId()) {
                    spinnerProduto.setSelection(i);
                    break;
                }
            }
        }

        // Preencher os campos de tempo plantio e quantidade prevista
        etTempoPlantio.setText(String.valueOf(producao.getTempoPlantio()));
        etQuantidadePrevista.setText(String.valueOf(producao.getQuantidadePrevista()));

        // Preencher a lista de insumos
        List<InsumoQuantidade> insumosExistentes = new ArrayList<>();
        for (Insumo insumo : producao.getInsumos()) {
            InsumoQuantidade iq = new InsumoQuantidade();
            iq.setId(insumo.getId());
            iq.setQuantidade(insumo.getQuantidade());
            insumosExistentes.add(iq);
        }

        insumosList.clear();
        insumosList.addAll(insumosExistentes);
        insumoQuantidadeAdapter.notifyDataSetChanged();
    }

}
