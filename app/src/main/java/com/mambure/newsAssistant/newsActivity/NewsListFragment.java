package com.mambure.newsAssistant.newsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mambure.newsAssistant.BaseListFragment;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListFragment extends BaseListFragment implements ArticlesAdapter.OnItemClickListener,
    PopupMenu.OnMenuItemClickListener{
    private NewsActivityViewModel mViewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    private ArticlesAdapter adapter;
    @BindView(R.id.rv_articles_list) public RecyclerView recyclerView;
    private LiveData<ArticlesResult> articlesStream;
    private LiveData<Boolean> saveArticleStatusLiveData;
    private Article currentArticleToProcess;

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
            if(articlesResult == null) return;
            if (articlesResult.status.equals(Constants.RESULT_OK)) {
                if(!articlesResult.articles.isEmpty()){
                    adapter.addItems(articlesResult.articles);
                    hideErrorMessage();
                    hideProgessBar();
                }else {
                    hideProgessBar();
                    showStatusMessage("No articles to show at the moment.");
                }
            } else {
                showStatusMessage(getResources().getString(R.string.requestErrorMessage));
                hideProgessBar();
            }
        });
        mViewModel.loadData();
    }

    @Override
    public void onStop() {
        mViewModel.cleanUp();
        if(articlesStream != null) articlesStream.removeObservers(this);
        if(saveArticleStatusLiveData != null) saveArticleStatusLiveData.removeObservers(this);
        adapter.clearData();
        super.onStop();
    }

    @Override
    public void onItemClick(View view, Article article) {
        mViewModel.setCurrentArticleToSave(article);

        switch (view.getId()){
            case R.id.article_list_item:
                // TODO: open article
                break;
            case R.id.article_menu:
                openPopupMenu(view);
                break;
        }
    }

    private void openPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.article_menu_popup);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();

    }

    @Override
    public void onRefresh() {
        hideErrorMessage();
        mViewModel.getArticles();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
               saveArticleStatusLiveData = mViewModel.saveArticle();
               saveArticleStatusLiveData.observe(this, isSuccessful -> {
                   if(isSuccessful){
                       Toast.makeText(getContext(), "Article saved!", Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(getContext(), "Error saving article!", Toast.LENGTH_SHORT).show();
                   }
               });
        }
        return false;
    }
}
