package com.agrobyte.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Insumo;

import java.util.List;

public class InsumoAdapter extends RecyclerView.Adapter<InsumoAdapter.InsumoViewHolder> {

    private List<Insumo> insumos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Insumo insumo);
    }

    public InsumoAdapter(List<Insumo> insumos, OnItemClickListener listener) {
        this.insumos = insumos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InsumoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo, parent, false);
        return new InsumoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InsumoViewHolder holder, int position) {
        Insumo insumo = insumos.get(position);
        holder.tvId.setText("ID: " + insumo.getId());
        holder.tvNome.setText("Nome: " + insumo.getNome());
        holder.tvQuantidade.setText("Quantidade: " + insumo.getQuantidade());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(insumo));
    }

    @Override
    public int getItemCount() {
        return insumos.size();
    }

    public void updateList(List<Insumo> newList) {
        insumos = newList;
        notifyDataSetChanged();
    }

    static class InsumoViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNome, tvQuantidade;

        public InsumoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
        }
    }
}
