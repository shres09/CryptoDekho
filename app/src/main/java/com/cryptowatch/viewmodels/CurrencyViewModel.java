package com.cryptowatch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cryptowatch.data.CurrencyRepository;
import com.cryptowatch.data.Database;
import com.cryptowatch.data.PortfolioRepository;
import com.cryptowatch.models.Currency;
import com.cryptowatch.models.Ohlc;

import java.util.List;

public class CurrencyViewModel extends ViewModel {
    private CurrencyRepository currencyRepository;
    private PortfolioRepository portfolioRepository;

    private MutableLiveData<Currency> currency;
    private MutableLiveData<List<Ohlc>> ohlc;

    public CurrencyViewModel(Currency currency, Database database) {
        this.currencyRepository = new CurrencyRepository(currency);
        this.portfolioRepository = new PortfolioRepository(database);

        this.currency = currencyRepository.getCurrencyValue();
        this.ohlc = currencyRepository.getOhlc(currency.getId(), "hourly", 24);
    }

    public LiveData<Currency> getCurrency() {
        return currency;
    }

    public LiveData<List<Ohlc>> getOhlc() {
        return ohlc;
    }

    public void fetchOhlc(String type, int count) {
        currencyRepository.getOhlc(currency.getValue().getId(), type, count);
    }


    // FIXME: refactor this methods on all places
    public void handlePortfolioChange(Currency currency) {
        if (!isInPortfolio(currency)) {
            addToPortfolio(currency);
        }
        else {
            removeFromPortfolio(currency);
        }
    }

    public boolean isInPortfolio(Currency currency) {
        return portfolioRepository.isCurrencyInserted(currency);
    }

    public void addToPortfolio(Currency currency) {
        portfolioRepository.insertCurrency(currency);
        currency.setInPortfolio(true);
    }

    public void removeFromPortfolio(Currency currency) {
        portfolioRepository.deleteCurrency(currency);
        currency.setInPortfolio(false);
    }
}
