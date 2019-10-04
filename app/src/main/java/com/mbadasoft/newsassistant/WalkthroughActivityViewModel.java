package com.mbadasoft.newsassistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;

import java.util.HashMap;

public class WalkthroughActivityViewModel extends AndroidViewModel {

    private AppNewsRepository newsRepository;
    private AppPreferencesRepository preferencesRepository;


    public WalkthroughActivityViewModel(@NonNull Application application) {
        super(application);
        newsRepository = AppNewsRepository.getInstance(application);
        preferencesRepository = AppPreferencesRepository.getInstance(application);
    }

    public LiveData<SourcesResult> getSources() {
        return newsRepository.getSources();
    }

    public boolean IsFirstTimeLogin() {
        return preferencesRepository.getAppPreferences() == null;
    }

    public void setIsFirstTimeLogin(boolean value) {
        preferencesRepository.saveisFirstTimeLogin(value);
    }
}
