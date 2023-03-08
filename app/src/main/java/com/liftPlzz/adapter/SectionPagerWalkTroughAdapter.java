package com.liftPlzz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.liftPlzz.fragments.WalkTrough1Fragment;
import com.liftPlzz.fragments.WalkTrough2Fragment;
import com.liftPlzz.fragments.WalkTrough3Fragment;
import com.liftPlzz.fragments.WalkTrough4Fragment;


public class SectionPagerWalkTroughAdapter extends FragmentStatePagerAdapter {

    // integer to count number of tabs
    int tabCount;

    // Constructor to the class
    public SectionPagerWalkTroughAdapter(FragmentManager fm, int tabCount) {
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
                WalkTrough1Fragment tab1 = new WalkTrough1Fragment();
                return tab1;
            case 1:
                WalkTrough2Fragment tab2 = new WalkTrough2Fragment();
                return tab2;
            case 2:
                WalkTrough3Fragment tab3 = new WalkTrough3Fragment();
                return tab3;
            case 3:
                WalkTrough4Fragment tab4 = new WalkTrough4Fragment();
                return tab4;
            default:
                return null;
        }
    }

    // Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
