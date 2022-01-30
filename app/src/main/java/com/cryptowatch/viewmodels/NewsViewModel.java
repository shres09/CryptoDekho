package com.cryptowatch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

import com.cryptowatch.network.CryptoCompareService;
import com.cryptowatch.utils.NewsListDeserializer;
import com.cryptowatch.models.NewsArticle;
import com.google.gson.reflect.TypeToken;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<List<NewsArticle>> news;

    public LiveData<List<NewsArticle>> getNews() {
        if (news == null) {
            news = new MutableLiveData<>();
            fetchNews();
        }
        return news;
    }

    // TODO: Move to repository/service
    protected void fetchNews() {
        CryptoCompareService service = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<NewsArticle>>() {}.getType(), new NewsListDeserializer())
                .create(CryptoCompareService.class);

        Call<List<NewsArticle>> getNews = service.getLatestNews();

        getNews.enqueue(new Callback<List<NewsArticle>>() {
            @Override
            public void onResponse(Call<List<NewsArticle>> call, Response<List<NewsArticle>> response) {
                news.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<NewsArticle>> call, Throwable t) {
                Log.e("getCurrencies", t.getMessage());
            }
        });
    }
}
