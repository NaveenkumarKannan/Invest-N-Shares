package com.androfocus.investnshare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class EquityPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public EquityPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:

                Equity equity = new Equity();
                return  equity;
            case 1:
                Commodity Commodity = new Commodity();
                return Commodity;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
