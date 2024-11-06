package com.agrobyte.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Producao {

    @SerializedName("id")
    private int id;

    @SerializedName("dataEntrada")
    private String dataEntrada;

    @SerializedName("tempoPlantio")
    private int tempoPlantio;

    @SerializedName("quantidadePrevista")
    private int quantidadePrevista;

    @SerializedName("status")
    private String status;

    @SerializedName("produto")
    private Produto produto; // Pode ser null

    @SerializedName("insumos")
    private List<Insumo> insumos;

    public int getId() {
        return id;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Insumo> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<Insumo> insumos) {
        this.insumos = insumos;
    }

    // Getters e Setters
    // ...
}
