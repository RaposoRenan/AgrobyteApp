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

public class AddProdutoActivity extends AppCompatActivity {

    private EditText etNome, etValorUnitario, etQuantidadeEstoque, etDataValidade;
    private Button btnSalvar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        etNome = findViewById(R.id.etNome);
        etValorUnitario = findViewById(R.id.etValorUnitario);
        etQuantidadeEstoque = findViewById(R.id.etQuantidadeEstoque);
        etDataValidade = findViewById(R.id.etDataValidade);
        btnSalvar = findViewById(R.id.btnSalvar);

        apiService = ApiClient.getApiServiceWithAuth(this);

        btnSalvar.setOnClickListener(v -> saveNewProduto());
    }

    private void saveNewProduto() {
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

        Produto newProduto = new Produto();
        newProduto.setNome(nome);
        newProduto.setValorUnitario(valorUnitario);
        newProduto.setQuantidadeEstoque(quantidadeEstoque);
        newProduto.setDataValidade(dataValidade);

        Call<Produto> call = apiService.createProduto(newProduto);
        call.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(@NonNull Call<Produto> call, @NonNull Response<Produto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProdutoActivity.this, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Volta para a tela anterior
                } else {
                    Toast.makeText(AddProdutoActivity.this, "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Produto> call, @NonNull Throwable t) {
                Toast.makeText(AddProdutoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
