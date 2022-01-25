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
import com.cryptowatch.interfaces.CurrencyClickListener;
import com.cryptowatch.models.Currency;

public class CurrencyRecyclerViewAdapter
        extends RecyclerView.Adapter<CurrencyRecyclerViewAdapter.CurrencyViewHolder> {

    private final LayoutInflater inflater;
    private final List<Currency> data;
    private final CurrencyClickListener clickListener;

    public CurrencyRecyclerViewAdapter(Context context, List<Currency> data, CurrencyClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cryptocurrency_layout, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        Currency currency = data.get(position);
        holder.getId().setText(currency.getId());
        holder.getName().setText(currency.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id;
        private TextView name;

        public CurrencyViewHolder(View view) {
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
            Currency currency = data.get(getAdapterPosition());
            clickListener.onCurrencyClick(currency);
        }
    }
}
