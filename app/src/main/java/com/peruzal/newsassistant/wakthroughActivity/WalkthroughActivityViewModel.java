package com.peruzal.newsassistant.wakthroughActivity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.peruzal.newsassistant.Constants;
import com.peruzal.newsassistant.data.DataRepository;
import com.peruzal.newsassistant.data.models.Source;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.HashSet;
import java.util.Set;

public class WalkthroughActivityViewModel extends AndroidViewModel {
    DataRepository dataRepository;
    SharedPreferences sharedPreferences;
    Set<String> preferredSources = new HashSet<>();

    public WalkthroughActivityViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }

    public void fetchSources() {
        dataRepository.fetchSources();
    }

    public void addPreferedSource(Source source) {
        preferredSources.add(source.id);
    }

    public void removePreferredSource(Source source) {
        preferredSources.remove(source.id);
    }
}
