package com.example.cryptocurrency;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

public class CoinArrayAdapter extends ArrayAdapter<Coin> {
    private static final String TAG= "CoinArrayAdapter";
    private List<Coin> coinList= new ArrayList<>();
    private Context mContext;

    static class CoinViewHolder {
        ImageView imageCoin;
        TextView nameCoin;
        TextView symbolCoin;
    }

    public CoinArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
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
        Picasso.get().load(imageUrl).resize(120,120).into(viewHolder.imageCoin);

        viewHolder.nameCoin.setText(coin.getNameCoin());
        viewHolder.symbolCoin.setText(coin.getSymbolCoin());
        return row;
    }
}
