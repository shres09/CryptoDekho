package com.cryptowatch.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowatch.R;
import com.cryptowatch.models.CryptoOhlcv;
import com.cryptowatch.models.Cryptocurrency;
import com.cryptowatch.viewmodels.AppViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private AppViewModel viewModel;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        LinearLayout layout = view.findViewById(R.id.mainLayout);
        renderInfo(layout, viewModel.getSelected().getValue()); // FIXME: ugly?
        viewModel.getOhlcv().observe(getViewLifecycleOwner(), ohlcv -> renderChart(layout, ohlcv));
        return view;
    }

    private void renderInfo(ViewGroup container, Cryptocurrency cryptocurrency) {
        ((TextView) container.findViewById(R.id.labelDetailId))
                .setText(cryptocurrency.getId());
        ((TextView) container.findViewById(R.id.labelDetailName))
                .setText(cryptocurrency.getName());
    }

    private void renderChart(ViewGroup container, List<CryptoOhlcv> ohlcv) {
        LineChart chart = container.findViewById(R.id.lineChart);

        List<Entry> entries = new ArrayList<>();
        for (CryptoOhlcv o : ohlcv) {
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