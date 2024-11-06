package com.agrobyte.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducaoDetailActivity extends AppCompatActivity {

    private TextView tvId, tvDataEntrada, tvTempoPlantio, tvQuantidadePrevista, tvStatus, tvProduto;
    private Button btnRealizarColheita, btnEditar, btnExcluir, btnVoltar;
    private ApiService apiService;
    private int producaoId;
    private Producao producao;

    private RecyclerView rvInsumosProducao;
    private InsumoProducaoAdapter insumoProducaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao_detail);

        // Inicializar componentes
        tvId = findViewById(R.id.tvId);
        tvDataEntrada = findViewById(R.id.tvDataEntrada);
        tvTempoPlantio = findViewById(R.id.tvTempoPlantio);
        tvQuantidadePrevista = findViewById(R.id.tvQuantidadePrevista);
        tvStatus = findViewById(R.id.tvStatus);
        tvProduto = findViewById(R.id.tvProduto);

        btnRealizarColheita = findViewById(R.id.btnRealizarColheita);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);

        rvInsumosProducao = findViewById(R.id.rvInsumosProducao);

        // Configurar RecyclerView para Insumos
        insumoProducaoAdapter = new InsumoProducaoAdapter(new ArrayList<>());
        rvInsumosProducao.setLayoutManager(new LinearLayoutManager(this));
        rvInsumosProducao.setAdapter(insumoProducaoAdapter);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Obter o ID da produção selecionada
        producaoId = getIntent().getIntExtra("producao_id", -1);

        // Verificar se o ID é válido e carregar os detalhes do insumo
        if (producaoId != -1) {
            fetchProducaoDetails();
        } else {
            Toast.makeText(this, "ID de produção inválido", Toast.LENGTH_SHORT).show();
            finish(); // Fechar a activity caso o ID seja inválido
        }

        // Configurar ações dos botões
        btnRealizarColheita.setOnClickListener(v -> realizarColheita());

        btnEditar.setOnClickListener(v -> editarProducao());

        btnExcluir.setOnClickListener(v -> confirmarExcluirProducao());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void fetchProducaoDetails() {
        Call<Producao> call = apiService.getProducaoById(producaoId);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producao = response.body();
                    displayProducaoDetails(producao);
                } else {
                    Toast.makeText(ProducaoDetailActivity.this, "Erro ao carregar produção", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(ProducaoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayProducaoDetails(Producao producao) {
        tvId.setText("ID: " + producao.getId());
        tvDataEntrada.setText("Data de Entrada: " + producao.getDataEntrada());
        tvTempoPlantio.setText("Tempo de Plantio: " + producao.getTempoPlantio() + " dias");
        tvQuantidadePrevista.setText("Quantidade Prevista: " + producao.getQuantidadePrevista());

        String nomeProduto = (producao.getProduto() != null) ? producao.getProduto().getNome() : "N/A";
        tvProduto.setText("Produto: " + nomeProduto);

        tvStatus.setText("Status: " + producao.getStatus());

        // Configurar a lista de insumos
        List<Insumo> insumos = producao.getInsumos();
        if (insumos != null && !insumos.isEmpty()) {
            insumoProducaoAdapter = new InsumoProducaoAdapter(insumos);
            rvInsumosProducao.setAdapter(insumoProducaoAdapter);
            insumoProducaoAdapter.notifyDataSetChanged();
        } else {
            // Exibir uma mensagem indicando que não há insumos
            insumosListEmpty();
        }
    }

    private void insumosListEmpty() {
        // Opcional: exibir uma mensagem ou alterar a visibilidade da RecyclerView
        // Por exemplo, exibir um TextView indicando que não há insumos
    }

    private void realizarColheita() {
        // Implementar a lógica para realizar colheita
        // Por exemplo, abrir uma nova atividade para registrar a colheita

        Intent intent = new Intent(ProducaoDetailActivity.this, RealizarColheitaActivity.class);
        intent.putExtra("producao_id", producaoId);
        startActivity(intent);
    }

    private void editarProducao() {
        // Abrir a atividade de edição da produção
        Intent intent = new Intent(ProducaoDetailActivity.this, EditProducaoActivity.class);
        intent.putExtra("producao_id", producaoId);
        startActivity(intent);
    }

    private void confirmarExcluirProducao() {
        // Exibir diálogo de confirmação
        new AlertDialog.Builder(this)
                .setTitle("Excluir Produção")
                .setMessage("Tem certeza que deseja excluir esta produção?")
                .setPositiveButton("Sim", (dialog, which) -> excluirProducao())
                .setNegativeButton("Não", null)
                .show();
    }

    private void excluirProducao() {
        Call<Void> call = apiService.deleteProducao(producaoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ProducaoDetailActivity.this, "Produção excluída com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Volta para a lista de produções
                } else {
                    Toast.makeText(ProducaoDetailActivity.this, "Erro ao excluir produção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProducaoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProducaoDetails(Producao producao) {
        // ... (dados anteriores)

        // Configurar a lista de insumos
        List<Insumo> insumos = producao.getInsumos();
        if (insumos != null && !insumos.isEmpty()) {
            insumoProducaoAdapter = new InsumoProducaoAdapter(insumos);
            rvInsumosProducao.setAdapter(insumoProducaoAdapter);
            insumoProducaoAdapter.notifyDataSetChanged();
        } else {
            // Exibir uma mensagem indicando que não há insumos
            // Por exemplo, adicionar um TextView vazio ou exibir uma mensagem no layout
            Toast.makeText(this, "Esta produção não possui insumos adicionados.", Toast.LENGTH_SHORT).show();
        }
    }

}
