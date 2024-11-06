package com.agrobyte.app.model;

import java.util.List;

public class ProducaoRequest {
    private Produto produto;
    private int tempoPlantio;
    private int quantidadePrevista;
    private List<InsumoQuantidade> insumos;

    // Getters e Setters

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getTempoPlantio() {
        return tempoPlantio;
    }

    public void setTempoPlantio(int tempoPlantio) {
        this.tempoPlantio = tempoPlantio;
    }

    public int getQuantidadePrevista() {
        return quantidadePrevista;
    }

    public void setQuantidadePrevista(int quantidadePrevista) {
        this.quantidadePrevista = quantidadePrevista;
    }

    public List<InsumoQuantidade> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<InsumoQuantidade> insumos) {
        this.insumos = insumos;
    }
}
