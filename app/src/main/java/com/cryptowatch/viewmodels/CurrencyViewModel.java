package com.cryptowatch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

import com.cryptowatch.data.CurrencyRepository;
import com.cryptowatch.network.CryptoCompareService;
import com.cryptowatch.utils.OhlcListDeserializer;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.google.gson.reflect.TypeToken;

public class CurrencyViewModel extends ViewModel {
    private CurrencyRepository repository;

    private MutableLiveData<Currency> currency;
    private MutableLiveData<List<Ohlc>> ohlc;

    public CurrencyViewModel() {

    }

    public CurrencyViewModel(Currency currency, CurrencyRepository repository) {
        this.currency = new MutableLiveData<>(currency);
        this.repository = repository;
    }

    public LiveData<Currency> getCurrency() {
        if (currency == null) {
            currency = new MutableLiveData<>();
        }

        return this.currency;
    }

    public LiveData<List<Ohlc>> getOhlc() {
        if (ohlc == null) {
            ohlc = new MutableLiveData<>();
            fetchOhlc("hourly", 24);
        }

        return ohlc;
    }

    public void handlePortfolioChange(Currency currency) {
        if (!isInPortfolio(currency)) {
            addToPortfolio(currency);
        }
        else {
            removeFromPortfolio(currency);
        }
    }

    public boolean isInPortfolio(Currency currency) {
        return repository.isCurrencyInserted(currency);
    }

    public void addToPortfolio(Currency currency) {
        repository.insertCurrency(currency);
        currency.setInPortfolio(true);
    }

    public void removeFromPortfolio(Currency currency) {
        repository.deleteCurrency(currency);
        currency.setInPortfolio(false);
    }

    // TODO: Move to service
    // FIXME: Type could be enum
    public void fetchOhlc(String type, int count) {
        CryptoCompareService service = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Ohlc>>() {}.getType(), new OhlcListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Ohlc>> getOhlc;

        switch (type) {
            case "minute":
                getOhlc = service.getOhlcMinute(currency.getValue().getId(), count);
                break;
            case "hourly":
                getOhlc = service.getOhlcHourly(currency.getValue().getId(), count);
                break;
            case "daily":
                getOhlc = service.getOhlcDaily(currency.getValue().getId(), count);
                break;
            default:
                return;
        }

        getOhlc.enqueue(new Callback<List<Ohlc>>() {
            @Override
            public void onResponse(Call<List<Ohlc>> call, Response<List<Ohlc>> response) {
                ohlc.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Ohlc>> call, Throwable t) {
                Log.e("getOhlc", t.getMessage());
            }
        });
    }
}
