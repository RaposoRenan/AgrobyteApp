package com.agrobyte.app.model;

import java.util.List;

public class Producao {
    private int id;
    private String dataEntrada;
    private int tempoPlantio;
    private int quantidadePrevista;
    private String status;
    private Produto produto;
    private List<InsumoQuantidade> insumos;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(String dataEntrada) { this.dataEntrada = dataEntrada; }

    public int getTempoPlantio() { return tempoPlantio; }
    public void setTempoPlantio(int tempoPlantio) { this.tempoPlantio = tempoPlantio; }

    public int getQuantidadePrevista() { return quantidadePrevista; }
    public void setQuantidadePrevista(int quantidadePrevista) { this.quantidadePrevista = quantidadePrevista; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public List<InsumoQuantidade> getInsumos() { return insumos; }
    public void setInsumos(List<InsumoQuantidade> insumos) { this.insumos = insumos; }
}
