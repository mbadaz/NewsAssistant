package com.peruzal.newsassistant.newsActivity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.DataRepository;
import com.peruzal.newsassistant.data.models.Article;
import com.peruzal.newsassistant.data.models.ArticlesResult;
import com.peruzal.newsassistant.data.models.Source;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class NewsActivityViewModel extends AndroidViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    DataRepository dataRepository;
    SharedPreferences sharedPreferences;
    Map<String, String> preferredSources = new HashMap<>();

    public NewsActivityViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void loadPreferences() {
        Set<String> sources = sharedPreferences.getStringSet(Constants.SOURCES, null);
        StringJoiner joiner = new StringJoiner(",");
        for (String source : sources) {
            joiner.add(source);
        }
        preferredSources.put(Constants.SOURCES, joiner.toString());
    }


    public LiveData<ArticlesResult> getArticlesStream() {
        return dataRepository.getArticlesStream();
    }



    public void getArticles(String source) {
        dataRepository.fetchArticles(preferredSources, source);
    }

    public void saveArticle(Article article) {
        dataRepository.saveArticle(article);
    }

    public void deleteArticle(Article article) {
        dataRepository.deleteArticle(article);
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }
}
