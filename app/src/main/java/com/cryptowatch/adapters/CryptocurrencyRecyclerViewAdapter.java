package com.cryptowatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.cryptowatch.R;
import com.cryptowatch.interfaces.ListClickListener;
import com.cryptowatch.models.Cryptocurrency;

public class CryptocurrencyRecyclerViewAdapter
        extends RecyclerView.Adapter<CryptocurrencyRecyclerViewAdapter.CryptocurrencyViewHolder> {

    private final LayoutInflater inflater;
    private final List<Cryptocurrency> data;
    private final ListClickListener clickListener;

    public CryptocurrencyRecyclerViewAdapter(Context context, List<Cryptocurrency> data, ListClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CryptocurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cryptocurrency_layout, parent, false);
        return new CryptocurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptocurrencyViewHolder holder, int position) {
        Cryptocurrency cryptocurrency = data.get(position);
        holder.getId().setText(cryptocurrency.getId());
        holder.getName().setText(cryptocurrency.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CryptocurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id;
        private TextView name;

        public CryptocurrencyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.labelId);
            name = view.findViewById(R.id.labelName);
            view.setOnClickListener(this);
        }

        public TextView getId() {
            return id;
        }

        public void setId(TextView id) {
            this.id = id;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            Cryptocurrency cryptocurrency = data.get(getAdapterPosition());
            clickListener.onCurrencyClick(cryptocurrency);
        }
    }
}
