package com.agrobyte.app.utils.List;

import com.agrobyte.app.model.Producao;

import java.util.List;

public class ProducaoResponse {
    private List<Producao> content;
    private int totalPages;
    private int totalElements;
    private boolean last;

    // Getters e Setters
    public List<Producao> getContent() {
        return content;
    }

    public void setContent(List<Producao> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
