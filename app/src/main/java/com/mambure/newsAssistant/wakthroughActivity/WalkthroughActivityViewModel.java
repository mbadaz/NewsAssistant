package com.mambure.newsAssistant.wakthroughActivity;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.mambure.newsAssistant.Constants.SharedPrefsKeys.IS_FIRST_RUN;

public class WalkthroughActivityViewModel extends ViewModel {
    private DataRepository dataRepository;
    private SharedPreferences sharedPreferences;
    private List<Source> preferredSources = new ArrayList<>();

    @Inject
    public WalkthroughActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public WalkthroughActivityViewModel() {}

    LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }

    void fetchSources() {
        dataRepository.fetchSources(Constants.REMOTE);
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

    boolean isFirstRun() {
        return sharedPreferences.getBoolean(IS_FIRST_RUN, true);
    }

    void setIsFirstRun(boolean status) {
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN, status).apply();
    }

}
