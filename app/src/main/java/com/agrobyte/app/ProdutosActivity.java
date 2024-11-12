package com.agrobyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Produto;
import com.agrobyte.app.model.ProdutoResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosActivity extends AppCompatActivity {

    private EditText etSearchName;
    private Button btnSearch, btnAdicionar, btnVoltar;
    private RecyclerView rvProdutos;
    private ProdutoAdapter adapter;
    private ApiService apiService;
    private List<Produto> produtosList = new ArrayList<>();

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        etSearchName = findViewById(R.id.etSearchName);
        btnSearch = findViewById(R.id.btnSearch);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnVoltar = findViewById(R.id.btnVoltar);
        rvProdutos = findViewById(R.id.rvProdutos);

        apiService = ApiClient.getApiServiceWithAuth(this);

        adapter = new ProdutoAdapter(produtosList, this::onProdutoClick);
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setAdapter(adapter);

        fetchProdutos();

        btnSearch.setOnClickListener(v -> searchProdutoByName());
        btnAdicionar.setOnClickListener(v -> openAddProdutoActivity());
        btnVoltar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProdutos(); // Atualiza a lista sempre que retorna para esta tela
    }

    private void fetchProdutos() {
        Call<ProdutoResponse> call = apiService.getProdutos();
        call.enqueue(new Callback<ProdutoResponse>() {
            @Override
            public void onResponse(Call<ProdutoResponse> call, Response<ProdutoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produtosList.clear();
                    produtosList.addAll(response.body().getContent());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProdutoResponse> call, Throwable t) {
                // Tratar erro
            }
        });
    }

    private void searchProdutoByName() {
        String nameQuery = etSearchName.getText().toString().trim();
        if (nameQuery.isEmpty()) {
            fetchProdutos();
            return;
        }

        List<Produto> filteredList = new ArrayList<>();
        for (Produto produto : produtosList) {
            if (produto.getNome().toLowerCase().contains(nameQuery.toLowerCase())) {
                filteredList.add(produto);
            }
        }
        adapter.updateList(filteredList);
    }

    private void openAddProdutoActivity() {
        Intent intent = new Intent(ProdutosActivity.this, AddProdutoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private void onProdutoClick(Produto produto) {
        Intent intent = new Intent(ProdutosActivity.this, ProdutoDetailActivity.class);
        intent.putExtra("produto_id", produto.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT) {
                fetchProdutos(); // Recarrega a lista após adicionar ou editar um produto
            }
        }
    }
}
