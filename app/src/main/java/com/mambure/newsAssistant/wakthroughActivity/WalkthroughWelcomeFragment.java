package com.mambure.newsAssistant.wakthroughActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.mambure.newsAssistant.R;

public class WalkthroughWelcomeFragment extends Fragment {
    private static final String TAG = WalkthroughWelcomeFragment.class.getSimpleName();

    public WalkthroughWelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Inflating fragment view");
        return inflater.inflate(R.layout.fragment_walkthrough_welcome, container, false);

    }

}
