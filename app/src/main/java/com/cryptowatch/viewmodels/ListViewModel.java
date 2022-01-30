package com.cryptowatch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

import com.cryptowatch.data.CurrencyRepository;
import com.cryptowatch.models.Value;
import com.cryptowatch.network.CryptoCompareService;
import com.cryptowatch.utils.CurrencyDeserializer;
import com.cryptowatch.utils.ValueDeserializer;
import com.cryptowatch.utils.CurrencyListDeserializer;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.google.gson.reflect.TypeToken;

public class ListViewModel extends ViewModel {
    private CurrencyRepository repository;

    private MutableLiveData<List<Currency>> market;
    private MutableLiveData<List<Currency>> portfolio;
    private MutableLiveData<Currency> selected = new MutableLiveData<>();
    private MutableLiveData<List<Ohlc>> ohlc = new MutableLiveData<>();

    public void setRepository(CurrencyRepository repository) {
        this.repository = repository;
    }

    // FIXME: da li ove dve metode moraju ovako
    public LiveData<List<Currency>> getMarket() {
        if (market == null) {
            market = new MutableLiveData<>();
            fetchMarket();
        }

        return market;
    }

    public LiveData<List<Currency>> getPortfolio() {
        if (portfolio == null) {
            portfolio = new MutableLiveData<>();
            fetchPortfolio();
        }

        return portfolio;
    }

    public LiveData<Currency> getSelected() {
        return this.selected;
    }

    public LiveData<List<Ohlc>> getOhlc() { return this.ohlc; }

    public void handlePortfolioChange(Currency currency) {
        if (!isInPortfolio(currency)) {
            addToPortfolio(currency);
        }
        else {
            removeFromPortfolio(currency);
        }
        portfolio.setValue(portfolio.getValue());
    }

    public boolean isInPortfolio(Currency currency) {
        return repository.isCurrencyInserted(currency);
    }

    public void addToPortfolio(Currency currency) {
        repository.insertCurrency(currency);
        currency.setInPortfolio(true);
        portfolio.getValue().add(currency);
    }

    public void removeFromPortfolio(Currency currency) {
        repository.deleteCurrency(currency);
        currency.setInPortfolio(false);
        portfolio.getValue().remove(currency);
    }

    public void selectData(Currency currency) {
        this.selected.setValue(currency);
    }

    // TODO: Move to service
    protected void fetchMarket() {
        CryptoCompareService service = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Currency>>() {}.getType(), new CurrencyListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Currency>> getMarket = service.getToplistByMarketCap();

        getMarket.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                market.setValue(response.body());
                getPortfolio(); // FIXME: kinda ugly
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Log.e("getToplistByMarketCap", t.getMessage());
            }
        });
    }

    // TODO: Move to service
    protected void fetchPortfolio() {
        List<String> currenciesId = repository.getAllCurrencyId();
        List<Currency> currencies = market
                .getValue()
                .stream()
                .filter(c -> currenciesId.contains(c.getId()))
                .collect(Collectors.toList());

        currencies.forEach(c -> c.setInPortfolio(true));

        portfolio.setValue(currencies);

        currenciesId.removeIf(id -> currencies.stream().anyMatch(c -> c.getId().equals(id)));

        CryptoCompareService currencyService = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(Currency.class, new CurrencyDeserializer())
                .create(CryptoCompareService.class);

        CryptoCompareService priceService = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(String[].class, new ValueDeserializer())
                .create(CryptoCompareService.class);

        for (String id : currenciesId) {
            fetchCurrency(id, currencyService, priceService);
        }
    }

    // FIXME: ugly
    protected void fetchCurrency(String id, CryptoCompareService currencyService, CryptoCompareService priceService) {
        Call<Currency> getCurrency = currencyService.getCurrency(id);

        getCurrency.enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(Call<Currency> call, Response<Currency> response) {
                fetchValue(response.body(), priceService);
            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {
                Log.e("getCurrency", t.getMessage());
            }
        });
    }

    protected void fetchValue(Currency currency, CryptoCompareService service) {
        Call<Value> getValue = service.getCurrencyValue(currency.getId());

        getValue.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                currency.setValue(response.body());
                currency.setInPortfolio(repository.isCurrencyInserted(currency));
                portfolio.getValue().add(currency);
                portfolio.setValue(portfolio.getValue());
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.e("getCurrencyValue", t.getMessage());
            }
        });
    }
}
