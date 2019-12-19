package com.example.cryptocurrency;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoinDetailsTab extends AppCompatActivity {
    private TextView coinName, coinSymbol;
    private ImageView coinImage;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<String> listSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_details_tab);

        // Obtained data from MAIN ACTIVITY
        String passedArgName = getIntent().getExtras().getString("name_coin");
        String passedArgSymbol = getIntent().getExtras().getString("symbol_coin");
        String passedArgImage = getIntent().getExtras().getString("image_coin");
        listSymbol = getIntent().getStringArrayListExtra("list_symbol");

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        coinName = (TextView) findViewById(R.id.textNameCoin);
        coinSymbol = (TextView) findViewById(R.id.textSymbolCoin);
        coinImage = (ImageView) findViewById(R.id.sectionImage);

        coinName.setText(passedArgName);
        coinSymbol.setText(passedArgSymbol);
        Picasso.get().load(passedArgImage).resize(120, 120).into(coinImage);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentGeneralInfo(passedArgSymbol), "General info");
        adapter.AddFragment(new FragmentGraph(passedArgSymbol, listSymbol), "Graph view");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

}