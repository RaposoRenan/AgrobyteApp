package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.ProducaoResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducoesActivity extends AppCompatActivity {

    private EditText etSearchId;
    private Button btnSearch, btnAtualizarStatus, btnIniciarProducao;
    private RecyclerView rvProducoes;
    private ProducaoAdapter adapter;
    private ApiService apiService;
    private List<Producao> producoesList = new ArrayList<>();

    private static final int REQUEST_CODE_INICIAR_PRODUCAO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producoes);

        // Inicializar componentes
        etSearchId = findViewById(R.id.etSearchId);
        btnSearch = findViewById(R.id.btnSearch);
        btnAtualizarStatus = findViewById(R.id.btnAtualizarStatus);
        btnIniciarProducao = findViewById(R.id.btnIniciarProducao);
        rvProducoes = findViewById(R.id.rvProducoes);

        // Configurar RecyclerView
        adapter = new ProducaoAdapter(producoesList, this::onProducaoClick);
        rvProducoes.setLayoutManager(new LinearLayoutManager(this));
        rvProducoes.setAdapter(adapter);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Carregar todas as produções inicialmente
        fetchProducoes();

        // Configurar ações dos botões
        btnSearch.setOnClickListener(v -> searchProducaoById());

        btnAtualizarStatus.setOnClickListener(v -> atualizarStatusProducoes());

        btnIniciarProducao.setOnClickListener(v -> abrirAddProducaoActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProducoes(); // Atualiza a lista sempre que retorna para esta tela
    }

    private void fetchProducoes() {
        Call<ProducaoResponse> call = apiService.getProducoes();
        call.enqueue(new Callback<ProducaoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProducaoResponse> call, @NonNull Response<ProducaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producoesList.clear();
                    producoesList.addAll(response.body().getContent());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProducoesActivity.this, "Erro ao carregar produções", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProducaoResponse> call, @NonNull Throwable t) {
                Toast.makeText(ProducoesActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducaoById() {
        String idStr = etSearchId.getText().toString().trim();
        if (idStr.isEmpty()) {
            fetchProducoes();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar produção por ID
        Call<Producao> call = apiService.getProducaoById(id);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producao> filteredList = new ArrayList<>();
                    filteredList.add(response.body());
                    adapter.updateList(filteredList);
                } else {
                    Toast.makeText(ProducoesActivity.this, "Produção não encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(ProducoesActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarStatusProducoes() {
        Call<List<Producao>> call = apiService.atualizarStatusProducoes();
        call.enqueue(new Callback<List<Producao>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producao>> call, @NonNull Response<List<Producao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producoesList.clear();
                    producoesList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ProducoesActivity.this, "Status das produções atualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProducoesActivity.this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producao>> call, @NonNull Throwable t) {
                Toast.makeText(ProducoesActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirAddProducaoActivity() {
        Intent intent = new Intent(ProducoesActivity.this, AddProducaoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INICIAR_PRODUCAO);
    }

    private void onProducaoClick(Producao producao) {
        Intent intent = new Intent(ProducoesActivity.this, ProducaoDetailActivity.class);
        intent.putExtra("producao_id", producao.getId());
        startActivity(intent);
    }

    // Manejar resultado da atividade de adicionar produção
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INICIAR_PRODUCAO && resultCode == RESULT_OK){
            fetchProducoes(); // Atualiza a lista após adicionar uma produção
        }
    }
}
