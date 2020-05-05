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
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.mambure.newsAssistant.Constants.SharedPrefsKeys.IS_FIRST_RUN;

public class WalkthroughActivityViewModel extends ViewModel {
    private static final String TAG = WalkThroughActivity.class.getSimpleName();
    private DataManager dataManager;
    private SharedPreferences sharedPreferences;
    private List<Source> preferredSources = new ArrayList<>();
    private List<Source> sourcesToFromLocal = new ArrayList<>();
    private int numberOfRunningTasks;
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<String> savingStatusLiveData = new MutableLiveData<>();;


    @Inject
    public WalkthroughActivityViewModel(DataManager dataManager, SharedPreferences sharedPreferences) {
        this.dataManager = dataManager;
        this.sharedPreferences = sharedPreferences;
    }

    public WalkthroughActivityViewModel() {}

    LiveData<SourcesResult> getSourcesStream() {
        return sourcesStream;
    }

    void loadSources() {
        loadSavedSourcesFromLocal();
        MyIdlingResource.getInstance().increment();
    }

    private void fetchSourcesFromRemote() {
        MyIdlingResource.getInstance().increment();
        Disposable disposable = dataManager.fetchSourcesFromRemote().
                        subscribeOn(Schedulers.io()).
                        subscribe(sourcesResult -> {
                            processSources(sourcesResult.sources);
                            sourcesStream.postValue(sourcesResult);
                            Log.d(TAG, "Result fetching sources from remote: " + sourcesResult);
                            MyIdlingResource.getInstance().countDown();
                        }, throwable -> {
                            SourcesResult errorResult = new SourcesResult();
                            errorResult.status = Constants.RESULT_ERROR;
                            sourcesStream.postValue(errorResult);
                            Log.e(TAG, "Error fetching sources from remote", throwable);
                            MyIdlingResource.getInstance().countDown();
                        });
        compositeDisposable.add(disposable);

    }

    private void processSources(List<Source> sources) {
        if(sourcesToFromLocal.isEmpty()) return;

        for (Source source : sources) {
            if (sourcesToFromLocal.contains(source)) {
                source.setChecked(true);
                preferredSources.add(source);
            }
        }
    }

    private void loadSavedSourcesFromLocal() {
        Disposable disposable = dataManager.fetchSourcesFromLocal().
                subscribeOn(Schedulers.io()).
                subscribe(sources -> {
                    sourcesToFromLocal.addAll(sources);
                    fetchSourcesFromRemote();
                }, throwable -> {
                    Log.e(TAG, "Error fetching sources from local: ", throwable);
                });
        compositeDisposable.add(disposable);
    }

    LiveData<String> savePreferredSources() {
        if (preferredSources.isEmpty()) return null;
        MyIdlingResource.getInstance().increment();

        List<Source> sourcesToSave = preferredSources.stream().
                filter(source -> !sourcesToFromLocal.contains(source)).collect(Collectors.toList());

        Disposable disposable = dataManager.saveSources(sourcesToSave).
                subscribeOn(Schedulers.io()).
                subscribe(() -> {
                    if(numberOfRunningTasks != 0) numberOfRunningTasks--;
                    if (numberOfRunningTasks == 0) savingStatusLiveData.postValue(Constants.RESULT_OK);
                    Log.d(TAG, "Preferred sources data saved: " + sourcesToSave);
                    MyIdlingResource.getInstance().countDown();
        }, throwable -> {
                savingStatusLiveData.postValue(Constants.RESULT_ERROR);
                Log.e(TAG, "Error saving preferred sources: ", throwable);
                MyIdlingResource.getInstance().countDown();
        });

        numberOfRunningTasks++;
        deleteSources();
        compositeDisposable.add(disposable);
        return savingStatusLiveData;
    }

    void processClickedSourceItem(Source source) {
        if(preferredSources.contains(source)){
            preferredSources.remove(source);
            source.setChecked(false);
        }
        else {
            preferredSources.add(source);
            source.setChecked(true);
        }
    }

    private void deleteSources() {
        List<Source> sourcesToDelete = sourcesToFromLocal.stream().
                filter(s -> !preferredSources.contains(s)).collect(Collectors.toList());

        if (!sourcesToDelete.isEmpty()){
            Disposable disposable = dataManager.deleteSources(sourcesToDelete).
                    subscribeOn(Schedulers.io()).
                    subscribe(() -> {
                        if(numberOfRunningTasks != 0) numberOfRunningTasks--;
                        if (numberOfRunningTasks == 0) savingStatusLiveData.postValue(Constants.RESULT_OK);
                        Log.d(TAG, "Sources deleted: " + sourcesToDelete.size());
                    }, throwable -> {
                        Log.e(TAG, "Error deleting sources", throwable);
                    });
            compositeDisposable.add(disposable);
            numberOfRunningTasks++;
        }
    }

    boolean isFirstRun() {
        return sharedPreferences.getBoolean(IS_FIRST_RUN, true);
    }

    void setIsFirstRun(boolean status) {
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN, status).apply();
    }

    void cleanUp() {
        compositeDisposable.clear();
        dataManager.cleanUp();
    }

}
