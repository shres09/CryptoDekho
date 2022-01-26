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
import com.cryptowatch.interfaces.NewsClickListener;
import com.cryptowatch.models.NewsArticle;

public class NewsRecyclerViewAdapter
        extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private final LayoutInflater inflater;
    private final List<NewsArticle> data;
    private final NewsClickListener clickListener;

    public NewsRecyclerViewAdapter(Context context, List<NewsArticle> data, NewsClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_article_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = data.get(position);
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView description;

        public NewsViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.labelTitle);
            description = view.findViewById(R.id.labelDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NewsArticle article = data.get(getAdapterPosition());
            clickListener.onNewsClicked(article);
        }
    }
}
