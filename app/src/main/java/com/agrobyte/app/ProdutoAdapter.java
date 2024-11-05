package com.agrobyte.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Produto;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> produtos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Produto produto);
    }

    public ProdutoAdapter(List<Produto> produtos, OnItemClickListener listener) {
        this.produtos = produtos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        holder.tvId.setText("ID: " + produto.getId());
        holder.tvNome.setText("Nome: " + produto.getNome());
        holder.tvQuantidade.setText("Quantidade: " + produto.getQuantidadeEstoque());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(produto));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void updateList(List<Produto> newList) {
        produtos = newList;
        notifyDataSetChanged();
    }

    static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNome, tvQuantidade;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
        }
    }
}
