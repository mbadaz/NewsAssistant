package com.mbadasoft.newsassistant.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mbadasoft.newsassistant.fragments.FragmentWalkthrough1;

public class WalkThroughViewPagerAdapter extends FragmentStatePagerAdapter {

    public WalkThroughViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentWalkthrough1.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
