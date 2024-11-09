package com.agrobyte.app.model;

public class ColheitaRequest {

    private int id;
    private int perdaDoenca;
    private int perdaErro;

    public ColheitaRequest(int id, int perdaDoenca, int perdaErro) {
        this.id = id;
        this.perdaDoenca = perdaDoenca;
        this.perdaErro = perdaErro;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerdaDoenca() {
        return perdaDoenca;
    }

    public void setPerdaDoenca(int perdaDoenca) {
        this.perdaDoenca = perdaDoenca;
    }

    public int getPerdaErro() {
        return perdaErro;
    }

    public void setPerdaErro(int perdaErro) {
        this.perdaErro = perdaErro;
    }
}
