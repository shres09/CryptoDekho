package com.cryptodekho.app.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cryptodekho.app.api.CryptoCompareService;
import com.cryptodekho.app.models.NewsArticle;
import com.cryptodekho.app.utilities.Constants;
import com.cryptodekho.app.utilities.NewsListDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    public MutableLiveData<List<NewsArticle>> getNews() {
        MutableLiveData<List<NewsArticle>> data = new MutableLiveData<>();

        CryptoCompareService service = CryptoCompareService.Client.getService(
                new TypeToken<List<NewsArticle>>() {}.getType(), new NewsListDeserializer());

        Call<List<NewsArticle>> call = service.getLatestNews(Constants.LANGUAGE);

        call.enqueue(new Callback<List<NewsArticle>>() {
            @Override
            public void onResponse(Call<List<NewsArticle>> call, Response<List<NewsArticle>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<NewsArticle>> call, Throwable t) {
                Log.d("getLatestNews", t.getMessage());
            }
        });

        return data;
    }
}
