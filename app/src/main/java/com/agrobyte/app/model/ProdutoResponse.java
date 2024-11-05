package com.agrobyte.app.model;

import java.util.List;

public class ProdutoResponse {
    private List<Produto> content;

    public List<Produto> getContent() {
        return content;
    }

    public void setContent(List<Produto> content) {
        this.content = content;
    }
}
