package com.agrobyte.app;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agrobyte.app.model.Insumo;

import java.util.List;

public class InsumoSpinnerAdapter extends ArrayAdapter<Insumo> {

    public InsumoSpinnerAdapter(@NonNull Context context, List<Insumo> insumos) {
        super(context, android.R.layout.simple_spinner_item, insumos);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Nullable
    @Override
    public Insumo getItem(int position) {
        return super.getItem(position);
    }
}
