package com.mbadasoft.newsassistant.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.WalkthroughActivityViewModel;
import com.mbadasoft.newsassistant.adapters.SourcesAdapter;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.models.SourcesResult;

public class WalkthroughFragment extends Fragment implements AdapterView.OnItemClickListener,
        Observer<SourcesResult>, SourcesAdapter.OnCheckBoxClickListener {
    private static final String TAG = WalkthroughFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private OnFragmentInteractionListener mListener;
    private WalkthroughActivityViewModel viewModel;
    private TableLayout tableLayout;
    TableRow tableRow;
    private RecyclerView recyclerView;
    private SourcesAdapter sourcesAdapter;
    private ListView categoriesList;
    private int counter = 0;

    public WalkthroughFragment() {
        // Required empty public constructor
    }


    public static WalkthroughFragment newInstance(int param1) {
        WalkthroughFragment fragment = new WalkthroughFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(getActivity()).get(WalkthroughActivityViewModel.class);
        switch (mParam1){
            case 2:
                return inflater.inflate(R.layout.fragment_walkthrough2, container, false);
            case 3:
                return inflater.inflate(R.layout.fragment_walkthrough3, container, false);
            case 1:
            default:
                return inflater.inflate(R.layout.fragment_walkthrough1, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (mParam1){
            case 1:
                break;
            case 2:
                tableLayout = view.findViewById(R.id.tableLayout);
                categoriesList = view.findViewById(R.id.list_walkthrough_category);
                categoriesList.setOnItemClickListener(this);
                break;
            case 3:
                recyclerView = view.findViewById(R.id.rv_walkthrough);
                LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(layoutManager);
                sourcesAdapter = new SourcesAdapter();
                sourcesAdapter.setCheckBoxClickListener(this);
                recyclerView.setAdapter(sourcesAdapter);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mParam1 == 3) {
            viewModel.getAvailableSources().observe(this,this );
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemTitle = parent.getItemAtPosition(position).toString();
        addItemToTable(view, itemTitle);
    }

    private void  addItemToTable(View view, String itemTitle) {
        if (counter == 3 || counter == 0) {
            tableRow = new TableRow(view.getContext());
            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.bottomMargin = 8;
            params.rightMargin = 8;
            params.leftMargin = 8;
            params.topMargin = 8;
            params.width = TableLayout.LayoutParams.MATCH_PARENT;
            tableRow.setLayoutParams(params);
            tableRow.setMeasureWithLargestChildEnabled(true);
            tableLayout.addView(tableRow);
            counter = 0;
        }

        TextView child = new TextView(view.getContext());
        child.setText(itemTitle);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMarginEnd(8);
        params.setMarginStart(8);
        params.gravity = Gravity.CENTER;
        params.width = TableRow.LayoutParams.MATCH_PARENT;
        child.setLayoutParams(params);
        child.setPadding(20, 12, 20, 12);
        child.setBackground(getResources().getDrawable(R.drawable.item_category_background));
        child.setTextColor(getResources().getColor(R.color.colorBackground_light));

        tableRow.addView(child);
        counter++;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        } else {
            viewModel.removeSourceFromSelection(source);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
