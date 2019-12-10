package com.example.cryptocurrency;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cryptocurrency.ui.main.SectionsPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class Coin_details_tab extends AppCompatActivity {
    private TextView coinName, coinSymbol;
    private ImageView coinImage;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;
    private List<Fragment> fragmentList;
    private Spinner spinner;
    private Fragment genInfo;
    private Fragment graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_details_tab);
        genInfo = new FragmentGeneralInfo();
        graphView = new FragmentGraph();
        fragmentList = new Vector<Fragment>();
        //fragmentList.add(Fragment.instantiate(this, FragmentGeneralInfo.class.getName()));
        //fragmentList.add(Fragment.instantiate(this, FragmentGraph.class.getName()));
        fragmentList.add(genInfo);
        fragmentList.add(graphView);

        // Obtained data from MAIN ACTIVITY
        String passedArgName = getIntent().getExtras().getString("name_coin");
        String passedArgSymbol = getIntent().getExtras().getString("symbol_coin");
        String passedArgImage = getIntent().getExtras().getString("image_coin");

        coinName = (TextView) findViewById(R.id.textNameCoin);
        coinSymbol = (TextView) findViewById(R.id.textSymbolCoin);
        coinImage = (ImageView) findViewById(R.id.sectionImage);

        coinName.setText(passedArgName);
        coinSymbol.setText(passedArgSymbol);
        Picasso.get().load(passedArgImage).resize(120,120).into(coinImage);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), passedArgSymbol);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        /*spinner = (Spinner) findViewById(R.id.spinner);
        String[] arraySpinner = new String[]{
                "BTC", "ETH", "EVN", "DOGE", "ZEC", "USD", "EUR"
        };
        List<String> listSpinnerItems = new ArrayList<>();
        listSpinnerItems.add("USD");
        listSpinnerItems.add("BTC");

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listSpinnerItems);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);*/



        //BTC, ETH, EVN,DOGE, ZEC, USD,EUR

        //information = new ArrayAdapter();
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //tabs.addTab(tabs.newTab().setText("General Info55"));
        //tabs.addTab(tabs.newTab().setText("Graph View55"));







        spinner = (Spinner) findViewById(R.id.spinner);
        final String[] arraySpinner = new String[]{
                "BTC", "ETH", "EVN", "DOGE", "ZEC", "USD", "EUR"
        };


        //TextView tt = (TextView) findViewById(R.id.textLowHigh3);
        //tt.setText("pomozi Boze");

    }
}