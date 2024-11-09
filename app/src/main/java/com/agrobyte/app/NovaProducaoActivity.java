package com.agrobyte.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.InsumoQuantidade;
import com.agrobyte.app.model.InsumoResponse;
import com.agrobyte.app.model.Producao;
import com.agrobyte.app.model.Produto;
import com.agrobyte.app.model.ProdutoResponse;
import com.agrobyte.app.network.ApiClient;
import com.agrobyte.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovaProducaoActivity extends AppCompatActivity {

    private Spinner spnProdutos;
    private EditText etTempoPlantio, etQuantidadePrevista;
    private Button btnAdicionarInsumo, btnSalvarProducao;
    private RecyclerView recyclerViewInsumos;
    private List<InsumoQuantidade> insumosList = new ArrayList<>();
    private ArrayAdapter<String> produtoAdapter;
    private InsumoProducaoAdapter insumoAdapter;
    private ApiService apiService;
    private boolean isEditMode = false;
    private int producaoId;
    private List<Produto> listaProdutos = new ArrayList<>();
    private List<Insumo> listaInsumos = new ArrayList<>();
    private List<String> nomesProdutos = new ArrayList<>();
    private List<String> nomesInsumos = new ArrayList<>();
    private Producao producaoExistente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_producao);

        spnProdutos = findViewById(R.id.spnProdutos);
        etTempoPlantio = findViewById(R.id.etTempoPlantio);
        etQuantidadePrevista = findViewById(R.id.etQuantidadePrevista);
        btnAdicionarInsumo = findViewById(R.id.btnAdicionarInsumo);
        btnSalvarProducao = findViewById(R.id.btnSalvarProducao);
        recyclerViewInsumos = findViewById(R.id.recyclerViewInsumos);

        apiService = ApiClient.getApiServiceWithAuth(this);

        insumoAdapter = new InsumoProducaoAdapter(this, insumosList);
        recyclerViewInsumos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInsumos.setAdapter(insumoAdapter);

        if (getIntent().hasExtra("producao_id")) {
            isEditMode = true;
            producaoId = getIntent().getIntExtra("producao_id", -1);
            carregarDadosProducao();
        }

        carregarProdutos();
        carregarInsumos();

        btnAdicionarInsumo.setOnClickListener(v -> adicionarInsumo());
        btnSalvarProducao.setOnClickListener(v -> salvarProducao());
    }

    private void carregarProdutos() {
        Call<ProdutoResponse> call = apiService.getProdutos();
        call.enqueue(new Callback<ProdutoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProdutoResponse> call, @NonNull Response<ProdutoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getContent() != null) {
                    listaProdutos = response.body().getContent();
                    nomesProdutos.clear();
                    for (Produto produto : listaProdutos) {
                        nomesProdutos.add(produto.getNome());
                    }

                    produtoAdapter = new ArrayAdapter<>(NovaProducaoActivity.this, android.R.layout.simple_spinner_item, nomesProdutos);
                    produtoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnProdutos.setAdapter(produtoAdapter);

                    if (isEditMode && producaoExistente != null && producaoExistente.getProduto() != null) {
                        int index = nomesProdutos.indexOf(producaoExistente.getProduto().getNome());
                        if (index >= 0) {
                            spnProdutos.setSelection(index);
                        }
                    }
                } else {
                    Toast.makeText(NovaProducaoActivity.this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProdutoResponse> call, @NonNull Throwable t) {
                Toast.makeText(NovaProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarInsumos() {
        Call<InsumoResponse> call = apiService.getInsumos();
        call.enqueue(new Callback<InsumoResponse>() {
            @Override
            public void onResponse(@NonNull Call<InsumoResponse> call, @NonNull Response<InsumoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getContent() != null) {
                    listaInsumos = response.body().getContent();
                    nomesInsumos.clear();
                    for (Insumo insumo : listaInsumos) {
                        nomesInsumos.add(insumo.getNome());
                    }

                    if (isEditMode && producaoExistente != null && producaoExistente.getInsumos() != null) {
                        insumosList.addAll(producaoExistente.getInsumos());
                        insumoAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(NovaProducaoActivity.this, "Erro ao carregar insumos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<InsumoResponse> call, @NonNull Throwable t) {
                Toast.makeText(NovaProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarDadosProducao() {
        Call<Producao> call = apiService.getProducaoById(producaoId);
        call.enqueue(new Callback<Producao>() {
            @Override
            public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    producaoExistente = response.body();
                    preencherCamposProducao(producaoExistente);
                } else {
                    Toast.makeText(NovaProducaoActivity.this, "Erro ao carregar produção", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                Toast.makeText(NovaProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void preencherCamposProducao(Producao producao) {
        etTempoPlantio.setText(String.valueOf(producao.getTempoPlantio()));
        etQuantidadePrevista.setText(String.valueOf(producao.getQuantidadePrevista()));
    }

    private void adicionarInsumo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Insumo");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_adicionar_insumo, null);
        final Spinner spnInsumos = viewInflated.findViewById(R.id.spnInsumos);
        final EditText etQuantidade = viewInflated.findViewById(R.id.etQuantidadeInsumo);

        ArrayAdapter<String> insumoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesInsumos);
        insumoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInsumos.setAdapter(insumoAdapter);

        builder.setView(viewInflated);

        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            String nomeInsumo = spnInsumos.getSelectedItem().toString();
            int quantidade = Integer.parseInt(etQuantidade.getText().toString());

            for (Insumo insumo : listaInsumos) {
                if (insumo.getNome().equals(nomeInsumo)) {
                    InsumoQuantidade iq = new InsumoQuantidade(insumo.getId(), quantidade, insumo.getNome());
                    insumosList.add(iq);
                    break;
                }
            }
            this.insumoAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void salvarProducao() {
        int tempoPlantio = Integer.parseInt(etTempoPlantio.getText().toString());
        int quantidadePrevista = Integer.parseInt(etQuantidadePrevista.getText().toString());
        String nomeProduto = spnProdutos.getSelectedItem().toString();

        Produto produtoSelecionado = null;
        for (Produto produto : listaProdutos) {
            if (produto.getNome().equals(nomeProduto)) {
                produtoSelecionado = produto;
                break;
            }
        }

        if (produtoSelecionado == null) {
            Toast.makeText(this, "Produto inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Producao producao = new Producao();
        producao.setProduto(produtoSelecionado);
        producao.setTempoPlantio(tempoPlantio);
        producao.setQuantidadePrevista(quantidadePrevista);
        producao.setInsumos(insumosList);

        if (isEditMode) {
            Call<Producao> call = apiService.updateProducao(producaoId, producao);
            call.enqueue(new Callback<Producao>() {
                @Override
                public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NovaProducaoActivity.this, "Produção atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NovaProducaoActivity.this, "Erro ao atualizar produção", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                    Toast.makeText(NovaProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<Producao> call = apiService.createProducao(producao);
            call.enqueue(new Callback<Producao>() {
                @Override
                public void onResponse(@NonNull Call<Producao> call, @NonNull Response<Producao> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NovaProducaoActivity.this, "Produção criada com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NovaProducaoActivity.this, "Erro ao criar produção", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Producao> call, @NonNull Throwable t) {
                    Toast.makeText(NovaProducaoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
