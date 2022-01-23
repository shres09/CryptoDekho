package com.cryptowatch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptowatch.R;
import com.cryptowatch.adapters.CryptocurrencyRecyclerViewAdapter;
import com.cryptowatch.interfaces.CryptocurrencyClickListener;
import com.cryptowatch.models.Cryptocurrency;
import com.cryptowatch.viewmodels.AppViewModel;

public class CryptocurrencyFragment extends Fragment implements CryptocurrencyClickListener {

    private AppViewModel viewModel;

    public CryptocurrencyFragment() {
        // Required empty public constructor
    }

    public static CryptocurrencyFragment newInstance() {
        return new CryptocurrencyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cryptocurrency, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cryptocurrencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            recyclerView.setAdapter(new CryptocurrencyRecyclerViewAdapter(getContext(), data, this));
        });
        return view;
    }

    @Override
    public void onCurrencyClick(Cryptocurrency cryptocurrency) {
        viewModel.selectData(cryptocurrency);
    }

}