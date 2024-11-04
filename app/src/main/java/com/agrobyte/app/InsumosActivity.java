package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.InsumoResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsumosActivity extends AppCompatActivity {

    private EditText etSearchId;
    private Button btnSearch, btnAdicionar, btnVoltar;
    private RecyclerView rvInsumos;
    private InsumoAdapter adapter;
    private ApiService apiService;
    private List<Insumo> insumosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insumos);

        etSearchId = findViewById(R.id.etSearchId);
        btnSearch = findViewById(R.id.btnSearch);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvInsumos = findViewById(R.id.rvInsumos);

        apiService = ApiClient.getApiServiceWithAuth(this);

        adapter = new InsumoAdapter(insumosList, this::onInsumoClick);
        rvInsumos.setLayoutManager(new LinearLayoutManager(this));
        rvInsumos.setAdapter(adapter);

        fetchInsumos();

        btnSearch.setOnClickListener(v -> searchInsumoById());
        btnAdicionar.setOnClickListener(v -> openAddInsumoActivity());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void fetchInsumos() {
        Call<InsumoResponse> call = apiService.getInsumos();
        call.enqueue(new Callback<InsumoResponse>() {
            @Override
            public void onResponse(@NonNull Call<InsumoResponse> call, @NonNull Response<InsumoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    insumosList.clear();
                    insumosList.addAll(response.body().getContent());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<InsumoResponse> call, @NonNull Throwable t) {
                // Tratar erro
            }
        });
    }

    private void searchInsumoById() {
        String idStr = etSearchId.getText().toString().trim();
        if (idStr.isEmpty()) {
            fetchInsumos();
            return;
        }

        int id = Integer.parseInt(idStr);
        List<Insumo> filteredList = new ArrayList<>();
        for (Insumo insumo : insumosList) {
            if (insumo.getId() == id) {
                filteredList.add(insumo);
                break;
            }
        }
        adapter.updateList(filteredList);
    }

    private void openAddInsumoActivity() {
        Intent intent = new Intent(InsumosActivity.this, AddInsumosActivity.class);
        startActivity(intent);
    }


    private void onInsumoClick(Insumo insumo) {
        Intent intent = new Intent(InsumosActivity.this, InsumoDetailActivity.class);
        intent.putExtra("insumo_id", insumo.getId());
        startActivity(intent);
    }
}
