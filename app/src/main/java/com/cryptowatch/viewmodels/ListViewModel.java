package com.cryptowatch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cryptowatch.data.Database;
import com.cryptowatch.data.ListRepository;
import com.cryptowatch.data.PortfolioRepository;
import com.cryptowatch.models.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewModel extends ViewModel {
    private ListRepository listRepository;
    private PortfolioRepository portfolioRepository;

    private MutableLiveData<List<Currency>> currencies;
    private MutableLiveData<List<Currency>> market;
    private MutableLiveData<List<Currency>> portfolio;
    private MutableLiveData<List<Currency>> search;

    public ListViewModel(Database database) {
        this.listRepository = new ListRepository();
        this.portfolioRepository = new PortfolioRepository(database);

        this.currencies = listRepository.getAllCurrencies();
        this.market = listRepository.getCurrenciesByTopList();
        this.portfolio = portfolioRepository.getCurrenciesFromPortfolio();
        this.search = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencies;
    }

    public LiveData<List<Currency>> getMarket() {
        return market;
    }

    public LiveData<List<Currency>> getPortfolio() {
        return portfolio;
    }

    public LiveData<List<Currency>> getSearch() {
        return search;
    }

    public void setSearch(String query) {
        if (currencies.getValue() == null) {
            return;
        }

        String q = query.trim().toLowerCase();
        List<Currency> list = currencies.getValue()
                .stream()
                .filter(c -> c.getId().toLowerCase().contains(q) || c.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());

        search.setValue(list);
    }

//    public void validateMarketDataInPortfolio() {
//        List<String> currenciesId = portfolioRepository.getAllCurrencyId();
//        market.getValue().stream().forEach(
//                currency -> {
//                    if (currenciesId.contains(currency.getId())) {
//                        currency.setInPortfolio(true);
//                    }
//                }
//        );
//        market.setValue(market.getValue());
//    }

    public void handlePortfolioChange(Currency currency) {
        if (!isInPortfolio(currency)) {
            addToPortfolio(currency);
        }
        else {
            removeFromPortfolio(currency);
        }

        // market.setValue(portfolio.getValue());
        // portfolio.setValue(portfolio.getValue());
    }

    public boolean isInPortfolio(Currency currency) {
        return portfolioRepository.isCurrencyInserted(currency);
    }

    public void addToPortfolio(Currency currency) {
        portfolioRepository.insertCurrency(currency);
        currency.setInPortfolio(true);
        // portfolio.getValue().add(currency);
    }

    public void removeFromPortfolio(Currency currency) {
        portfolioRepository.deleteCurrency(currency);
        currency.setInPortfolio(false);
        // portfolio.getValue().remove(currency);
    }
}
