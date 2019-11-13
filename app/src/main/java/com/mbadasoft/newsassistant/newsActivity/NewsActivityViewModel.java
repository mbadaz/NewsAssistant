package com.mbadasoft.newsassistant.newsActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.data.DataController;
import com.mbadasoft.newsassistant.models.ArticlesResult;

public class NewsActivityViewModel extends AndroidViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private DataController dataController;

    public NewsActivityViewModel(@NonNull Application application) {
        super(application);
        dataController = new DataController(new AppNewsRepository(application), new AppPreferencesRepository(application));
    }


    public LiveData<ArticlesResult> getArticles(String fragmentId) {
        return dataController.getArticles(fragmentId);
    }


}
