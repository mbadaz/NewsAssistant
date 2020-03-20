package com.peruzal.newsassistant.wakthroughActivity;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.peruzal.newsassistant.data.DataRepository;
import com.peruzal.newsassistant.data.models.Source;
import com.peruzal.newsassistant.data.models.SourcesResult;

import java.util.ArrayList;
import java.util.List;

public class WalkthroughActivityViewModel extends ViewModel {
    private DataRepository dataRepository;
    private SharedPreferences sharedPreferences;
    private List<Source> preferredSources = new ArrayList<>();

    public WalkthroughActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public WalkthroughActivityViewModel() {}

    LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }

    void fetchSources() {
        dataRepository.fetchSources();
    }

    void addPreferedSource(Source source) {
        preferredSources.add(source);
    }

    void removePreferredSource(Source source) {
        preferredSources.remove(source);
    }

    void savePreferredSources() {
        dataRepository.saveSources(preferredSources);
    }

}
