package com.example.cryptocurrency;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private String symbolName;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, String symbol) {
        symbolName = symbol;
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        //Fragment fragment = null;
        //Bundle bundle = new Bundle();
        //bundle.putString("symbolName", symbolName);
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        /*switch (position) {
            case 0: fragment = new FragmentGeneralInfo();
                fragment.setArguments(bundle);
                //fragment = fragmentList.get(position);
                break;
            case 1: fragment = new FragmentGraph();
                fragment.setArguments(bundle);
                //fragment = fragmentList.get(position);
                break;
        }*/
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        /*
        switch (position){
            case 0:
                return "General Info";
            case 1:
                return  "Graph View";
        }*/
        return fragmentListTitle.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListTitle.size();
    }
}
