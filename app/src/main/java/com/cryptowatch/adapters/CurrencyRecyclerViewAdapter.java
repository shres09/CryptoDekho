package com.cryptowatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.cryptowatch.R;
import com.cryptowatch.interfaces.CurrencyClickListener;
import com.cryptowatch.models.Currency;
import com.squareup.picasso.Picasso;

public class CurrencyRecyclerViewAdapter
        extends RecyclerView.Adapter<CurrencyRecyclerViewAdapter.CurrencyViewHolder> implements Filterable {

    private final LayoutInflater inflater;
    private final List<Currency> list;
    private final List<Currency> filteredList;
    private final CurrencyClickListener clickListener;

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Currency> newFilteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                newFilteredList.addAll(list);
            }
            else {
                String filter = constraint.toString().toLowerCase().trim();
                for (Currency currency : list) {
                    if (currency.getId().toLowerCase().contains(filter) || currency.getName().toLowerCase().contains(filter)) {
                        newFilteredList.add(currency);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = newFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public CurrencyRecyclerViewAdapter(Context context, List<Currency> list, CurrencyClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.filteredList = new ArrayList<>(list);
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
        Currency currency = filteredList.get(position);
        holder.id.setText(currency.getId());
        holder.name.setText(currency.getName());
        holder.price.setText(String.valueOf(currency.getPrice()));
        Picasso.get().load("https://www.cryptocompare.com/" + currency.getImage()).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    protected class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView logo;
        private final TextView id;
        private final TextView name;
        private final TextView price;

        public CurrencyViewHolder(View view) {
            super(view);
            logo = view.findViewById(R.id.imageCurrency);
            id = view.findViewById(R.id.labelId);
            name = view.findViewById(R.id.labelName);
            price = view.findViewById(R.id.labelPrice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Currency currency = list.get(getAdapterPosition());
            clickListener.onCurrencyClick(currency);
        }
    }
}
