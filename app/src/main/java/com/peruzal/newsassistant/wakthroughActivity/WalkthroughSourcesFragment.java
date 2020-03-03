package com.peruzal.newsassistant.wakthroughActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.newsassistant.R;

public class WalkthroughSourcesFragment extends Fragment implements SourcesAdapter.OnListItemClickListener {
    private static final String TAG = WalkthroughSourcesFragment.class.getSimpleName();

    private WalkthroughActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private SourcesAdapter sourcesAdapter;

    public WalkthroughSourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walkthrough_sources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider.
                AndroidViewModelFactory(getActivity().getApplication()).create(WalkthroughActivityViewModel.class);

        recyclerView = view.findViewById(R.id.rv_walkthrough);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sourcesAdapter = new SourcesAdapter(this);
        recyclerView.setAdapter(sourcesAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getSourcesStream().observe(this, sourcesResult -> {
            if (sourcesResult != null && sourcesResult.status.equals("ok")) {
                sourcesAdapter.addData(sourcesResult.sources);
            }
        });

        viewModel.fetchSources();
    }

    @Override
    public void onItemClick(String sourceId) {
        Toast.makeText(getContext(), sourceId, Toast.LENGTH_SHORT).show();
    }
}
