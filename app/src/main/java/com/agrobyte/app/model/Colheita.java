package com.agrobyte.app.model;

public class Colheita {
    private int id;
    private String dataColheita;
    private int qntdColhida;
    private int perdaDoenca;
    private int perdaErro;

    // Getters e Setters
    public int getId() { return id; }
    public String getDataColheita() { return dataColheita; }
    public int getQntdColhida() { return qntdColhida; }
    public int getPerdaDoenca() { return perdaDoenca; }
    public int getPerdaErro() { return perdaErro; }

    public void setId(int id) { this.id = id; }
    public void setDataColheita(String dataColheita) { this.dataColheita = dataColheita; }
    public void setQntdColhida(int qntdColhida) { this.qntdColhida = qntdColhida; }
    public void setPerdaDoenca(int perdaDoenca) { this.perdaDoenca = perdaDoenca; }
    public void setPerdaErro(int perdaErro) { this.perdaErro = perdaErro; }
}
