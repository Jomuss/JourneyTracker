package com.joemoss.firebasetest.profileviews;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Comments", "Journeys"};
    private static int NUM_ITEMS = 2;

    public ProfilePagerAdapter(FragmentManager fm, Context context){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new UserCommentsFragment();
            case 1:
                return new UserPostsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
