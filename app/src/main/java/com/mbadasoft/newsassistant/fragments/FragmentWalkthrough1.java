package com.mbadasoft.newsassistant.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mbadasoft.newsassistant.MainActivity;
import com.mbadasoft.newsassistant.R;

public class FragmentWalkthrough1 extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private OnFragmentInteractionListener mListener;
    private TableLayout tableLayout;
    private ListView catgoriesListView;
    private TextView textFinish;

    public FragmentWalkthrough1() {
        // Required empty public constructor
    }


    public static FragmentWalkthrough1 newInstance(int param1) {
        FragmentWalkthrough1 fragment = new FragmentWalkthrough1();
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
        // Inflate the layout for this fragment
        switch (mParam1){
            case 1:
                return inflater.inflate(R.layout.fragment_walkthrough1, container, false);
            case 2:
                return inflater.inflate(R.layout.fragment_walkthrough2, container, false);
            case 3:
                return inflater.inflate(R.layout.fragment_walkthrough3, container, false);
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
                catgoriesListView = view.findViewById(R.id.list_walkthrough_category);
                catgoriesListView.setOnItemClickListener(this);
                break;
            case 3:
                break;
        }

        textFinish = view.findViewById(R.id.txt_skip);
        textFinish.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemTitle = parent.getItemAtPosition(position).toString();
        TableRow tableRow = new TableRow(view.getContext());
        TextView child = new TextView(view.getContext());
        child.setText(itemTitle);
        child.setPadding(12, 12, 12, 12);
        TableLayout.LayoutParams parrams = new TableLayout.LayoutParams();
        TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        params.bottomMargin = 8;
        params.rightMargin = 8;
        params.leftMargin = 8;
        params.topMargin = 8;
        params.width = TableLayout.LayoutParams.WRAP_CONTENT;
        child.setLayoutParams(params);
        child.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        child.setTextColor(getResources().getColor(R.color.colorBackground_light));
        //tableRow.addView(child);
        tableRow.setLayoutParams(params);
        tableRow.setMeasureWithLargestChildEnabled(false);
        tableRow.setClipChildren(true);
        tableRow.setLayoutMode(TableLayout.LayoutParams.WRAP_CONTENT);
        tableLayout.addView(child, TableLayout.LayoutParams.WRAP_CONTENT);
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
