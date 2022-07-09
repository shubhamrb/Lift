package com.liftPlzz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.liftPlzz.fragments.CompletedFragment;
import com.liftPlzz.fragments.PurchasePointFragment;
import com.liftPlzz.fragments.RedeemPointFragment;
import com.liftPlzz.fragments.UpComingFragment;


public class SectionPagerPointWalletAdapter extends FragmentStatePagerAdapter {

    // integer to count number of tabs
    int tabCount;

    // Constructor to the class
    public SectionPagerPointWalletAdapter(FragmentManager fm, int tabCount) {
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
                PurchasePointFragment purchasePointFragment = new PurchasePointFragment();
                return purchasePointFragment;
            case 1:
                RedeemPointFragment redeemPointFragment = new RedeemPointFragment();
                return redeemPointFragment;
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
