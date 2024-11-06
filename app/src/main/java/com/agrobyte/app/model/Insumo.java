package com.agrobyte.app.model;

public class Insumo {

    private int id;
    private String nome;
    private double valorUnitario;
    private int quantidade; // Campo renomeado de "quantidadeEstoque" para "quantidade"
    private double valor; // Campo adicional conforme a resposta da API
    private String dataValidade;

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() { // Getter para "quantidade"
        return quantidade;
    }

    public void setQuantidade(int quantidade) { // Setter para "quantidade"
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }
}
