package com.mambure.newsAssistant.wakthroughActivity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mambure.newsAssistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalkthroughFinishFragment extends Fragment {
    public WalkthroughFinishFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walkthrough_finish, container, false);
    }
}
