package com.cryptowatch.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cryptowatch.api.CryptoCompareService;
import com.cryptowatch.models.NewsArticle;
import com.cryptowatch.utilities.Constants;
import com.cryptowatch.utilities.NewsListDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    public MutableLiveData<List<NewsArticle>> getNews() {
        MutableLiveData<List<NewsArticle>> data = new MutableLiveData<>();

        CryptoCompareService service = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<NewsArticle>>() {}.getType(), new NewsListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<NewsArticle>> call = service.getLatestNews(Constants.LANGUAGE);

        call.enqueue(new Callback<List<NewsArticle>>() {
            @Override
            public void onResponse(Call<List<NewsArticle>> call, Response<List<NewsArticle>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<NewsArticle>> call, Throwable t) {
                Log.d("getCurrencies", t.getMessage());
                // FIXME: static string
            }
        });

        return data;
    }
}
