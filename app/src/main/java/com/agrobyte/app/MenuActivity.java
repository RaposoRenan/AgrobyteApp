package com.agrobyte.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MenuActivity extends AppCompatActivity {

    private MaterialCardView cardColheitas, cardProducao, cardInsumos, cardProdutos;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicializar os cards e botão de logout
        cardColheitas = findViewById(R.id.cardColheitas);
        cardProducao = findViewById(R.id.cardProducao);
        cardInsumos = findViewById(R.id.cardInsumos);
        cardProdutos = findViewById(R.id.cardProdutos);
        btnLogout = findViewById(R.id.btnLogout);

        // Configurar os listeners de clique para os cards
        cardColheitas.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, ColheitasActivity.class)));
        cardProducao.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, ProducaoActivity.class)));
        cardInsumos.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, InsumosActivity.class)));
        cardProdutos.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, ProdutosActivity.class)));

        // Logout com confirmação
        btnLogout.setOnClickListener(view -> showLogoutConfirmationDialog());
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Logout")
                .setMessage("Tem certeza de que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
