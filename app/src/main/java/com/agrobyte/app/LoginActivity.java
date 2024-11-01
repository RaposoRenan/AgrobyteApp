package com.agrobyte.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;
import com.agrobyte.app.network.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar;
    private ApiService apiService;

    // Substitua pelos valores do seu client_id e client_secret
    private static final String CLIENT_ID = "myclientid";
    private static final String CLIENT_SECRET = "myclientsecret";
    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "read write";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar as views
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        apiService = ApiClient.getApiService(this);

        // Configurar o evento de clique do botão
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                efetuarLogin();
            }
        });
    }

    private void efetuarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<TokenResponse> call = apiService.login(
                GRANT_TYPE,
                email,
                senha,
                SCOPE,
                CLIENT_ID,
                CLIENT_SECRET
        );

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse tokenResponse = response.body();
                    // Salvar o token
                    salvarToken(tokenResponse.getAccessToken());

                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Navegar para a próxima tela (por exemplo, MainActivity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Falha no login: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarToken(String token) {
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", token);
        editor.apply();
    }

}