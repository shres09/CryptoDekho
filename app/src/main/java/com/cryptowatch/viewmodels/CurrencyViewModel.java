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
import com.cryptowatch.network.CryptoCompareService;
import com.cryptowatch.network.CurrencyDeserializer;
import com.cryptowatch.network.OhlcListDeserializer;
import com.cryptowatch.network.PriceDeserializer;
import com.cryptowatch.network.RetrofitClientInstance;
import com.cryptowatch.network.CurrencyListDeserializer;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.google.gson.reflect.TypeToken;

public class CurrencyViewModel extends ViewModel {
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

    public boolean isInPortfolio(Currency currency) {
        return repository.isCurrencyInserted(currency);
    }

    public void addToPortfolio(Currency currency) {
        repository.insertCurrency(currency);
        getPortfolio(); // FIXME: kinda ugly
        portfolio.getValue().add(currency);
    }

    public void removeFromPortfolio(Currency currency) {
        repository.deleteCurrency(currency);
        getPortfolio(); // FIXME: kinda ugly
        portfolio.getValue().remove(currency);
    }

    public void selectData(Currency currency) {
        this.selected.setValue(currency);
        fetchOhlc();
    }

    // TODO: Move to service
    protected void fetchMarket() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Currency>>() {}.getType(), new CurrencyListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Currency>> getMarket = service.getToplistByMarketCap();

        getMarket.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                market.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Log.e("getTopListByMarketCap", t.getMessage());
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

        portfolio.setValue(currencies);

        currenciesId.removeIf(id -> currencies.stream().anyMatch(c -> c.getId().equals(id)));

        CryptoCompareService currencyService = RetrofitClientInstance
                .getRetrofitInstance(Currency.class, new CurrencyDeserializer())
                .create(CryptoCompareService.class);

        CryptoCompareService priceService = RetrofitClientInstance
                .getRetrofitInstance(Double.class, new PriceDeserializer())
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
                fetchPrice(response.body(), priceService);
            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {
                Log.e("getCurrency", t.getMessage());
            }
        });
    }

    protected void fetchPrice(Currency currency, CryptoCompareService service) {
        Call<Double> getPrice = service.getCurrencyPrice(currency.getId());

        getPrice.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                currency.setPrice(response.body());
                portfolio.getValue().add(currency);
                portfolio.setValue(portfolio.getValue());
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.e("getPrice", t.getMessage());
            }
        });
    }

    // TODO: Move to service
    protected void fetchOhlc() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Ohlc>>() {}.getType(), new OhlcListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Ohlc>> getOhlc = service.getDailyOhlc(selected.getValue().getId());

        getOhlc.enqueue(new Callback<List<Ohlc>>() {
            @Override
            public void onResponse(Call<List<Ohlc>> call, Response<List<Ohlc>> response) {
                ohlc.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Ohlc>> call, Throwable t) {
                Log.e("fetchOhlc", t.getMessage());
            }
        });
    }
}
