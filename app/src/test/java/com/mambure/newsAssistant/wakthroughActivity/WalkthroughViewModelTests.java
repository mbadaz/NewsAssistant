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

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
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

    @Mock
    Repository repository;
    @InjectMocks
    WalkthroughActivityViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeViewModel() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanUp() {
        viewModel = null;
//        sourcesListArgumentCaptor = null;
    }

    @Test
    public void changePreferredSources() {
        // GIVEN
        List<Source> sourcesFromLocal = TestMockingUtils.generateMockSources().subList(0,4);
        SourcesResult sourcesResultFromRemote = TestMockingUtils.generateMockSourceResult();
        ArgumentCaptor<List<Source>> sourcesToSaveArgCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Source>> sourcesToDeleteArgCaptor = ArgumentCaptor.forClass(List.class);
        Maybe<List<Source>> maybe = Maybe.just(sourcesFromLocal);

        // WHEN
        Mockito.when(repository.getSavedSources()).
                thenReturn(maybe);
        Mockito.when(repository.getSources())
                .thenReturn(Observable.just(sourcesResultFromRemote));
        Mockito.when(repository.deletePreferredSources(anyList()))
                .thenReturn(Mockito.mock(Completable.class));
        Mockito.when(repository.saveSources(anyList()))
                .thenReturn(Mockito.mock(Completable.class));

        viewModel.loadSources();
        maybe.test().awaitTerminalEvent();
        viewModel.processClickedSourceItem(sourcesResultFromRemote.sources.get(0));
        viewModel.processClickedSourceItem(sourcesResultFromRemote.sources.get(1));
        viewModel.processClickedSourceItem(sourcesResultFromRemote.sources.get(4));
        viewModel.savePreferredSources();

        // VERIFY Sources to be deleted and Sources to be Saved
        Mockito.verify(repository).deletePreferredSources(sourcesToDeleteArgCaptor.capture());
        Mockito.verify(repository).saveSources(sourcesToSaveArgCaptor.capture());
        List<Source> sourcesToDelete = sourcesToDeleteArgCaptor.getValue();
        List<Source> sourcesToSave = sourcesToSaveArgCaptor.getValue();

        Assertions.assertThat(sourcesToDelete)
                .contains(sourcesResultFromRemote.sources.get(0),
                        sourcesResultFromRemote.sources.get(1));

        Assertions.assertThat(sourcesToSave)
                .contains(sourcesResultFromRemote.sources.get(4));
    }

    @Test
    public void sourcesFetchAndInitializeTest() {
        // GIVEN
        List<Source> sources = TestMockingUtils.generateMockSources();
        Mockito.when(repository.getSavedSources()).thenReturn(Maybe.just(sources.subList(0,3)));
        Mockito.when(repository.getSources()).thenReturn(Observable.just(TestMockingUtils.generateMockSourceResult()));

        // PEFORM
        viewModel.loadSources();
        LiveData<SourcesResult> sourcesResultLiveData = viewModel.getSourcesStream();

        // VERIFY
        SourcesResult result = LiveDataTestingUtils.getValue(sourcesResultLiveData);
        int checkedCount = (int) result.sources.stream().filter(Source::isChecked).count();
        Assert.assertEquals(3,checkedCount);
    }

}
