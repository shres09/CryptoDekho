package com.cryptowatch.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        holder.getTitle().setText(article.getTitle());
        holder.getDescription().setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // FIXME: maybe it doesnt have to be private
        private TextView title;
        private TextView description;

        public NewsViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.labelTitle);
            description = view.findViewById(R.id.labelDescription);
            view.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getDescription() {
            return description;
        }

        public void setDescription(TextView description) {
            this.description = description;
        }

        @Override
        public void onClick(View v) {
            NewsArticle article = data.get(getAdapterPosition());
            clickListener.onNewsClicked(article);
        }
    }
}