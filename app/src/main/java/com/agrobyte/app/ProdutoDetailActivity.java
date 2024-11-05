package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.Produto;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoDetailActivity extends AppCompatActivity {

    private TextView tvId, tvNome, tvQuantidade, tvValorUnitario, tvDataValidade;
    private Button btnEditar, btnExcluir, btnVoltar;
    private ApiService apiService;
    private int produtoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detail);

        tvId = findViewById(R.id.tvId);
        tvNome = findViewById(R.id.tvNome);
        tvQuantidade = findViewById(R.id.tvQuantidade);
        tvValorUnitario = findViewById(R.id.tvValorUnitario);
        tvDataValidade = findViewById(R.id.tvDataValidade);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);

        apiService = ApiClient.getApiServiceWithAuth(this);
        produtoId = getIntent().getIntExtra("produto_id", -1);

        if (produtoId != -1) {
            fetchProdutoDetails();
        } else {
            Toast.makeText(this, "ID de produto inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnEditar.setOnClickListener(v -> editProduto());
        btnExcluir.setOnClickListener(v -> deleteProduto());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void fetchProdutoDetails() {
        Call<Produto> call = apiService.getProdutoById(produtoId);
        call.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(@NonNull Call<Produto> call, @NonNull Response<Produto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produto produto = response.body();
                    displayProdutoDetails(produto);
                } else {
                    Toast.makeText(ProdutoDetailActivity.this, "Erro ao carregar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Produto> call, @NonNull Throwable t) {
                Toast.makeText(ProdutoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProdutoDetails(Produto produto) {
        tvId.setText("ID: " + produto.getId());
        tvNome.setText("Nome: " + produto.getNome());
        tvQuantidade.setText("Quantidade: " + produto.getQuantidadeEstoque());
        tvValorUnitario.setText("Valor Unitário: " + produto.getValorUnitario());
        tvDataValidade.setText("Data de Validade: " + produto.getDataValidade());
    }

    private void editProduto() {
        Intent intent = new Intent(ProdutoDetailActivity.this, EditProdutoActivity.class);
        intent.putExtra("produto_id", produtoId);
        startActivity(intent);
    }

    private void deleteProduto() {
        Call<Void> call = apiService.deleteProduto(produtoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProdutoDetailActivity.this, "Produto excluído com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProdutoDetailActivity.this, "Falha ao excluir produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProdutoDetailActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
