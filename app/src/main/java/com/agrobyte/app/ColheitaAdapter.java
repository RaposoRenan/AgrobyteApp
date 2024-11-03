package com.agrobyte.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Colheita;

import java.util.List;

public class ColheitaAdapter extends RecyclerView.Adapter<ColheitaAdapter.ColheitaViewHolder> {

    private List<Colheita> colheitas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Colheita colheita);
    }

    public ColheitaAdapter(List<Colheita> colheitas, OnItemClickListener listener) {
        this.colheitas = colheitas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColheitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colheita, parent, false);
        return new ColheitaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColheitaViewHolder holder, int position) {
        Colheita colheita = colheitas.get(position);
        holder.tvId.setText("ID: " + colheita.getId());
        holder.tvData.setText("Data: " + colheita.getDataColheita());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(colheita));
    }

    @Override
    public int getItemCount() {
        return colheitas.size();
    }

    public void updateList(List<Colheita> newList) {
        colheitas = newList;
        notifyDataSetChanged();
    }

    static class ColheitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvData;

        public ColheitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvData = itemView.findViewById(R.id.tvData);
        }
    }
}
