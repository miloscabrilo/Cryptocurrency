package com.example.cryptocurrency;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class CoinArrayAdapter extends ArrayAdapter<Coin> {
    private static final String TAG= "CoinArrayAdapter";
    private List<Coin> coinList= new ArrayList<>();

    private RequestQueue mQueue;


    static class CoinViewHolder {
        ImageView imageCoin;
        TextView nameCoin;
        TextView symbolCoin;
    }


    public CoinArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

    }

    @Override
    public void add(Coin object) {
        coinList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.coinList.size();
    }

    @Override
    public Coin getItem(int index) {
        return this.coinList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CoinViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_of_coins, parent, false);
            viewHolder = new CoinViewHolder();
            viewHolder.imageCoin = (ImageView) row.findViewById(R.id.coinImage);
            viewHolder.nameCoin = (TextView) row.findViewById(R.id.coinName);
            viewHolder.symbolCoin = (TextView) row.findViewById(R.id.coinSymbol);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CoinViewHolder)row.getTag();
        }
        Coin coin = getItem(position);



        String imageUrl = coin.getImageCoin();
        //Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.ic_launcher_background).into(viewHolder.imageCoin);
        Picasso.get().load(imageUrl).resize(coin.getHeight()/14, coin.getHeight()/14).into(viewHolder.imageCoin);



        viewHolder.nameCoin.setText(coin.getNameCoin());
        viewHolder.symbolCoin.setText(coin.getSymbolCoin());
        return row;
    }



}
