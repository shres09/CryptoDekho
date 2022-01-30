package com.cryptowatch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.squareup.picasso.Target;

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
        // FIXME: move to static string

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.logo.setImageBitmap(bitmap);
                currency.setImage(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        Picasso.get().load("https://www.cryptocompare.com/" + currency.getImageUrl()).into(target);


        holder.id.setText(currency.getId());
        holder.name.setText(currency.getName());
        holder.price.setText(currency.getValue().getPrice());
        holder.marketCap.setText("MCap " + currency.getValue().getMarketCap());
        holder.percentChange.setText(currency.getValue().getChange24H());
        holder.percentChange.setTextColor((currency.getValue().getChange24H().contains("-")) ? Color.RED : Color.GREEN);
        holder.portfolio.setChecked(currency.isInPortfolio());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    protected class CurrencyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView logo;
        private final TextView id;
        private final TextView name;
        private final TextView price;
        private final TextView marketCap;
        private final TextView percentChange;
        private final CheckBox portfolio;

        public CurrencyViewHolder(View view) {
            super(view);

            logo = view.findViewById(R.id.imageCurrency);
            id = view.findViewById(R.id.labelId);
            name = view.findViewById(R.id.labelName);
            price = view.findViewById(R.id.labelPrice);
            marketCap = view.findViewById(R.id.labelMarketCap);
            percentChange = view.findViewById(R.id.labelPercentChange);
            portfolio = view.findViewById(R.id.btnPortfolio);

            setupViewClick(view);
            setupPortfolioClick(portfolio);
        }

        public void setupViewClick(View view) {
            view.setOnClickListener(v -> {
                Currency currency = filteredList.get(getAdapterPosition());
                clickListener.onCurrencyClick(currency);
            });
        }

        public void setupPortfolioClick(CheckBox portfolio) {
            portfolio.setOnClickListener(v -> {
                Currency currency = filteredList.get(getAdapterPosition());
                clickListener.onPortfolioClick(currency);
            });
        }
    }
}
