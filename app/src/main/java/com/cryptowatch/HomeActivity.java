package com.cryptowatch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private NavController nvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
    }

    protected void initComponents() {
        BottomNavigationView nav = findViewById(R.id.navbar);
        AppBarConfiguration abc = new AppBarConfiguration.Builder(R.id.market, R.id.portfolio, R.id.search, R.id.news).build();
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragment);
        nvc = nhf.getNavController();
        NavigationUI.setupActionBarWithNavController(this, nvc, abc);
        NavigationUI.setupWithNavController(nav, nvc);
    }
}