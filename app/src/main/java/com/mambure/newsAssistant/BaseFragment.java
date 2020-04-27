package com.mambure.newsAssistant;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mambure.newsAssistant.newsActivity.NewsFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String SOURCE = "Fragment Id";
    protected String source;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.txtMessage)
    public TextView txtMessage;

    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    public static BaseFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(SOURCE, arg);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            source = bundle.getString(SOURCE);
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgessBar() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void showStatusMessage(String message) {
        hideProgessBar();
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.setText(message);
    }

    public void hideStatusMessage() {
        txtMessage.setVisibility(View.GONE);
    }

}
