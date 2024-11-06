package com.agrobyte.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Producao;

import java.util.List;

public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.ProducaoViewHolder> {

    private List<Producao> producoes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producao producao);
    }

    public ProducaoAdapter(List<Producao> producoes, OnItemClickListener listener) {
        this.producoes = producoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProducaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producao, parent, false);
        return new ProducaoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducaoViewHolder holder, int position) {
        Producao producao = producoes.get(position);
        holder.tvId.setText("ID: " + producao.getId());

        String nomeProduto = (producao.getProduto() != null) ? producao.getProduto().getNome() : "N/A";
        holder.tvProduto.setText("Produto: " + nomeProduto);

        holder.tvStatus.setText("Status: " + producao.getStatus());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(producao));
    }

    @Override
    public int getItemCount() {
        return producoes.size();
    }

    public void updateList(List<Producao> newList) {
        producoes = newList;
        notifyDataSetChanged();
    }

    static class ProducaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvProduto, tvStatus;

        public ProducaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvProduto = itemView.findViewById(R.id.tvProduto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
