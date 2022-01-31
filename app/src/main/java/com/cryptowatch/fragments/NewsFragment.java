package com.cryptowatch.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptowatch.R;
import com.cryptowatch.adapters.NewsRecyclerViewAdapter;
import com.cryptowatch.interfaces.NewsClickListener;
import com.cryptowatch.models.NewsArticle;
import com.cryptowatch.viewmodels.NewsViewModel;

public class NewsFragment extends Fragment implements NewsClickListener {
    private NewsViewModel viewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getNews().observe(getViewLifecycleOwner(), data -> {
            recyclerView.setAdapter(new NewsRecyclerViewAdapter(getContext(), data, this));
        });
        return view;
    }

    @Override
    public void onNewsClicked(NewsArticle article) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        System.out.println(article.getUrl());
        intent.setData(Uri.parse(article.getUrl()));
        startActivity(intent);
    }
}