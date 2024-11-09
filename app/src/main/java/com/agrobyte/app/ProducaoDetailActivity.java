package com.agrobyte.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.ColheitaRequest;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducaoDetailActivity extends AppCompatActivity {

    private TextView tvId, tvDataEntrada, tvTempoPlantio, tvQuantidadePrevista, tvStatusProducao, tvProdutoNome, tvInsumos;
    private Button btnRealizarColheita, btnEditarProducao, btnExcluirProducao;
    private ApiService apiService;
    private int producaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_producao);

        // Inicializar views
        tvId = findViewById(R.id.tvId);  // Adicione um TextView com ID tvId no layout activity_detalhes_producao.xml
        tvDataEntrada = findViewById(R.id.tvDataEntrada);
        tvTempoPlantio = findViewById(R.id.tvTempoPlantio);
        tvQuantidadePrevista = findViewById(R.id.tvQuantidadePrevista);
        tvStatusProducao = findViewById(R.id.tvStatusProducao);
        tvProdutoNome = findViewById(R.id.tvProdutoNome);
        tvInsumos = findViewById(R.id.tvInsumos);
        btnRealizarColheita = findViewById(R.id.btnRealizarColheita);
        btnEditarProducao = findViewById(R.id.btnEditarProducao);
        btnExcluirProducao = findViewById(R.id.btnExcluirProducao);

        apiService = ApiClient.getApiServiceWithAuth(this);

        // Obter o ID da produção selecionada
        producaoId = getIntent().getIntExtra("producao_id", -1);

        if (producaoId != -1) {
            loadProducaoDetails(producaoId);
        } else {
            Toast.makeText(this, "ID de produção inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar listeners
        btnRealizarColheita.setOnClickListener(v -> performHarvest());
        btnEditarProducao.setOnClickListener(v -> openEditProducaoActivity());
        btnExcluirProducao.setOnClickListener(v -> deleteProducao());
    }

    private void loadProducaoDetails(int id) {
        Call<Producao> call = apiService.getProducaoById(id);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Producao producao = response.body();
                    displayProducaoDetails(producao);
                } else {
                    Toast.makeText(ProducaoDetailActivity.this, "Erro ao carregar produção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(ProducaoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProducaoDetails(Producao producao) {
        tvId.setText("ID: " + producao.getId());
        tvDataEntrada.setText("Data Entrada: " + producao.getDataEntrada());
        tvTempoPlantio.setText("Tempo Plantio: " + producao.getTempoPlantio());
        tvQuantidadePrevista.setText("Quantidade Prevista: " + producao.getQuantidadePrevista());
        tvStatusProducao.setText("Status: " + producao.getStatus());
        tvProdutoNome.setText("Produto: " + (producao.getProduto() != null ? producao.getProduto().getNome() : "N/A"));

        StringBuilder insumosStr = new StringBuilder();
        if (producao.getInsumos() != null) {
            for (int i = 0; i < producao.getInsumos().size(); i++) {
                insumosStr.append(producao.getInsumos().get(i).getNome())
                        .append(" - Quantidade: ")
                        .append(producao.getInsumos().get(i).getQuantidade())
                        .append("\n");
            }
        }
        tvInsumos.setText(insumosStr.toString());
    }

    private void performHarvest() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_realizar_colheita, null);
        EditText etPerdaDoenca = dialogView.findViewById(R.id.etPerdaDoenca);
        EditText etPerdaErro = dialogView.findViewById(R.id.etPerdaErro);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Realizar Colheita");
        builder.setView(dialogView);
        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            try {
                int perdaDoenca = Integer.parseInt(etPerdaDoenca.getText().toString());
                int perdaErro = Integer.parseInt(etPerdaErro.getText().toString());

                ColheitaRequest colheitaRequest = new ColheitaRequest(producaoId, perdaDoenca, perdaErro);

                // Realizar chamada da API para colheita
                Call<Void> call = apiService.realizarColheita(colheitaRequest);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProducaoDetailActivity.this, "Colheita realizada com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProducaoDetailActivity.this, "Erro ao realizar colheita", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(ProducaoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(ProducaoDetailActivity.this, "Insira valores válidos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void openEditProducaoActivity() {
        Intent intent = new Intent(ProducaoDetailActivity.this, NovaProducaoActivity.class);
        intent.putExtra("producao_id", producaoId);
        startActivity(intent);
    }

    private void deleteProducao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Produção");
        builder.setMessage("Tem certeza que deseja excluir esta produção?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            Call<Void> call = apiService.deleteProducao(producaoId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProducaoDetailActivity.this, "Produção excluída com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ProducaoDetailActivity.this, "Erro ao excluir produção", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(ProducaoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Não", null);
        builder.show();
    }
}
