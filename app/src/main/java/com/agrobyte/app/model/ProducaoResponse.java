package com.agrobyte.app.model;

import java.util.List;

public class ProducaoResponse {
    private List<Producao> content;
    private Pageable pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private Sort sort;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    // Getters e Setters

    public List<Producao> getContent() {
        return content;
    }

    public void setContent(List<Producao> content) {
        this.content = content;
    }

    // Outros getters e setters para os campos restantes
}
