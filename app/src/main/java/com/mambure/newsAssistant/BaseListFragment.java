package com.mambure.newsAssistant;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.TextView;

import com.mambure.newsAssistant.customTabs.CustomTabActivityHelper;
import com.mambure.newsAssistant.newsActivity.SavedArticleListFragment;
import com.mambure.newsAssistant.newsActivity.NewsActivity;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    protected static final String SOURCE = "Fragment Id";
    protected String fragmentId;

    @BindView(R.id.txtMessage)
    public TextView errorMessageTextView;

    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    protected CustomTabActivityHelper customTabActivityHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragmentId = bundle.getString(SOURCE);
        }
        FragmentActivity activity = requireActivity();
        if(activity instanceof NewsActivity) customTabActivityHelper = ((NewsActivity) activity).getCustomTabActivityHelper();
    }

    protected void hideProgessBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void showProgressBar() {
       swipeRefreshLayout.setRefreshing(true);
    }

    protected void showStatusMessage(String message) {
        hideProgessBar();
        errorMessageTextView.setVisibility(View.VISIBLE);
        errorMessageTextView.setText(message);
    }

    public void hideErrorMessage() {
        errorMessageTextView.setVisibility(View.GONE);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        errorMessageTextView.setVisibility(View.GONE);
    }
}
