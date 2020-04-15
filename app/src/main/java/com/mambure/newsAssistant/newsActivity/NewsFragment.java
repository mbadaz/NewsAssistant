package com.mambure.newsAssistant.newsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mambure.newsAssistant.R;

public class NewsFragment extends Fragment {
    public static final String FRAGMENT_ID = "Fragment Id";
    private NewsActivityViewModel mViewModel;
    private ArticlesAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String args;


    public static NewsFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_ID, arg);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            args = bundle.getString(FRAGMENT_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressbar_loading);
        recyclerView = getView().findViewById(R.id.rv_articles_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticlesAdapter();

    }

   }
