package com.agrobyte.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.agrobyte.app.model.Colheita;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColheitaDetailActivity extends AppCompatActivity {

    private TextView tvId, tvDataColheita, tvQntdColhida, tvPerdaDoenca, tvPerdaErro, tvProdutoNome;
    private ApiService apiService;
    private int colheitaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colheita_detail);

        tvId = findViewById(R.id.tvId);
        tvDataColheita = findViewById(R.id.tvDataColheita);
        tvQntdColhida = findViewById(R.id.tvQntdColhida);
        tvPerdaDoenca = findViewById(R.id.tvPerdaDoenca);
        tvPerdaErro = findViewById(R.id.tvPerdaErro);
        tvProdutoNome = findViewById(R.id.tvProdutoNome);

        apiService = ApiClient.getApiServiceWithAuth(this);
        colheitaId = getIntent().getIntExtra("colheita_id", -1);

        if (colheitaId != -1) {
            fetchColheitaDetails();
        } else {
            finish();
        }
    }

    private void fetchColheitaDetails() {
        Call<Colheita> call = apiService.getColheitaById(colheitaId);
        call.enqueue(new Callback<Colheita>() {
            @Override
            public void onResponse(@NonNull Call<Colheita> call, @NonNull Response<Colheita> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayColheitaDetails(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Colheita> call, @NonNull Throwable t) {
                // Tratar falha
            }
        });
    }

    private void displayColheitaDetails(Colheita colheita) {
        tvId.setText("ID: " + colheita.getId());
        tvDataColheita.setText("Data Colheita: " + colheita.getDataColheita());
        tvQntdColhida.setText("Quantidade Colhida: " + colheita.getQntdColhida());
        tvPerdaDoenca.setText("Perda por Doença: " + colheita.getPerdaDoenca());
        tvPerdaErro.setText("Perda por Erro: " + colheita.getPerdaErro());
        tvProdutoNome.setText("Produto: " + colheita.getNomeProduto());
    }
}
