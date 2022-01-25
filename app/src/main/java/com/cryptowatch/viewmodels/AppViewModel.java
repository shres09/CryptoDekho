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
import com.cryptowatch.api.CryptoOhlcvDeserializer;
import com.cryptowatch.api.RetrofitClientInstance;
import com.cryptowatch.api.CryptocurrencyListDeserializer;
import com.cryptowatch.models.CryptoOhlcv;
import com.cryptowatch.models.Cryptocurrency;
import com.google.gson.reflect.TypeToken;

public class AppViewModel extends ViewModel {
    private MutableLiveData<List<Cryptocurrency>> data;
    private MutableLiveData<Cryptocurrency> selected = new MutableLiveData<>();
    private MutableLiveData<List<CryptoOhlcv>> ohlcv = new MutableLiveData<>();

    public LiveData<List<Cryptocurrency>> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
            fetchData();
        }
        return data;
    }

    public LiveData<Cryptocurrency> getSelected() {
        return this.selected;
    }

    public LiveData<List<CryptoOhlcv>> getOhlcv() { return this.ohlcv; }

    public void selectData(Cryptocurrency cryptocurrency) {
        this.selected.setValue(cryptocurrency);
        fetchOhlcv();
    }

    // TODO: Move to repository/service
    protected void fetchData() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Cryptocurrency>>() {}.getType(), new CryptocurrencyListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<Cryptocurrency>> getCurrencies = service.getToplistByMarketCap();

        getCurrencies.enqueue(new Callback<List<Cryptocurrency>>() {
            @Override
            public void onResponse(Call<List<Cryptocurrency>> call, Response<List<Cryptocurrency>> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Cryptocurrency>> call, Throwable t) {
                Log.e("getTopListByMarketCap", t.getMessage());
            }
        });
    }

    // TODO: Move to repository/service
    protected void fetchOhlcv() {
        CryptoCompareService service = RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<CryptoOhlcv>>() {}.getType(), new CryptoOhlcvDeserializer())
                .create(CryptoCompareService.class);

        Call<List<CryptoOhlcv>> getOhlcv = service.getDailyOhlcv(selected.getValue().getId());

        getOhlcv.enqueue(new Callback<List<CryptoOhlcv>>() {
            @Override
            public void onResponse(Call<List<CryptoOhlcv>> call, Response<List<CryptoOhlcv>> response) {
                ohlcv.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CryptoOhlcv>> call, Throwable t) {
                Log.e("fetchOhlcv", t.getMessage());
            }
        });
    }
}
