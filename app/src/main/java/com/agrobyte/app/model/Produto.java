package com.agrobyte.app.model;

public class Produto {
    private int id;
    private String nome;
    private double valorUnitario;
    private int quantidadeEstoque;
    private String dataValidade;

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getValorUnitario() { return valorUnitario; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public String getDataValidade() { return dataValidade; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }
}
