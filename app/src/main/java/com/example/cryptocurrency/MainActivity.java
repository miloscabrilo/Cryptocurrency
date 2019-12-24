/**
 * This is the Main class activity
 *
 * Project description: TBA
 *
 * @author  Milos Cabrilo
 * @version 1.0
 * @since   12/4/2019
 */
package com.example.cryptocurrency;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARG_NAME_FROM_MAIN = "name_coin";
    public static final String ARG_IMAGE_FROM_MAIN = "image_coin";
    public static final String ARG_SYMBOL_FROM_MAIN = "symbol_coin";
    public static final String ARG_LIST_SYMBOL = "list_symbol";
    public static final String ARG_INTERNET_ACCESS = "internet_access";
    private List<String> listSymbol;
    private ListView listView;
    private CoinArrayAdapter coinArrayAdapter;
    private RequestQueue mQueue;
    private int numberDataPerPages;
    private CryptocurrencyDatabase db;


    // Main Activity create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization parameters.
        mQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.listView);
        numberDataPerPages = 20;    // number of Cryptocurrencies returned
        coinArrayAdapter = new CoinArrayAdapter(getApplicationContext(), R.layout.list_of_coins);
        listView.setAdapter(coinArrayAdapter);
        listView.setFocusable(false);
        listSymbol = new ArrayList<>();
        listSymbol.add("Select");
        db = new CryptocurrencyDatabase(this);

        coinArrayAdapter.setInternetAccess(isNetworkConnected());



        if(!isNetworkConnected()) {
            Cursor res = db.readCoins();
            //coinArrayAdapter.setListWithoutInternet(res);
            if(res.getCount() == 0) {
                showMessage("Error", "Nothing found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {
                buffer.append("ID :" + res.getString(0) + "\n");
                buffer.append("NAME :" + res.getString(1) + "\n");
                buffer.append("SYMBOL :" + res.getString(2) + "\n");
                //buffer.append("IMAGE :" + res.getString(3) + "\n");
                Coin coin = new Coin( res.getString(2), res.getString(0), res.getString(1));
                listSymbol.add(res.getString(1));
                coinArrayAdapter.add(coin);
            }
            showMessage("DATA", buffer.toString());
        }
        else {
            db.deleteCoins();
            db.deleteSecetedCoin();
            db.deleteGraphs();
            // Use jsonParse method to get Cryptocurrencies.
            if (coinArrayAdapter.getCount() == 0) {
                try {
                    jsonParse(coinArrayAdapter.getCount());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        // OnClick Listener. Opens a new layout with general information about the Cryptocurrency clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CoinDetailsTab.class);
                TextView nameView = (TextView) view.findViewById(R.id.coinName);
                TextView symbolView = (TextView) view.findViewById(R.id.coinSymbol);
                intent.putExtra(ARG_NAME_FROM_MAIN, nameView.getText());
                intent.putExtra(ARG_SYMBOL_FROM_MAIN, symbolView.getText());
                intent.putExtra(ARG_IMAGE_FROM_MAIN, coinArrayAdapter.getItem(position).getImageCoin());
                intent.putExtra(ARG_INTERNET_ACCESS, isNetworkConnected());
                intent.putStringArrayListExtra(ARG_LIST_SYMBOL, (ArrayList<String>)listSymbol);
                startActivity(intent);
            }
        });

        // Scroll Listener. Expands the list of loaded Cryptocurrencies.
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int firstItem = -1;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            // Load new Cryptocurrencies only if user scrolled to bottom of list.
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount < totalItemCount && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                    if (firstVisibleItem != firstItem) {
                        firstItem = firstVisibleItem;
                        try {
                            jsonParse(coinArrayAdapter.getCount());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // JSON deserialize method
    private void jsonParse(int countCoins) throws JSONException {
        int urlPage;
        if(countCoins == 0) {
            urlPage=0;
        }
        else {
            urlPage = countCoins / numberDataPerPages;
        }
        // https://min-api.cryptocompare.com/data/top/totaltoptiervolfull?tsym=USD&limit=12&page=0
        String url = "https://min-api.cryptocompare.com/data/top/totaltoptiervolfull?tsym=USD";
        url = url + "&limit=" + Integer.toString(numberDataPerPages) + "&page=" + Integer.toString(urlPage);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("Data");     // find Array with name DATA
                    for(int i=0; i<data.length();i++) {
                        // Get important element from Array DATA
                        JSONObject objectData = data.getJSONObject(i);
                        JSONObject coinInfo = objectData.getJSONObject("CoinInfo");
                        String symbol = coinInfo.getString("Name");
                        String coinName = coinInfo.getString("FullName");
                        String imageUrl = "https://www.cryptocompare.com"+ coinInfo.getString("ImageUrl");
                        Coin coin = new Coin(imageUrl, coinName, symbol);
                        coinArrayAdapter.add(coin);
                        listSymbol.add(symbol);

                        //URL url = new URL(imageUrl);
                        //Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        db.insertCoins(coinName, symbol, imageUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


}