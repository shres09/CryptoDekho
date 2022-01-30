package com.cryptowatch.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cryptowatch.data.CurrencyRepository;
import com.cryptowatch.data.Database;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.cryptowatch.R;
import com.cryptowatch.viewmodels.ListViewModel;
import com.cryptowatch.models.Currency;

public class MainActivity extends AppCompatActivity {
    private ListViewModel viewModel;
    private NavController nvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    protected void initComponents() {
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        viewModel.setRepository(new CurrencyRepository(new Database(this)));
        viewModel.getSelected().observe(this, this::navigate);

        BottomNavigationView nav = findViewById(R.id.navbar);
        AppBarConfiguration abc = new AppBarConfiguration.Builder(R.id.market, R.id.portfolio, R.id.news).build();
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragment);
        nvc = nhf.getNavController();
        NavigationUI.setupActionBarWithNavController(this, nvc, abc);
        NavigationUI.setupWithNavController(nav, nvc);
    }

    protected void navigate(Currency currency) {
        Intent intent = new Intent(this, CurrencyActivity.class);
        intent.putExtra(CurrencyActivity.CURRENCY_INTENT_KEY, currency);
        startActivity(intent);
    }
}