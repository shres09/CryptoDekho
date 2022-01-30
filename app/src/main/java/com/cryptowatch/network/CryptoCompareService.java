package com.cryptowatch.network;

import com.cryptowatch.models.Ohlc;
import com.cryptowatch.models.Currency;
import com.cryptowatch.models.NewsArticle;
import com.cryptowatch.models.Value;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CryptoCompareService {
    @GET("top/mktcapfull?tsym=EUR&limit=13") // FIXME: works with 13, doesnt work with 14
    Call<List<Currency>> getToplistByMarketCap();

    @GET("all/coinlist")
    Call<Currency> getCurrency(@Query("fsym") String id);

    @GET("pricemultifull?tsyms=EUR")
    Call<Value> getCurrencyValue(@Query("fsyms") String id);

    @GET("v2/histominute?tsym=EUR")
    Call<List<Ohlc>> getOhlcMinute(@Query("fsym") String id, @Query("limit") int count);

    @GET("v2/histohour?tsym=EUR")
    Call<List<Ohlc>> getOhlcHourly(@Query("fsym") String id, @Query("limit") int count);

    @GET("v2/histoday?tsym=EUR")
    Call<List<Ohlc>> getOhlcDaily(@Query("fsym") String id, @Query("limit") int count);

    @GET("v2/news/?lang=EN")
    Call<List<NewsArticle>> getLatestNews();

    class RetrofitClientInstance {
        private static final String BASE_URL = "https://min-api.cryptocompare.com/data/";

        private static Converter.Factory createGsonConverter(Type type, Object typeAdapter) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(type, typeAdapter);
            Gson gson = gsonBuilder.create();

            return GsonConverterFactory.create(gson);
        }

        public static Retrofit getRetrofitInstance(Type type, Object typeAdapter) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(createGsonConverter(type, typeAdapter))
                    .build();
        }
    }
}
