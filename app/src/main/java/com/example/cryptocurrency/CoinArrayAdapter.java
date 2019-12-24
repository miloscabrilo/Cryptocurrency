package com.example.cryptocurrency;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class CoinArrayAdapter extends ArrayAdapter<Coin> {
    private static final String TAG= "CoinArrayAdapter";
    private List<Coin> coinList= new ArrayList<>();
    private Context mContext;
    private boolean internetAccess;

    static class CoinViewHolder {
        ImageView imageCoin;
        TextView nameCoin;
        TextView symbolCoin;
    }

    public CoinArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
    }

    public void setInternetAccess(Boolean isInternetAccess) {
        internetAccess = isInternetAccess;
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


        if(internetAccess)
            Picasso.get().load(imageUrl).resize(120,120).into(viewHolder.imageCoin);
        else {
            Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).resize(120,120).into(viewHolder.imageCoin);
        }
        /*Picasso.get()
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(viewHolder.imageCoin);*/
        //Picasso.get().load("/data/data/com.example.cryptocurrency/databases/image.jpg").resize(120, 120);

        /*try {
            URL url = new URL(imageUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }*/

        viewHolder.nameCoin.setText(coin.getNameCoin());
        viewHolder.symbolCoin.setText(coin.getSymbolCoin());
        return row;
    }
    public void setListWithoutInternet(Cursor res) {
        View row;
        CoinViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.list_of_coins, null);
        while(res.moveToNext()) {
            viewHolder = new CoinViewHolder();
            viewHolder.imageCoin = (ImageView) row.findViewById(R.id.coinImage);
            viewHolder.nameCoin = (TextView) row.findViewById(R.id.coinName);
            viewHolder.symbolCoin = (TextView) row.findViewById(R.id.coinSymbol);

            viewHolder.nameCoin.setText(res.getString(1));
            viewHolder.symbolCoin.setText(res.getString(2));
            viewHolder.imageCoin.setImageBitmap(getImage(res));
            row.setTag(viewHolder);
        }

    }

    public Bitmap getImage(Cursor cur){

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(3);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null;
    }

    /*private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }*/
}
