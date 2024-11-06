package com.agrobyte.app;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agrobyte.app.model.Produto;

import java.util.List;

public class ProdutoSpinnerAdapter extends ArrayAdapter<Produto> {

    public ProdutoSpinnerAdapter(@NonNull Context context, List<Produto> produtos) {
        super(context, android.R.layout.simple_spinner_item, produtos);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Nullable
    @Override
    public Produto getItem(int position) {
        return super.getItem(position);
    }
}
