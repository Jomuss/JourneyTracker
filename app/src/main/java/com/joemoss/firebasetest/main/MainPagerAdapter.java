package com.joemoss.firebasetest.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Subscribed", "Popular"};
    private Context context;
    private static int NUM_ITEMS = 2;

    public MainPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;

    }




    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show SubscribedFragment
                //.newInstance(0, "Page # 1");
                return SubscribedFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show Popular Fragment
                return new PopularFragment(context);
                //return PopularFragment.newInstance(1, "Page # 2");
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return NUM_ITEMS;
    }
}