package com.agrobyte.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInsumosActivity extends AppCompatActivity {

    private EditText etNome, etValorUnitario, etQuantidadeEstoque, etDataValidade;
    private Button btnSalvar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insumo);

        // Inicializar componentes
        etNome = findViewById(R.id.etNome);
        etValorUnitario = findViewById(R.id.etValorUnitario);
        etQuantidadeEstoque = findViewById(R.id.etQuantidadeEstoque);
        etDataValidade = findViewById(R.id.etDataValidade);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Configurar ação do botão salvar
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarInsumo();
            }
        });
    }

    private void salvarInsumo() {
        String nome = etNome.getText().toString().trim();
        String valorUnitarioStr = etValorUnitario.getText().toString().trim();
        String quantidadeEstoqueStr = etQuantidadeEstoque.getText().toString().trim();
        String dataValidade = etDataValidade.getText().toString().trim();

        if (nome.isEmpty() || valorUnitarioStr.isEmpty() || quantidadeEstoqueStr.isEmpty() || dataValidade.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorUnitario;
        int quantidadeEstoque;

        try {
            valorUnitario = Double.parseDouble(valorUnitarioStr);
            quantidadeEstoque = Integer.parseInt(quantidadeEstoqueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto Insumo
        Insumo newInsumo = new Insumo();
        newInsumo.setNome(nome);
        newInsumo.setValorUnitario(valorUnitario);
        newInsumo.setQuantidade(quantidadeEstoque); // Método corrigido
        newInsumo.setDataValidade(dataValidade);
        // Se necessário, calcule e defina o valor

        // Enviar requisição para a API
        Call<Insumo> call = apiService.createInsumo(newInsumo);
        call.enqueue(new Callback<Insumo>() {
            @Override
            public void onResponse(@NonNull Call<Insumo> call, @NonNull Response<Insumo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddInsumosActivity.this, "Insumo salvo com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Voltar para a atividade anterior
                } else {
                    Toast.makeText(AddInsumosActivity.this, "Erro ao salvar insumo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Insumo> call, @NonNull Throwable t) {
                Toast.makeText(AddInsumosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
