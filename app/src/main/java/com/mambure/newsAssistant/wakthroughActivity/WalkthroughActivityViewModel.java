package com.mambure.newsAssistant.wakthroughActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.MyIdlingResource;
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
    public static final String TAG = WalkThroughActivity.class.getSimpleName();
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
        MyIdlingResource.getInstance().increment();
       compositeDisposable.add(
               dataManager.fetchSourcesFromRemote().
                       subscribeOn(Schedulers.io()).
                       subscribe(sourcesResult -> {
                           sourcesStream.postValue(sourcesResult);
                           Log.d(TAG, "Result fetching sources from remote: " + sourcesResult);
                           MyIdlingResource.getInstance().countDown();
                       }, throwable -> {
                           SourcesResult errorResult = new SourcesResult();
                           errorResult.status = Constants.RESULT_ERROR;
                           sourcesStream.postValue(errorResult);
                           Log.e(TAG, "Error fetching sources from remote", throwable);
                           MyIdlingResource.getInstance().countDown();
                       })
       );
    }

    LiveData<String> savePreferredSources() {

        if (preferredSources.isEmpty()) {
            return null;
        }

        MyIdlingResource.getInstance().increment();
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
                        Log.d(TAG, "Preferred sources data saved: " + preferredSources);
                        MyIdlingResource.getInstance().countDown();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        liveData.postValue(Constants.RESULT_ERROR);
                        Log.e(TAG, "Error saving preferred sources: ", e);
                        MyIdlingResource.getInstance().countDown();
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

    void processClickedSourceItem(Source source) {
        if(preferredSources.contains(source)) preferredSources.remove(source);
        else preferredSources.add(source);
        source.setChecked(!source.isChecked());
    }
}
