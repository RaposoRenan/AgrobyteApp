package com.agrobyte.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.R;
import com.agrobyte.app.model.Producao;

import java.util.List;

public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.ViewHolder> {

    private List<Producao> producaoList;
    private final OnProducaoClickListener onProducaoClickListener;

    public ProducaoAdapter(List<Producao> producaoList, OnProducaoClickListener onProducaoClickListener) {
        this.producaoList = producaoList;
        this.onProducaoClickListener = onProducaoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producao producao = producaoList.get(position);
        holder.tvId.setText("ID: " + producao.getId());
        holder.tvNomeProduto.setText("Produto: " + (producao.getProduto() != null ? producao.getProduto().getNome() : "N/A"));
        holder.tvStatus.setText("Status: " + producao.getStatus());
        holder.itemView.setOnClickListener(v -> onProducaoClickListener.onProducaoClick(producao));
    }

    @Override
    public int getItemCount() {
        return producaoList.size();
    }

    public void updateList(List<Producao> newList) {
        producaoList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId, tvNomeProduto, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNomeProduto = itemView.findViewById(R.id.tvNomeProduto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    public interface OnProducaoClickListener {
        void onProducaoClick(Producao producao);
    }
}
