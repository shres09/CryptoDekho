package com.cryptowatch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

import com.cryptowatch.api.CryptoCompareService;
import com.cryptowatch.api.OhlcListDeserializer;
import com.cryptowatch.api.RetrofitClientInstance;
import com.cryptowatch.api.CurrencyListDeserializer;
import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.google.gson.reflect.TypeToken;

public class AppViewModel extends ViewModel {
    private MutableLiveData<List<Currency>> data;
    private MutableLiveData<Currency> selected = new MutableLiveData<>();
    private MutableLiveData<List<Ohlc>> ohlcv = new MutableLiveData<>();

    public LiveData<List<Currency>> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
            fetchData();
        }
        return data;
    }

    public LiveData<Currency> getSelected() {
        return this.selected;
    }

    public LiveData<List<Ohlc>> getOhlcv() { return this.ohlcv; }

    public void selectData(Currency currency) {
        this.selected.setValue(currency);
        fetchOhlcv();
    }

    // TODO: Move to repository/service
    protected void fetchData() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Currency>>() {}.getType(), new CurrencyListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Currency>> getCurrencies = service.getToplistByMarketCap();

        getCurrencies.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Log.e("getTopListByMarketCap", t.getMessage());
            }
        });
    }

    // TODO: Move to repository/service
    protected void fetchOhlcv() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Ohlc>>() {}.getType(), new OhlcListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Ohlc>> getOhlcv = service.getDailyOhlcv(selected.getValue().getId());

        getOhlcv.enqueue(new Callback<List<Ohlc>>() {
            @Override
            public void onResponse(Call<List<Ohlc>> call, Response<List<Ohlc>> response) {
                ohlcv.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Ohlc>> call, Throwable t) {
                Log.e("fetchOhlcv", t.getMessage());
            }
        });
    }
}
