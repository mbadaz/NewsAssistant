package com.peruzal.newsassistant.wakthroughActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.newsassistant.MyApplication;
import com.peruzal.newsassistant.R;
import com.peruzal.newsassistant.data.models.Source;
import com.peruzal.newsassistant.dependencyInjection.ViewModelsFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkthroughSourcesFragment extends Fragment implements SourcesAdapter.OnItemClickListener {
    private static final String TAG = WalkthroughSourcesFragment.class.getSimpleName();
    private WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    @BindView(R.id.rv_walkthrough)
    public RecyclerView recyclerView;
    private SourcesAdapter sourcesAdapter;

    public WalkthroughSourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourcesAdapter = new SourcesAdapter(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) getActivity().getApplication()).
                getComponent().inject(this);
        viewModel = viewModelsFactory.create(WalkthroughActivityViewModel.class);
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
            }
        });
        viewModel.fetchSources();
    }

    @Override
    public void onItemClick(Source source) {
        if (source.isChecked()) {
            viewModel.removePreferredSource(source);
        }
        viewModel.addPreferedSource(source);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.getSourcesStream().removeObservers(this);
    }
}
