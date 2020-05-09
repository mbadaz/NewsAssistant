package com.mambure.newsAssistant.wakthroughActivity;

import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.LiveDataTestingUtils;
import com.mambure.newsAssistant.TestMockingUtils;
import com.mambure.newsAssistant.data.Repository;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;

import static org.mockito.ArgumentMatchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class WalkthroughViewModelTests {

    WalkthroughActivityViewModel viewModel;
    @Mock
    Repository repository;
    @Mock
    SharedPreferences sharedPreferences;
    @Captor
    ArgumentCaptor<List<Source>> sourcesArgumentCaptor;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeViewModel() {
        MockitoAnnotations.initMocks(this);
        viewModel = new WalkthroughActivityViewModel(repository, sharedPreferences);
    }

    @Test
    public void sourcesFetchAndInitializeTest() {
        // GIVEN
        List<Source> sources = TestMockingUtils.generateMockSources();
        Mockito.when(repository.getSavedSources()).thenReturn(new Maybe<List<Source>>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super List<Source>> s) {
                s.onSuccess(sources.subList(1, 4));
            }
        });

        Mockito.when(repository.getSources()).thenReturn(new Observable<SourcesResult>() {
            @Override
            protected void subscribeActual(Observer<? super SourcesResult> observer) {
                SourcesResult sourcesResult = new SourcesResult();
                sourcesResult.sources = sources;
                sourcesResult.status = Constants.RESULT_OK;
                observer.onNext(sourcesResult);
            }
        });

        // PEFORM
        viewModel.loadSources();
        LiveData<SourcesResult> sourcesResultLiveData = viewModel.getSourcesStream();

        // VERIFY
        SourcesResult result = LiveDataTestingUtils.getValue(sourcesResultLiveData);
        int checkedCount = (int) result.sources.stream().filter(Source::isChecked).count();
        Assert.assertEquals(3,checkedCount);
    }

    @Test
    public void changePreferredSources() {
        // GIVEN
        List<Source> sourcesFromLocal = TestMockingUtils.generateMockSources().subList(0,3);
        List<Source> sourcesFromRemote = TestMockingUtils.generateMockSources();

        Mockito.when(repository.getSavedSources()).thenReturn(new Maybe<List<Source>>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super List<Source>> s) {
                s.onSuccess(sourcesFromLocal);
            }
        });

        Mockito.when(repository.getSources()).thenReturn(new Observable<SourcesResult>() {
            @Override
            protected void subscribeActual(Observer<? super SourcesResult> observer) {
                SourcesResult sourcesResult = new SourcesResult();
                sourcesResult.sources = sourcesFromRemote;
                sourcesResult.status = Constants.RESULT_OK;
                observer.onNext(sourcesResult);
            }
        });

        Mockito.when(repository.deletePreferredSources(anyList())).thenReturn(new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver observer) {
                observer.onComplete();
            }
        });

        Mockito.when(repository.saveSources(anyList())).thenReturn(new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver observer) {
                observer.onComplete();
            }
        });

        // PERFORM Deselect sources
        viewModel.loadSources();
        viewModel.processClickedSourceItem(sourcesFromRemote.get(0));
        viewModel.processClickedSourceItem(sourcesFromRemote.get(1));
        viewModel.savePreferredSources();

        // VERIFY Sources to be deleted from local database
        Mockito.verify(repository).deletePreferredSources(sourcesArgumentCaptor.capture());

        Assert.assertEquals(2, sourcesArgumentCaptor.getValue().size());

    }
}
