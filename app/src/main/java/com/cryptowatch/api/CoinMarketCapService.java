package com.cryptowatch.api;

import com.cryptowatch.models.CryptocurrencyList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface CoinMarketCapService {

    @Headers("X-CMC_PRO_API_KEY: a98a54b1-a3e3-43c8-893e-b71285e15143")
    @GET("cryptocurrency/listings/latest")
    Call<CryptocurrencyList> getCurrencies();

}
