package com.cryptowatch.interfaces;

import com.cryptowatch.models.Currency;

public interface CurrencyClickListener {
    void onCurrencyClick(Currency currency);
    void onPortfolioClick(Currency currency);
}
