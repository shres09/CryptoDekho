package com.cryptowatch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.cryptowatch.R;
import com.cryptowatch.adapters.CurrencyRecyclerViewAdapter;
import com.cryptowatch.interfaces.CurrencyClickListener;
import com.cryptowatch.models.Currency;
import com.cryptowatch.viewmodels.ListViewModel;

public class PortfolioFragment extends Fragment implements CurrencyClickListener {
    private ListViewModel viewModel;
    private CurrencyRecyclerViewAdapter adapter;

    public PortfolioFragment() {
        // Required empty public constructor
    }

    public static PortfolioFragment newInstance() {
        return new PortfolioFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cryptocurrencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getPortfolio().observe(getViewLifecycleOwner(), data -> {
            adapter = new CurrencyRecyclerViewAdapter(getContext(), data, this);
            recyclerView.setAdapter(adapter);
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCurrencyClick(Currency currency) {
        viewModel.selectData(currency);
    }

    @Override
    public void onPortfolioClick(Currency currency) {
        viewModel.handlePortfolioChange(currency);
    }
}