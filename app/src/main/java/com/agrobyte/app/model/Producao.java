package com.agrobyte.app.model;

import java.util.List;

public class Producao {
    private int id;
    private String dataEntrada;
    private int tempoPlantio;
    private int quantidadePrevista;
    private String status;
    private Produto produto; // Objeto Produto
    private List<Insumo> insumos; // Lista de Insumos

    // Getters e Setters
    public int getId() { return id; }
    public String getDataEntrada() { return dataEntrada; }
    public int getTempoPlantio() { return tempoPlantio; }
    public int getQuantidadePrevista() { return quantidadePrevista; }
    public String getStatus() { return status; }
    public Produto getProduto() { return produto; }
    public List<Insumo> getInsumos() { return insumos; }

    public void setId(int id) { this.id = id; }
    public void setDataEntrada(String dataEntrada) { this.dataEntrada = dataEntrada; }
    public void setTempoPlantio(int tempoPlantio) { this.tempoPlantio = tempoPlantio; }
    public void setQuantidadePrevista(int quantidadePrevista) { this.quantidadePrevista = quantidadePrevista; }
    public void setStatus(String status) { this.status = status; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public void setInsumos(List<Insumo> insumos) { this.insumos = insumos; }
}
