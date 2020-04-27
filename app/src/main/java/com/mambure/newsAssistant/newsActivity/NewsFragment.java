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

import com.mambure.newsAssistant.BaseFragment;
import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends BaseFragment implements ArticlesAdapter.OnItemClickListener{
    private NewsActivityViewModel mViewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    private ArticlesAdapter adapter;
    @BindView(R.id.rv_articles_list) public RecyclerView recyclerView;
    private LiveData<ArticlesResult> articlesStream;
    private LiveData<Boolean> isBusyStatus;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
        articlesStream = mViewModel.getArticlesStream();
        articlesStream.observe(this, articlesResult -> {
            if (articlesResult.status.equals(Constants.RESULT_OK)) {
                if(articlesResult.articles != null && !articlesResult.articles.isEmpty()){
                    adapter.addItems(articlesResult.articles);
                    hideProgessBar();

                }else {
                }
            } else {
                showStatusMessage(getResources().getString(R.string.requestErrorMessage));
            }

        });
        mViewModel.getArticles(source);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.cleanUp();
        if(articlesStream != null) articlesStream.removeObservers(this);
        if (isBusyStatus != null)isBusyStatus.removeObservers(this);

    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mViewModel.getArticles(source);
    }
}
