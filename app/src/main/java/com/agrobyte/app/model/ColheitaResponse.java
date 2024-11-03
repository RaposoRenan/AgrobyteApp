package com.agrobyte.app.model;

import java.util.List;

public class ColheitaResponse {
    private List<Colheita> content;

    // Outros campos omitidos para simplicidade

    public List<Colheita> getContent() {
        return content;
    }

    public void setContent(List<Colheita> content) {
        this.content = content;
    }
}
