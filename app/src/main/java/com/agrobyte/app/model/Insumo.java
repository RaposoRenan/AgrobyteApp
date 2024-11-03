package com.agrobyte.app.model;

public class Insumo {
    private int id;
    private String nome;
    private double valorUnitario;
    private int quantidade;
    private double valor;

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getValorUnitario() { return valorUnitario; }
    public int getQuantidade() { return quantidade; }
    public double getValor() { return valor; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setValor(double valor) { this.valor = valor; }
}
