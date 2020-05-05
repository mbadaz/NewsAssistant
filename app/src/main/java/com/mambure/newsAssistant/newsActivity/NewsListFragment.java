package com.mambure.newsAssistant.newsActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mambure.newsAssistant.BaseListFragment;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListFragment extends BaseListFragment implements ArticlesAdapter.OnItemClickListener{
    private NewsActivityViewModel mViewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    private ArticlesAdapter adapter;
    @BindView(R.id.rv_articles_list) public RecyclerView recyclerView;
    private LiveData<ArticlesResult> articlesStream;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NewsActivity) requireActivity()).component.inject(this);
        mViewModel = new ViewModelProvider(requireActivity(), viewModelsFactory).
                get(NewsActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressBar();
        hideErrorMessage();
        mViewModel.setDataSource(source);
        articlesStream = mViewModel.getArticlesStream();
        articlesStream.observe(this, articlesResult -> {
            if (articlesResult.status.equals(Constants.RESULT_OK)) {
                adapter.addItems(articlesResult.articles);
                if(!articlesResult.articles.isEmpty()){
                    hideErrorMessage();
                }else {
                    showStatusMessage("No articles to show at the moment.");
                }
            } else {
                showStatusMessage(getResources().getString(R.string.requestErrorMessage));
            }
            hideProgessBar();
        });
        mViewModel.loadData();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onStop() {
        mViewModel.cleanUp();
        if(articlesStream != null) articlesStream.removeObservers(this);
        adapter.clearData();
        super.onStop();
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        hideErrorMessage();
        mViewModel.getArticles();
    }
}
