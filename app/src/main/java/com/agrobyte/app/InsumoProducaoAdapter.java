package com.agrobyte.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.InsumoQuantidade;

import java.util.List;

public class InsumoProducaoAdapter extends RecyclerView.Adapter<InsumoProducaoAdapter.ViewHolder> {

    private final List<InsumoQuantidade> insumoList;
    private final Context context;

    public InsumoProducaoAdapter(Context context, List<InsumoQuantidade> insumoList) {
        this.context = context;
        this.insumoList = insumoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InsumoQuantidade insumo = insumoList.get(position);
        holder.tvNomeInsumo.setText(insumo.getNome());
        holder.tvQuantidade.setText("Quantidade: " + insumo.getQuantidade());

/*        holder.btnDeleteInsumo.setOnClickListener(v -> {
            insumoList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Insumo removido", Toast.LENGTH_SHORT).show();
        });*/
    }

    @Override
    public int getItemCount() {
        return insumoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeInsumo, tvQuantidade;
        //private final ImageButton btnDeleteInsumo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeInsumo = itemView.findViewById(R.id.tvNomeInsumo);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            //btnDeleteInsumo = itemView.findViewById(R.id.btnDeleteInsumo);
        }
    }
}
