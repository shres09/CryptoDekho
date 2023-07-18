package com.cryptodekho.app.interfaces;

import com.cryptodekho.app.models.Currency;

public interface CurrencyClickListener {
    void onCurrencyClick(Currency currency);
    void onPortfolioClick(Currency currency, boolean checked);
}
