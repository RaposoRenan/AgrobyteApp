package com.agrobyte.app.model;

public class Colheita {
    private int id;
    private String dataColheita;
    private int qntdColhida;
    private int perdaDoenca;
    private int perdaErro;
    private String nomeProduto; // Novo campo para o nome do produto

    // Getters e Setters
    public int getId() { return id; }
    public String getDataColheita() { return dataColheita; }
    public int getQntdColhida() { return qntdColhida; }
    public int getPerdaDoenca() { return perdaDoenca; }
    public int getPerdaErro() { return perdaErro; }
    public String getNomeProduto() { return nomeProduto; } // Getter para o nome do produto

    public void setId(int id) { this.id = id; }
    public void setDataColheita(String dataColheita) { this.dataColheita = dataColheita; }
    public void setQntdColhida(int qntdColhida) { this.qntdColhida = qntdColhida; }
    public void setPerdaDoenca(int perdaDoenca) { this.perdaDoenca = perdaDoenca; }
    public void setPerdaErro(int perdaErro) { this.perdaErro = perdaErro; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; } // Setter para o nome do produto
}
