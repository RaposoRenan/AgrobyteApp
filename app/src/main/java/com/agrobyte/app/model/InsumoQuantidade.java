package com.agrobyte.app.model;

public class InsumoQuantidade {
    private int id;
    private int quantidade;
    private String nome;

    // Construtor
    public InsumoQuantidade(int id, int quantidade, String nome) {
        this.id = id;
        this.quantidade = quantidade;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
