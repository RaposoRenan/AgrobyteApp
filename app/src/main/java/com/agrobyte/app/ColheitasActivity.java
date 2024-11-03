package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Colheita;
import com.agrobyte.app.model.ColheitaResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColheitasActivity extends AppCompatActivity {

    private EditText etSearchId;
    private Button btnSearch, btnVoltar;
    private RecyclerView rvColheitas;
    private ColheitaAdapter adapter;
    private ApiService apiService;

    private List<Colheita> colheitasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_colheitas);

        // Inicializar componentes
        etSearchId = findViewById(R.id.etSearchId);
        btnSearch = findViewById(R.id.btnSearch);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvColheitas = findViewById(R.id.rvColheitas);

        // Configurar RecyclerView
        adapter = new ColheitaAdapter(colheitasList, this::onColheitaClick);
        rvColheitas.setLayoutManager(new LinearLayoutManager(this));
        rvColheitas.setAdapter(adapter);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Carregar todas as colheitas inicialmente
        fetchColheitas();

        // Configurar ações dos botões
        btnSearch.setOnClickListener(v -> searchColheitaById());

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(ColheitasActivity.this, MenuActivity.class));
            finish();
        });
    }

    private void fetchColheitas() {
        Call<ColheitaResponse> call = apiService.getColheitas();
        call.enqueue(new Callback<ColheitaResponse>() {
            @Override
            public void onResponse(@NonNull Call<ColheitaResponse> call, @NonNull Response<ColheitaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    colheitasList.clear();
                    colheitasList.addAll(response.body().getContent());
                    adapter.notifyDataSetChanged();
                } else {
                    // Tratar erro
                }
            }

            @Override
            public void onFailure(@NonNull Call<ColheitaResponse> call, @NonNull Throwable t) {
                // Tratar falha
            }
        });
    }

    private void searchColheitaById() {
        String idStr = etSearchId.getText().toString().trim();
        if (idStr.isEmpty()) {
            fetchColheitas();
            return;
        }
        int id = Integer.parseInt(idStr);

        // Filtrar a lista de colheitas
        List<Colheita> filteredList = new ArrayList<>();
        for (Colheita colheita : colheitasList) {
            if (colheita.getId() == id) {
                filteredList.add(colheita);
                break;
            }
        }
        adapter.updateList(filteredList);
    }

    private void onColheitaClick(Colheita colheita) {
        Intent intent = new Intent(ColheitasActivity.this, ColheitaDetailActivity.class);
        intent.putExtra("colheita_id", colheita.getId());
        startActivity(intent);
    }
}
