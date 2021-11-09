package com.liftPlzz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.liftPlzz.fragments.CompletedFragment;
import com.liftPlzz.fragments.UpComingFragment;


public class SectionPagerMyRidesAdapter extends FragmentStatePagerAdapter {

    // integer to count number of tabs
    int tabCount;

    // Constructor to the class
    public SectionPagerMyRidesAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        // Initializing tab count
        this.tabCount = tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    // Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                UpComingFragment upComingFragment = new UpComingFragment();
                return upComingFragment;
            case 1:
                CompletedFragment upComingFragment1 = new CompletedFragment();
                return upComingFragment1;
            default:
                return null;
        }
    }

    // Override method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
