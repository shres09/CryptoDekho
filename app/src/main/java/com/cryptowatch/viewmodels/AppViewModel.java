package com.cryptowatch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

import com.cryptowatch.api.CoinMarketCapService;
import com.cryptowatch.models.Cryptocurrency;
import com.cryptowatch.models.CryptocurrencyList;

public class AppViewModel extends ViewModel {

    private MutableLiveData<List<Cryptocurrency>> data;
    private MutableLiveData<Cryptocurrency> selected = new MutableLiveData<>();

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

    public void selectData(Cryptocurrency cryptocurrency) {
        this.selected.setValue(cryptocurrency);
    }

    // TODO: Move to repository/service
    protected void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://pro-api.coinmarketcap.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CoinMarketCapService service = retrofit.create(CoinMarketCapService.class);

        Call<CryptocurrencyList> crypto = service.getCurrencies();

        crypto.enqueue(new Callback<CryptocurrencyList>() {
            @Override
            public void onResponse(Call<CryptocurrencyList> call, Response<CryptocurrencyList> response) {
                data.postValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<CryptocurrencyList> call, Throwable t) {
                System.out.println("yo");
            }
        });
    }

}
