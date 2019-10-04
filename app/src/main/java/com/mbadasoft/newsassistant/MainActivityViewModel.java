package com.mbadasoft.newsassistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;

public class MainActivityViewModel extends AndroidViewModel {
    private AppNewsRepository newsRepository;
    private AppPreferencesRepository preferencesRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        newsRepository = AppNewsRepository.getInstance(application);
        preferencesRepository = AppPreferencesRepository.getInstance(application);
    }

    public LiveData<SourcesResult> getSources() {
        return newsRepository.getSources();
    }

    public LiveData<ArticlesResult> getArticles() {
        return newsRepository.getArticles();
    }

}
