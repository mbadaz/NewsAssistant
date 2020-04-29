package com.mambure.newsAssistant.wakthroughActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkthroughSourcesFragment extends Fragment implements
        SourcesAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener{
    private static final String TAG = WalkthroughSourcesFragment.class.getSimpleName();
    private WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;

    @BindView(R.id.rv_walkthrough)
    public RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.txtMessage)
    public TextView txtMessage;

    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    private EditText searchEditText;

    private SourcesAdapter sourcesAdapter;
    private SearchView searchView;

    public WalkthroughSourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sourcesAdapter = new SourcesAdapter(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((WalkThroughActivity) requireActivity()).component.inject(this);
        viewModel = new ViewModelProvider(requireActivity(), viewModelsFactory).get(WalkthroughActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_walkthrough_sources, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(sourcesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getSourcesStream().observe(this, sourcesResult -> {
            if (sourcesResult.status.equals("ok")) {
                sourcesAdapter.addData(sourcesResult.sources);
                Log.d(TAG, "Added: " + sourcesResult.sources.size() + " sources");
            }else {
                txtMessage.setVisibility(View.VISIBLE);
                txtMessage.setText(getResources().getString(R.string.requestErrorMessage));
            }
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        });
        viewModel.fetchSources();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.getSourcesStream().removeObservers(this);
    }

    @Override
    public void onItemClick(Source source) {
        if (source.isChecked()) {
            source.setChecked(false);
            viewModel.removePreferredSource(source);
            return;
        }
        source.setChecked(true);
        viewModel.addPreferedSource(source);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        viewModel.fetchSources();
        txtMessage.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu_walkhrough_activity, menu);
        searchView = (SearchView) menu.findItem(R.id.app_bar_sources_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.sourceSearchViewHint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Search")) {
          searchView.setOnQueryTextListener(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        sourcesAdapter.getItemsFilter().filter(newText);
        return false;
    }
}
