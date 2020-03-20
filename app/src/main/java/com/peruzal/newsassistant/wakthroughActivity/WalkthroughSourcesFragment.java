package com.peruzal.newsassistant.wakthroughActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.newsassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkthroughSourcesFragment extends Fragment {
    private static final String TAG = WalkthroughSourcesFragment.class.getSimpleName();
    WalkthroughActivityViewModel viewModel;

    @BindView(R.id.rv_walkthrough)
    RecyclerView recyclerView;

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
       View view = inflater.inflate(R.layout.fragment_walkthrough_sources, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).
                create(WalkthroughActivityViewModel.class);
    }
}
