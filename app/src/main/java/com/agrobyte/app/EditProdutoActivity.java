package com.agrobyte.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.Produto;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProdutoActivity extends AppCompatActivity {

    private EditText etNome, etValorUnitario, etQuantidadeEstoque, etDataValidade;
    private Button btnSalvar;
    private ApiService apiService;
    private int produtoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produto);

        etNome = findViewById(R.id.etNome);
        etValorUnitario = findViewById(R.id.etValorUnitario);
        etQuantidadeEstoque = findViewById(R.id.etQuantidadeEstoque);
        etDataValidade = findViewById(R.id.etDataValidade);
        btnSalvar = findViewById(R.id.btnSalvar);

        apiService = ApiClient.getApiServiceWithAuth(this);

        produtoId = getIntent().getIntExtra("produto_id", -1);

        if (produtoId != -1) {
            fetchProdutoDetails();
        } else {
            finish();
        }

        btnSalvar.setOnClickListener(v -> saveProdutoChanges());
    }

    private void fetchProdutoDetails() {
        Call<Produto> call = apiService.getProdutoById(produtoId);
        call.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(@NonNull Call<Produto> call, @NonNull Response<Produto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produto produto = response.body();
                    populateFields(produto);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Produto> call, @NonNull Throwable t) {
                Toast.makeText(EditProdutoActivity.this, "Erro ao carregar produto", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateFields(Produto produto) {
        etNome.setText(produto.getNome());
        etValorUnitario.setText(String.valueOf(produto.getValorUnitario()));
        etQuantidadeEstoque.setText(String.valueOf(produto.getQuantidadeEstoque()));
        etDataValidade.setText(produto.getDataValidade());
    }

    private void saveProdutoChanges() {
        String nome = etNome.getText().toString().trim();
        double valorUnitario;
        int quantidadeEstoque;

        try {
            valorUnitario = Double.parseDouble(etValorUnitario.getText().toString().trim());
            quantidadeEstoque = Integer.parseInt(etQuantidadeEstoque.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        String dataValidade = etDataValidade.getText().toString().trim();

        Produto updatedProduto = new Produto();
        updatedProduto.setId(produtoId);
        updatedProduto.setNome(nome);
        updatedProduto.setValorUnitario(valorUnitario);
        updatedProduto.setQuantidadeEstoque(quantidadeEstoque);
        updatedProduto.setDataValidade(dataValidade);

        Call<Produto> call = apiService.updateProduto(produtoId, updatedProduto);
        call.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(@NonNull Call<Produto> call, @NonNull Response<Produto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProdutoActivity.this, "Produto atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditProdutoActivity.this, "Erro ao atualizar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Produto> call, @NonNull Throwable t) {
                Toast.makeText(EditProdutoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
