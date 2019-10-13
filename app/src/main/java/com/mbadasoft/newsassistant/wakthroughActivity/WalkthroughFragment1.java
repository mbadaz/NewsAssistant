package com.mbadasoft.newsassistant.wakthroughActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.mbadasoft.newsassistant.R;

public class WalkthroughFragment1 extends Fragment {
    private static final String TAG = WalkthroughFragment1.class.getSimpleName();

    public WalkthroughFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Inflating fragment view");
        return inflater.inflate(R.layout.fragment_walkthrough1, container, false);

    }

}
