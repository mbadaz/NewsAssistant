package com.mbadasoft.newsassistant;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mbadasoft.newsassistant.data.ApiInfo;
import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.models.ArticlesResult;
import com.mbadasoft.newsassistant.models.SourcesResult;
import java.util.HashMap;
import java.util.Map;
import static com.mbadasoft.newsassistant.MainActivity.*;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private AppNewsRepository newsRepository;
    private AppPreferencesRepository preferencesRepository;
    private Map<String, Object> userPreferences = new HashMap<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        newsRepository = AppNewsRepository.getInstance(application);
        preferencesRepository = AppPreferencesRepository.getInstance(application);
        loadUserPrefs();
    }

    private void loadUserPrefs() {
        userPreferences.put("Sources", preferencesRepository.getPreferredSources());
        userPreferences.put("Load_limit", preferencesRepository.getArticlesLoadingLimit());
    }

    public LiveData<SourcesResult> getSources() {
        return newsRepository.getSources();
    }

    public LiveData<ArticlesResult> getArticles(String args) {
        switch (args) {
            case MY_NEWS:
                Log.d(TAG, "Loading" + MY_NEWS);
                return newsRepository.getArticles(userPreferences, ApiInfo.EVERYTHING_API_ENDPOINT);
            case HEADLINES:
                Log.d(TAG, "Loading" + HEADLINES);
                return newsRepository.getArticles(userPreferences, ApiInfo.HEADLINES_API_ENDPOINT);
            default:
                Log.d(TAG, "Loading" + SAVED);
                return newsRepository.getArticles(null, ApiInfo.LOCAL);
        }
    }


}
