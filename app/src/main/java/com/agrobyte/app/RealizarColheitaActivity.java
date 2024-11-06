package com.agrobyte.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.model.Colheita;
import com.agrobyte.app.model.ColheitaRequest;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Activity para realizar uma colheita
public class RealizarColheitaActivity extends AppCompatActivity {

    private EditText etPerdaDoenca, etPerdaErro;
    private Button btnSalvar, btnVoltar;
    private ApiService apiService;
    private int producaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_colheita);

        // Inicializar componentes
        etPerdaDoenca = findViewById(R.id.etPerdaDoenca);
        etPerdaErro = findViewById(R.id.etPerdaErro);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Obter instância do ApiService com autenticação
        apiService = ApiClient.getApiServiceWithAuth(this);

        // Obter o ID da produção para a qual realizar a colheita
        producaoId = getIntent().getIntExtra("producao_id", -1);

        if(producaoId == -1){
            Toast.makeText(this, "ID de produção inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvarColheita());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarColheita() {
        String perdaDoencaStr = etPerdaDoenca.getText().toString().trim();
        String perdaErroStr = etPerdaErro.getText().toString().trim();

        if(perdaDoencaStr.isEmpty() || perdaErroStr.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int perdaDoenca, perdaErro;
        try {
            perdaDoenca = Integer.parseInt(perdaDoencaStr);
            perdaErro = Integer.parseInt(perdaErroStr);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criação do objeto ColheitaRequest
        ColheitaRequest colheitaRequest = new ColheitaRequest();
        colheitaRequest.setId(producaoId);
        colheitaRequest.setPerdaDoenca(perdaDoenca);
        colheitaRequest.setPerdaErro(perdaErro);

        // Enviar requisição para realizar a colheita
        Call<Colheita> call = apiService.realizarColheita(colheitaRequest);
        call.enqueue(new Callback<Colheita>() {
            @Override
            public void onResponse(@NonNull Call<Colheita> call, @NonNull Response<Colheita> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(RealizarColheitaActivity.this, "Colheita realizada com sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(RealizarColheitaActivity.this, "Erro ao realizar colheita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Colheita> call, @NonNull Throwable t) {
                Toast.makeText(RealizarColheitaActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
