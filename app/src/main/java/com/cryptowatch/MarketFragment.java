package com.cryptowatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cryptowatch.adapters.CurrencyRecyclerViewAdapter;
import com.cryptowatch.data.Database;
import com.cryptowatch.interfaces.CurrencyClickListener;
import com.cryptowatch.models.Currency;
import com.cryptowatch.viewmodels.ListViewModel;
import com.cryptowatch.viewmodels.ListViewModelFactory;

public class MarketFragment extends Fragment implements CurrencyClickListener {
    private ListViewModel viewModel;
    private CurrencyRecyclerViewAdapter adapter;

    public MarketFragment() {
        // Required empty public constructor
    }

    public static MarketFragment newInstance() {
        return new MarketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(requireActivity(),
                new ListViewModelFactory(new Database(requireActivity()))).get(ListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cryptocurrencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getMarket().observe(getViewLifecycleOwner(), data -> {
            adapter = new CurrencyRecyclerViewAdapter(getContext(), data, this);
            viewModel.checkMarketInPortfolio();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        });
        viewModel.getPortfolio().observe(getViewLifecycleOwner(), data -> refreshView());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        viewModel.checkMarketInPortfolio();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCurrencyClick(Currency currency) {
        Intent intent = new Intent(getActivity(), CurrencyActivity.class);
        intent.putExtra(CurrencyActivity.CURRENCY_INTENT_KEY, currency);
        startActivity(intent);
    }

    @Override
    public void onPortfolioClick(Currency currency, boolean checked) {
        viewModel.handlePortfolioChange(currency, checked);
    }
}