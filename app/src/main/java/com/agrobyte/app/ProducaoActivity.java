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

import com.agrobyte.app.R;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.ProducaoResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity principal para exibir a lista de produções.
 */
public class ProducaoActivity extends AppCompatActivity {

    private EditText etSearchName;
    private Button btnSearch, btnIniciarProducao, btnAtualizarStatus;
    private RecyclerView rvProducoes;
    private ProducaoAdapter producaoAdapter;
    private ApiService apiService;
    private List<Producao> producaoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao);

        // Inicializar as views
        etSearchName = findViewById(R.id.etSearchName);
        btnSearch = findViewById(R.id.btnSearch);
        btnIniciarProducao = findViewById(R.id.btnIniciarProducao);
        btnAtualizarStatus = findViewById(R.id.btnAtualizarStatus);
        rvProducoes = findViewById(R.id.rvProducoes);

        // Configurar RecyclerView
        producaoAdapter = new ProducaoAdapter(producaoList, this::openProducaoDetail);
        rvProducoes.setLayoutManager(new LinearLayoutManager(this));
        rvProducoes.setAdapter(producaoAdapter);

        // Inicializar ApiService
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Carregar as produções
        fetchProducoes();

        // Configurar listeners
        btnSearch.setOnClickListener(v -> searchProducaoByName());
        btnIniciarProducao.setOnClickListener(v -> openNovaProducaoActivity());
        btnAtualizarStatus.setOnClickListener(v -> updateStatusProducoes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProducoes(); // Atualiza a lista de produções toda vez que a Activity é retomada
    }

    private void fetchProducoes() {
        Call<ProducaoResponse> call = apiService.getProducoes();
        call.enqueue(new Callback<ProducaoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProducaoResponse> call, @NonNull Response<ProducaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producaoList.clear();
                    producaoList.addAll(response.body().getContent());
                    producaoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao carregar produções", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProducaoResponse> call, @NonNull Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducaoByName() {
        String nameQuery = etSearchName.getText().toString().trim();
        if (nameQuery.isEmpty()) {
            fetchProducoes();
            return;
        }

        List<Producao> filteredList = new ArrayList<>();
        for (Producao producao : producaoList) {
            if (producao.getProduto() != null && producao.getProduto().getNome().toLowerCase().contains(nameQuery.toLowerCase())) {
                filteredList.add(producao);
            }
        }
        producaoAdapter.updateList(filteredList); // Atualiza a lista no adapter
    }

    private void updateStatusProducoes() {
        Call<List<Producao>> call = apiService.updateStatusProducoes();
        call.enqueue(new Callback<List<Producao>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producao>> call, @NonNull Response<List<Producao>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProducaoActivity.this, "Status das produções atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    fetchProducoes();
                } else {
                    Toast.makeText(ProducaoActivity.this, "Erro ao atualizar status das produções", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producao>> call, @NonNull Throwable t) {
                Toast.makeText(ProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openNovaProducaoActivity() {
        Intent intent = new Intent(ProducaoActivity.this, NovaProducaoActivity.class);
        startActivity(intent);
    }

    private void openProducaoDetail(Producao producao) {
        Intent intent = new Intent(ProducaoActivity.this, ProducaoDetailActivity.class);
        intent.putExtra("producao_id", producao.getId());
        startActivity(intent);
    }
}
