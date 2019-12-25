package com.mambure.newsassistant.newsActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsassistant.data.DataController;
import com.mambure.newsassistant.models.Article;

import java.util.List;

import javax.inject.Inject;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private DataController dataController;

    @Inject
    public NewsActivityViewModel(@NonNull DataController dataController) {
        this.dataController = dataController;
    }


    public LiveData<List<Article>> getArticles(String fragmentId) {
        return dataController.getArticles(fragmentId);
    }


}
