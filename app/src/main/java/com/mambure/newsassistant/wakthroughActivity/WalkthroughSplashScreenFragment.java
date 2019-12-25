package com.mambure.newsassistant.wakthroughActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.mambure.newsassistant.R;

public class WalkthroughSplashScreenFragment extends Fragment {
    private static final String TAG = WalkthroughSplashScreenFragment.class.getSimpleName();

    public WalkthroughSplashScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Inflating fragment view");
        return inflater.inflate(R.layout.splash_fragment_walkthrough, container, false);

    }

}
