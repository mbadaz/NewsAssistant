package com.mambure.newsAssistant.wakthroughActivity;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataManager;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.mambure.newsAssistant.Constants.SharedPrefsKeys.IS_FIRST_RUN;

public class WalkthroughActivityViewModel extends ViewModel {
    private DataManager dataManager;
    private SharedPreferences sharedPreferences;
    private List<Source> preferredSources = new ArrayList<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public WalkthroughActivityViewModel(DataManager dataManager, SharedPreferences sharedPreferences) {
        this.dataManager = dataManager;
        this.sharedPreferences = sharedPreferences;
    }

    public WalkthroughActivityViewModel() {}

    LiveData<SourcesResult> getSourcesStream() {
        return sourcesStream;
    }

    void fetchSources() {
       compositeDisposable.add(
               dataManager.fetchSourcesFromRemote().
                       subscribeOn(Schedulers.io()).
                       subscribe(sourcesResult -> {
                           sourcesStream.postValue(sourcesResult);
                       })
       );
    }

    void addPreferedSource(Source source) {
        preferredSources.add(source);
    }

    void removePreferredSource(Source source) {
        preferredSources.remove(source);
    }

    LiveData<String> savePreferredSources() {
        if (preferredSources.isEmpty()) {
            return null;
        }

        MutableLiveData<String> liveData = new MutableLiveData<>();
        dataManager.saveSources(preferredSources).
                subscribeOn(Schedulers.io()).
                subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        liveData.postValue(Constants.RESULT_OK);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        liveData.postValue(Constants.RESULT_ERROR);
                    }
                });
        return liveData;
    }

    boolean isFirstRun() {
        return sharedPreferences.getBoolean(IS_FIRST_RUN, true);
    }

    void setIsFirstRun(boolean status) {
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN, status).apply();
    }


}
