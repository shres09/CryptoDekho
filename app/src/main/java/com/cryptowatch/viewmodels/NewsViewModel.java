package com.cryptowatch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cryptowatch.data.NewsRepository;
import com.cryptowatch.models.NewsArticle;

import java.util.List;

public class NewsViewModel extends ViewModel {
    private NewsRepository repository;

    private MutableLiveData<List<NewsArticle>> news;

    public NewsViewModel() {
        repository = new NewsRepository();
    }

    public LiveData<List<NewsArticle>> getNews() {
        if (news == null) {
            news = repository.getNews();
        }
        return news;
    }
}
