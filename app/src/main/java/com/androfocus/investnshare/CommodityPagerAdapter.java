package com.androfocus.investnshare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class CommodityPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public CommodityPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                Commodity Commodity = new Commodity();
                return Commodity;
            case 1:
                Equity equity = new Equity();

                return  equity;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
