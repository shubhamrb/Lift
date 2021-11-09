package com.liftPlzz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.liftPlzz.fragments.BikeFragment;
import com.liftPlzz.fragments.CarFragment;
import com.liftPlzz.model.getVehicle.Datum;


public class SectionPagerAddVehicleAdapter extends FragmentStatePagerAdapter {

    // integer to count number of tabs
    int tabCount;
    Datum datum;

    // Constructor to the class
    public SectionPagerAddVehicleAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        // Initializing tab count
        this.tabCount = tabCount;
    }

    public SectionPagerAddVehicleAdapter(FragmentManager fm, int tabCount, Datum datum) {
        super(fm);
        // Initializing tab count
        this.datum = datum;
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
                CarFragment carFragment = new CarFragment();
                return carFragment;
            case 1:
                BikeFragment bikeFragment = new BikeFragment();
                return bikeFragment;
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
