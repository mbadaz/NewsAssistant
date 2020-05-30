package com.mambure.newsAssistant.wakthroughActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.MyIdlingResource;
import com.mambure.newsAssistant.data.Repository;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.mambure.newsAssistant.Constants.SharedPrefsKeys.IS_FIRST_RUN;

public class WalkthroughActivityViewModel extends ViewModel {
    private static final String TAG = WalkthroughActivityViewModel.class.getSimpleName();
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private List<Source> preferredSources = new ArrayList<>();
    private List<Source> savedSources = new ArrayList<>();
    private int numberOfRunningTasks;
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<String> savingStatusLiveData = new MutableLiveData<>();;


    @Inject
    public WalkthroughActivityViewModel(Repository repository, SharedPreferences sharedPreferences) {
        this.repository = repository;
        this.sharedPreferences = sharedPreferences;
    }

    public WalkthroughActivityViewModel() {}

    LiveData<SourcesResult> getSourcesStream() {
        return sourcesStream;
    }

    void loadSources() {
        loadSavedSources();
        //MyIdlingResource.getInstance().increment();
    }

    private void fetchSourcesFromRemote() {
        MyIdlingResource.getInstance().increment();
        Disposable disposable = repository.getSources().
                        subscribeOn(Schedulers.io()).
                        subscribe(sourcesResult -> {
                            processSourceResult(sourcesResult);
                            processSources(sourcesResult.sources);
                            sourcesStream.postValue(sourcesResult);
                            Log.d(TAG, "Result fetching sources from remote: " + sourcesResult);
                            MyIdlingResource.getInstance().countDown();
                        }, throwable -> {
                            SourcesResult errorResult = new SourcesResult();
                            errorResult.result = Constants.Result.ERROR;
                            sourcesStream.postValue(errorResult);
                            Log.e(TAG, "Error fetching sources from remote", throwable);
                            MyIdlingResource.getInstance().countDown();
                        });
        compositeDisposable.add(disposable);

    }

    private void processSourceResult(SourcesResult sourcesResult) {
        if (sourcesResult.sources.size() > 0) {
            sourcesResult.result = Constants.Result.OK;
        } else {
            sourcesResult.result = Constants.Result.NO_DATA;
        }
    }

    private void processSources(List<Source> sources) {
        for (Source source : sources) {
            if (savedSources.contains(source)) source.setChecked(true);
        }
    }

    private void loadSavedSources() {
      repository.getSavedSources().
                subscribeOn(Schedulers.io()).
                subscribe(new MaybeObserver<List<Source>>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<Source> sources) {
                        savedSources = sources;
                        preferredSources.addAll(sources);
                        fetchSourcesFromRemote();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading sources from local: ", e);
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        fetchSourcesFromRemote();
                        Log.d(TAG, "No saved sources");
                        disposable.dispose();
                    }
                });
    }

    boolean hasSourcesToSave() {
        return !preferredSources.isEmpty();
    }

    LiveData<String> savePreferredSources() {
        if (preferredSources.isEmpty()) return null;
        MyIdlingResource.getInstance().increment();
        List<Source> sourcesToSave = preferredSources.stream().
                filter(source -> !savedSources.contains(source)).collect(Collectors.toList());

        Disposable disposable = repository.saveSources(sourcesToSave).
                subscribeOn(Schedulers.io()).
                subscribe(() -> {
                    if(numberOfRunningTasks != 0) numberOfRunningTasks--;
                    if (numberOfRunningTasks == 0) savingStatusLiveData.postValue(Constants.RESULT_OK);
                    Log.d(TAG, "Preferred sources data saved: " + sourcesToSave.toString());
                    MyIdlingResource.getInstance().countDown();
        }, throwable -> {
                savingStatusLiveData.postValue(Constants.RESULT_ERROR);
                Log.e(TAG, "Error saving preferred sources: ", throwable);
                MyIdlingResource.getInstance().countDown();
        });
        deleteSources();
        numberOfRunningTasks++;
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
        List<Source> sourcesToDelete = new ArrayList<>();
        for (Source source: savedSources) {
            if (!preferredSources.contains(source)) {
                sourcesToDelete.add(source);
            }
        }

        if (!sourcesToDelete.isEmpty()){
            Disposable disposable = repository.deletePreferredSources(sourcesToDelete).
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
        repository.cleanUp();
    }

}
