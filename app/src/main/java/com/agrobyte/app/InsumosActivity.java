package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insumos);

        // Inicialização das views
        etSearchId = findViewById(R.id.etSearchId);
        btnSearch = findViewById(R.id.btnSearch);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvInsumos = findViewById(R.id.rvInsumos);

        // Configuração do ApiService
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Configuração do RecyclerView e Adapter
        adapter = new InsumoAdapter(insumosList, this::onInsumoClick);
        rvInsumos.setLayoutManager(new LinearLayoutManager(this));
        rvInsumos.setAdapter(adapter);

        // Carregar a lista inicial de insumos
        fetchInsumos();

        // Configuração dos listeners de botão
        btnSearch.setOnClickListener(v -> searchInsumoById());
        btnAdicionar.setOnClickListener(v -> openAddInsumoActivity());
        btnVoltar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchInsumos(); // Atualiza a lista sempre que retorna para esta tela
    }

    private void fetchInsumos() {
        // Chamando a API para buscar a lista de insumos
        Call<InsumoResponse> call = apiService.getInsumos(); // Utilizando InsumoResponse
        call.enqueue(new Callback<InsumoResponse>() {
            @Override
            public void onResponse(Call<InsumoResponse> call, Response<InsumoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    insumosList.clear();
                    insumosList.addAll(response.body().getContent()); // Pega a lista de insumos do InsumoResponse
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InsumosActivity.this, "Erro ao carregar insumos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsumoResponse> call, Throwable t) {
                Toast.makeText(InsumosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchInsumoById() {
        String idStr = etSearchId.getText().toString().trim();
        if (idStr.isEmpty()) {
            fetchInsumos();
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            List<Insumo> filteredList = new ArrayList<>();
            for (Insumo insumo : insumosList) {
                if (insumo.getId() == id) {
                    filteredList.add(insumo);
                    break;
                }
            }
            adapter.updateList(filteredList);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAddInsumoActivity() {
        Intent intent = new Intent(InsumosActivity.this, AddInsumosActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private void onInsumoClick(Insumo insumo) {
        Intent intent = new Intent(InsumosActivity.this, InsumoDetailActivity.class);
        intent.putExtra("insumo_id", insumo.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT) {
                fetchInsumos(); // Recarrega a lista após adicionar ou editar um insumo
            }
        }
    }
}
