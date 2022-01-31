package com.cryptowatch.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.cryptowatch.R;
import com.cryptowatch.data.Database;
import com.cryptowatch.models.Currency;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.utilities.Constants;
import com.cryptowatch.viewmodels.CurrencyViewModel;
import com.cryptowatch.viewmodels.CurrencyViewModelFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

// FIXME: refactor whole class
public class CurrencyActivity extends AppCompatActivity {
    public static String CURRENCY_INTENT_KEY = "currency";

    private CurrencyViewModel viewModel;
    private boolean inputSourceFocus;
    private boolean inputTargetFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // FIXME: returns on wrong fragment

        Currency c = getIntent().getParcelableExtra(CURRENCY_INTENT_KEY);
        viewModel = new ViewModelProvider(this, new CurrencyViewModelFactory(c, new Database(this)))
                .get(CurrencyViewModel.class);
        viewModel.getCurrency().observe(this, currency -> {
            renderInfo(currency);
            initConversion(currency);
        });
        viewModel.getOhlc().observe(this, ohlc -> renderChart(ohlc));
        renderChipGroup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        MenuItem portfolioItem = menu.findItem(R.id.action_portfolio);
        CheckBox btnPortfolio = portfolioItem.getActionView().findViewById(R.id.checkBox);
        Currency currency = viewModel.getCurrency().getValue();
        btnPortfolio.setChecked(viewModel.isInPortfolio(currency));
        // FIXME: Use isChecked variable
        btnPortfolio.setOnCheckedChangeListener((v, isChecked) -> viewModel.handlePortfolioChange(currency));
        return true;
    }

    private void renderInfo(Currency currency) {
        getSupportActionBar().setTitle(currency.getId());

        Bitmap bitmap = currency.getImage();
        if (bitmap != null) {
            // FIXME:
            getSupportActionBar().setLogo(new BitmapDrawable(this.getResources(), bitmap));
            ImageView image = findViewById(R.id.imageLogo);
            image.setImageBitmap(bitmap);
        }

        TextView labelName = findViewById(R.id.labelDetailName);
        TextView labelConversionCurrency = findViewById(R.id.labelConversionCurrency);

        TextView labelPrice = findViewById(R.id.labelDetailPrice);
        TextView labelMarketCap = findViewById(R.id.labelDetailMarketCap);
        TextView labelSupply = findViewById(R.id.labelSupply);
        TextView label1hChange = findViewById(R.id.label1hChange);
        TextView label24hChange = findViewById(R.id.label24hChange);
        TextView label24hVolume = findViewById(R.id.label24hVolume);
        TextView label24hHigh = findViewById(R.id.label24hHigh);
        TextView label24hLow = findViewById(R.id.label24hLow);

        labelName.setText(currency.getName());
        labelConversionCurrency.setText(Constants.CONVERSION_CURRENCY);

        // FIXME: XD
        if (currency.getValue() == null) {
            return;
        }

        labelPrice.setText(currency.getValue().getPrice());
        labelMarketCap.setText(currency.getValue().getMarketCap());
        labelSupply.setText(currency.getValue().getSupply());
        label1hChange.setText(currency.getValue().getChange1H());
        label24hChange.setText(currency.getValue().getChange24H());
        label24hVolume.setText(currency.getValue().getVolume24H());
        label24hHigh.setText(currency.getValue().getHigh24H());
        label24hLow.setText(currency.getValue().getLow24H());
    }

    private void initConversion(Currency currency) {
        EditText inputSource = findViewById(R.id.inputSource);
        EditText inputTarget = findViewById(R.id.inputTarget);

        inputSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!inputSourceFocus) {
                    return;
                }

                try {
                    double input = Double.parseDouble(s.toString());
                    inputTarget.setText(String.valueOf(input * currency.getValue().getRawPrice()));
                }
                catch (NumberFormatException e) {
                    inputTarget.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!inputTargetFocus) {
                    return;
                }

                try {
                    double input = Double.parseDouble(s.toString());
                    inputSource.setText(String.valueOf(input / currency.getValue().getRawPrice()));
                }
                catch (NumberFormatException e) {
                    inputSource.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputSource.setOnFocusChangeListener((view, focus) -> {
            inputSourceFocus = focus;
        });

        inputTarget.setOnFocusChangeListener((view, focus) -> {
            inputTargetFocus = focus;
        });
    }

    private void renderChart(List<Ohlc> ohlc) {
        LineChart chart = findViewById(R.id.lineChart);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i("I", e.getX() + " " + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        YAxis yAxis = chart.getAxisLeft();
        // yAxis.setGridColor(Color.BLACK);

        // chart.setTouchEnabled(false);
        // TODO: set highlighting


        List<Entry> entries = new ArrayList<>();
        for (Ohlc o : ohlc) {
            entries.add(new Entry(o.getTime(), (float) o.getClose())); // FIXME: change all to float?
        }

        LineDataSet dataSet = new LineDataSet(entries, "SOME TEXT");
        dataSet.setColor(Color.BLUE);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.chart_gradient));

        dataSet.setHighlightEnabled(true);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    private void renderChipGroup() {
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = chipGroup.findViewById(checkedId);
            String text = chip.getText().toString();
            // FIXME: use id instead of text
            switch (text) {
                // FIXME: static string
                case "1H":
                    viewModel.fetchOhlc("minute", 60);
                    break;
                case "24H":
                    viewModel.fetchOhlc("hourly", 24);
                    break;
                case "7D":
                    viewModel.fetchOhlc("hourly", 168);
                    break;
                case "1M":
                    viewModel.fetchOhlc("daily", 30);
                    break;
                case "3M":
                    viewModel.fetchOhlc("daily", 90);
                    break;
                case "1Y":
                    viewModel.fetchOhlc("daily", 365);
                    break;
                case "MAX":
                    viewModel.fetchOhlc("daily", 2000);
                    break;
            }
        });
    }
}
