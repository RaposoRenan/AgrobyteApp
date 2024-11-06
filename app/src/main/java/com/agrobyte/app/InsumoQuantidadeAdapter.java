package com.agrobyte.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrobyte.app.model.Insumo;
import com.agrobyte.app.model.InsumoQuantidade;

import java.util.List;

public class InsumoQuantidadeAdapter extends RecyclerView.Adapter<InsumoQuantidadeAdapter.InsumoQuantidadeViewHolder> {

    private List<InsumoQuantidade> insumos;
    private List<Insumo> insumosDisponiveis; // Lista de insumos para preencher os spinners

    public InsumoQuantidadeAdapter(List<InsumoQuantidade> insumos, List<Insumo> insumosDisponiveis) {
        this.insumos = insumos;
        this.insumosDisponiveis = insumosDisponiveis;
    }

    @NonNull
    @Override
    public InsumoQuantidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_insumo, parent, false);
        return new InsumoQuantidadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsumoQuantidadeViewHolder holder, int position) {
        InsumoQuantidade insumoQuantidade = insumos.get(position);

        // Configurar o Spinner com os insumos disponíveis
        InsumoSpinnerAdapter spinnerAdapter = new InsumoSpinnerAdapter(holder.itemView.getContext(), insumosDisponiveis);
        holder.spinnerInsumo.setAdapter(spinnerAdapter);

        // Preselecionar o insumo se já estiver definido
        if (insumoQuantidade.getId() != 0) {
            for (int i = 0; i < insumosDisponiveis.size(); i++) {
                if (insumosDisponiveis.get(i).getId() == insumoQuantidade.getId()) {
                    holder.spinnerInsumo.setSelection(i);
                    break;
                }
            }
        }

        // Definir a quantidade
        holder.etQuantidade.setText(insumoQuantidade.getQuantidade() > 0 ? String.valueOf(insumoQuantidade.getQuantidade()) : "");

        // Listener para capturar a seleção do insumo
        holder.spinnerInsumo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selectedPosition, long id) {
                Insumo selectedInsumo = (Insumo) parent.getItemAtPosition(selectedPosition);
                insumoQuantidade.setId(selectedInsumo.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                insumoQuantidade.setId(0);
            }
        });

        // Listener para capturar a quantidade inserida
        holder.etQuantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int quantidade = Integer.parseInt(s.toString());
                    insumoQuantidade.setQuantidade(quantidade);
                } catch (NumberFormatException e) {
                    insumoQuantidade.setQuantidade(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar
            }
        });

        // Listener para remover o insumo
        holder.btnRemoverInsumo.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                insumos.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insumos.size();
    }

    static class InsumoQuantidadeViewHolder extends RecyclerView.ViewHolder {
        Spinner spinnerInsumo;
        EditText etQuantidade;
        Button btnRemoverInsumo;

        public InsumoQuantidadeViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerInsumo = itemView.findViewById(R.id.spinnerInsumo);
            etQuantidade = itemView.findViewById(R.id.etQuantidade);
            btnRemoverInsumo = itemView.findViewById(R.id.btnRemoverInsumo);
        }
    }
}
