package com.mbadasoft.newsassistant.wakthroughActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mbadasoft.newsassistant.MyApplication;
import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.dependencyInjection.ViewModelsFactory;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;

import javax.inject.Inject;

public class WalkthroughSourcesSelectionFragment extends Fragment implements
        Observer<SourcesResult>, SourcesAdapter.OnCheckBoxClickListener, SearchView.OnQueryTextListener {
    private static final String TAG = WalkthroughSourcesSelectionFragment.class.getSimpleName();
    private Toolbar toolbar;

    private WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    private RecyclerView recyclerView;
    private SourcesAdapter sourcesAdapter;
    private SearchView searchView;

    public WalkthroughSourcesSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        ((MyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        viewModel = ViewModelProviders.of(getActivity(),  viewModelsFactory).get(WalkthroughActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walkthrough3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar_walkthrough);
        ((WalkThroughActivity)getActivity()).setSupportActionBar(toolbar);
        recyclerView = view.findViewById(R.id.rv_walkthrough);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        sourcesAdapter = new SourcesAdapter();
        sourcesAdapter.setCheckBoxClickListener(this);
        recyclerView.setAdapter(sourcesAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       inflater.inflate(R.menu.menu_walkthough, menu);
       searchView = (SearchView) menu.findItem(R.id.menu_walkthrough_search).getActionView();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
       searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getAvailableSources().observe(this,this );
    }


    @Override
    public void onChanged(SourcesResult sourcesResult) {
        if (sourcesResult.status.equals("ok")) {
            sourcesAdapter.addData(sourcesResult.sources);
            Log.d(TAG, "UPDATED UI WITH SOURCES DATA: \n" + sourcesResult.sources);
        }
    }

    @Override
    public void onCheckBoxClicked(CheckBox checkBox, Source source) {
        if (checkBox.isChecked()) {
            viewModel.addSourceToSelection(source);
            source.setChecked(true);
        } else {
            viewModel.removeSourceFromSelection(source);
            source.setChecked(false);
        }
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        sourcesAdapter.getFilter().filter(query);
        return true;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        sourcesAdapter.getFilter().filter(newText);
        return true;
    }
}
