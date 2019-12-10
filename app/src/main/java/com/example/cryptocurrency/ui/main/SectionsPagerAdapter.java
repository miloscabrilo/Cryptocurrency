package com.example.cryptocurrency.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cryptocurrency.FragmentGeneralInfo;
import com.example.cryptocurrency.FragmentGraph;
import com.example.cryptocurrency.R;

import java.util.List;
import java.util.Vector;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //@StringRes
    //private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private String symbolName;


    //fragmentList.add(Fragment.instantiate(this, FragmentGeneralInfo.class.getName()));
    //fragmentList.add(Fragment.instantiate(this, FragmentGraph.class.getName()));

    public SectionsPagerAdapter(Context context, FragmentManager fm, String symbol) {
        super(fm);
        mContext = context;
        symbolName = symbol;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("symbolName", symbolName);
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        switch (position) {
            case 0: fragment = new FragmentGeneralInfo();
                    fragment.setArguments(bundle);
                    //fragment = fragmentList.get(position);
                    break;
            case 1: fragment = new FragmentGraph();
                    fragment.setArguments(bundle);
                    //fragment = fragmentList.get(position);
                    break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "General Info";
            case 1:
                return  "Graph View";
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}