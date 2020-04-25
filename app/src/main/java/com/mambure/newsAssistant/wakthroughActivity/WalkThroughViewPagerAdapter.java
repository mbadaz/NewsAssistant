package com.mambure.newsAssistant.wakthroughActivity;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WalkThroughViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;

    public WalkThroughViewPagerAdapter(@NonNull FragmentManager fm, Fragment[] fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
