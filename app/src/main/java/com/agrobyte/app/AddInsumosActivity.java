package com.agrobyte.app;

import android.os.Bundle;
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

        apiService = ApiClient.getApiServiceWithAuth(this);

        // Configurar ação do botão Salvar
        btnSalvar.setOnClickListener(v -> saveNewInsumo());
    }

    private void saveNewInsumo() {
        String nome = etNome.getText().toString().trim();
        double valorUnitario;
        int quantidadeEstoque;

        // Validar e obter valores dos campos
        try {
            valorUnitario = Double.parseDouble(etValorUnitario.getText().toString().trim());
            quantidadeEstoque = Integer.parseInt(etQuantidadeEstoque.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        String dataValidade = etDataValidade.getText().toString().trim();

        // Criar um novo objeto Insumo com os dados
        Insumo newInsumo = new Insumo();
        newInsumo.setNome(nome);
        newInsumo.setValorUnitario(valorUnitario);
        newInsumo.setQuantidadeEstoque(quantidadeEstoque);
        newInsumo.setDataValidade(dataValidade);

        // Fazer a requisição POST para adicionar o novo insumo
        Call<Insumo> call = apiService.createInsumo(newInsumo);
        call.enqueue(new Callback<Insumo>() {
            @Override
            public void onResponse(@NonNull Call<Insumo> call, @NonNull Response<Insumo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddInsumosActivity.this, "Insumo adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Volta para a tela anterior
                } else {
                    Toast.makeText(AddInsumosActivity.this, "Erro ao adicionar insumo: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Insumo> call, @NonNull Throwable t) {
                Toast.makeText(AddInsumosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
