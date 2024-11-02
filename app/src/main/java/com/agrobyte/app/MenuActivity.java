package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MenuActivity extends AppCompatActivity {

    private MaterialCardView cardColheitas, cardProducao, cardInsumos, cardProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicializar os cards
        cardColheitas = findViewById(R.id.cardColheitas);
        cardProducao = findViewById(R.id.cardProducao);
        cardInsumos = findViewById(R.id.cardInsumos);
        cardProdutos = findViewById(R.id.cardProdutos);

        // Configurar os listeners de clique
        cardColheitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar para a Activity de Colheitas
                Intent intent = new Intent(MenuActivity.this, ColheitasActivity.class);
                startActivity(intent);
            }
        });

        cardProducao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar para a Activity de Produção
                Intent intent = new Intent(MenuActivity.this, ProducaoActivity.class);
                startActivity(intent);
            }
        });

        cardInsumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar para a Activity de Insumos
                Intent intent = new Intent(MenuActivity.this, InsumosActivity.class);
                startActivity(intent);
            }
        });

        cardProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar para a Activity de Produtos
                Intent intent = new Intent(MenuActivity.this, ProdutosActivity.class);
                startActivity(intent);
            }
        });
    }
}
