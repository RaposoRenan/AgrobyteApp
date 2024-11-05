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

public class EditInsumoActivity extends AppCompatActivity {

    private EditText etNome, etValorUnitario, etQuantidadeEstoque, etDataValidade;
    private Button btnSalvar;
    private ApiService apiService;
    private int insumoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_insumo);

        etNome = findViewById(R.id.etNome);
        etValorUnitario = findViewById(R.id.etValorUnitario);
        etQuantidadeEstoque = findViewById(R.id.etQuantidadeEstoque);
        etDataValidade = findViewById(R.id.etDataValidade);
        btnSalvar = findViewById(R.id.btnSalvar);

        apiService = ApiClient.getApiServiceWithAuth(this);

        insumoId = getIntent().getIntExtra("insumo_id", -1);

        if (insumoId != -1) {
            fetchInsumoDetails();
        } else {
            finish();
        }

        btnSalvar.setOnClickListener(v -> saveInsumoChanges());
    }

    private void fetchInsumoDetails() {
        Call<Insumo> call = apiService.getInsumoById(insumoId);
        call.enqueue(new Callback<Insumo>() {
            @Override
            public void onResponse(@NonNull Call<Insumo> call, @NonNull Response<Insumo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Insumo insumo = response.body();
                    populateFields(insumo);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Insumo> call, @NonNull Throwable t) {
                Toast.makeText(EditInsumoActivity.this, "Erro ao carregar insumo", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateFields(Insumo insumo) {
        etNome.setText(insumo.getNome());
        etValorUnitario.setText(String.valueOf(insumo.getValorUnitario()));
        etQuantidadeEstoque.setText(String.valueOf(insumo.getQuantidadeEstoque()));
        etDataValidade.setText(insumo.getDataValidade());
    }

    private void saveInsumoChanges() {
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

        Insumo updatedInsumo = new Insumo();
        updatedInsumo.setId(insumoId);
        updatedInsumo.setNome(nome);
        updatedInsumo.setValorUnitario(valorUnitario);
        updatedInsumo.setQuantidadeEstoque(quantidadeEstoque);
        updatedInsumo.setDataValidade(dataValidade);

        Call<Insumo> call = apiService.updateInsumo(insumoId, updatedInsumo);
        call.enqueue(new Callback<Insumo>() {
            @Override
            public void onResponse(@NonNull Call<Insumo> call, @NonNull Response<Insumo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditInsumoActivity.this, "Insumo atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditInsumoActivity.this, "Erro ao atualizar insumo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Insumo> call, @NonNull Throwable t) {
                Toast.makeText(EditInsumoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
