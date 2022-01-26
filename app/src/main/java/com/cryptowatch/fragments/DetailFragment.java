package com.cryptowatch.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowatch.R;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.cryptowatch.viewmodels.CurrencyViewModel;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private CurrencyViewModel viewModel;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        LinearLayout layout = view.findViewById(R.id.mainLayout);
        renderInfo(layout, viewModel.getSelected().getValue()); // FIXME: ugly?
        viewModel.getOhlc().observe(getViewLifecycleOwner(), ohlc -> renderChart(layout, ohlc));
        return view;
    }

    // FIXME: ugly
    private void renderInfo(ViewGroup container, Currency currency) {
        ((TextView) container.findViewById(R.id.labelDetailId)).setText(currency.getId());
        ((TextView) container.findViewById(R.id.labelDetailName)).setText(currency.getName());

        // FIXME: ugly AF
        Button btnPortfolio = container.findViewById(R.id.btnPortfolio);
        if (!viewModel.isInPortfolio(currency))
        {
            btnPortfolio.setText("Add to portfolio");
        }
        else {
            btnPortfolio.setText("Remove from portfolio");
        }

        btnPortfolio.setOnClickListener(v -> {
            if (!viewModel.isInPortfolio(currency))
            {
                viewModel.addToPortfolio(currency);
                ((Button) v).setText("Remove from portfolio");
            }
            else {
                viewModel.removeFromPortfolio(currency);
                ((Button) v).setText("Add to portfolio");
            }
        });
    }

    private void renderChart(ViewGroup container, List<Ohlc> ohlc) {
        LineChart chart = container.findViewById(R.id.lineChart);

        List<Entry> entries = new ArrayList<>();
        for (Ohlc o : ohlc) {
            entries.add(new Entry(o.getTime(), (float) o.getClose())); // FIXME: change all to float?
        }

        LineDataSet dataSet = new LineDataSet(entries, "SOME TEXT");
        dataSet.setColor(Color.GREEN);
        // dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}