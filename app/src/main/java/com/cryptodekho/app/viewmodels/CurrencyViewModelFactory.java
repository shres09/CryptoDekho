package com.cryptodekho.app.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cryptodekho.app.repositories.Database;
import com.cryptodekho.app.models.Currency;

public class CurrencyViewModelFactory implements ViewModelProvider.Factory {
    private Currency currency;
    private Database database;

    public CurrencyViewModelFactory(Currency currency, Database database) {
        this.currency = currency;
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new CurrencyViewModel(currency, database);
    }
}
