package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsumoDetailActivity extends AppCompatActivity {

    private TextView tvId, tvNome, tvQuantidade, tvValorUnitario, tvDataValidade;
    private Button btnEditar, btnExcluir, btnVoltar;
    private ApiService apiService;
    private int insumoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insumo_detail);

        // Inicializar componentes
        tvId = findViewById(R.id.tvId);
        tvNome = findViewById(R.id.tvNome);
        tvQuantidade = findViewById(R.id.tvQuantidade);
        tvValorUnitario = findViewById(R.id.tvValorUnitario);
        tvDataValidade = findViewById(R.id.tvDataValidade);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);

        apiService = ApiClient.getApiServiceWithAuth(this);

        // Obter o ID do insumo selecionado
        insumoId = getIntent().getIntExtra("insumo_id", -1);

        // Verificar se o ID é válido e carregar os detalhes do insumo
        if (insumoId != -1) {
            fetchInsumoDetails();
        } else {
            Toast.makeText(this, "ID de insumo inválido", Toast.LENGTH_SHORT).show();
            finish(); // Fechar a activity caso o ID seja inválido
        }

        // Configurar ações dos botões
        btnEditar.setOnClickListener(v -> editInsumo());
        btnExcluir.setOnClickListener(v -> deleteInsumo());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void fetchInsumoDetails() {
        Call<Insumo> call = apiService.getInsumoById(insumoId);
        call.enqueue(new Callback<Insumo>() {
            @Override
            public void onResponse(@NonNull Call<Insumo> call, @NonNull Response<Insumo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Insumo insumo = response.body();
                    displayInsumoDetails(insumo);
                } else {
                    Toast.makeText(InsumoDetailActivity.this, "Erro ao carregar insumo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Insumo> call, @NonNull Throwable t) {
                Toast.makeText(InsumoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInsumoDetails(Insumo insumo) {
        tvId.setText("ID: " + insumo.getId());
        tvNome.setText("Nome: " + insumo.getNome());
        tvQuantidade.setText("Quantidade: " + insumo.getQuantidadeEstoque());
        tvValorUnitario.setText("Valor Unitário: " + insumo.getValorUnitario());
        tvDataValidade.setText("Data de Validade: " + insumo.getDataValidade());
    }

    private void editInsumo() {
        Intent intent = new Intent(InsumoDetailActivity.this, EditInsumoActivity.class);
        intent.putExtra("insumo_id", insumoId);
        startActivity(intent);
    }

    private void deleteInsumo() {
        Call<Void> call = apiService.deleteInsumo(insumoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InsumoDetailActivity.this, "Insumo excluído com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Volta para a lista de insumos
                } else {
                    Toast.makeText(InsumoDetailActivity.this, "Falha ao excluir insumo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(InsumoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
