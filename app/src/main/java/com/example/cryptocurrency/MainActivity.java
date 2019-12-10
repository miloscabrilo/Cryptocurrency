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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //public static int serialNumber=1;

    //public static final String ARG_FROM_MAIN = "arg";
    public static final String ARG_NAME_FROM_MAIN = "name_coin";
    public static final String ARG_IMAGE_FROM_MAIN = "image_coin";
    public static final String ARG_SYMBOL_FROM_MAIN = "symbol_coin";

    private ListView listView;
    private CoinArrayAdapter coinArrayAdapter;
    private RequestQueue mQueue;
    private Drawable drawable;
    private CoinDetails coinDetails;
    private int numberDataPerPages;

    // Main Activity create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization parameters.
        mQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.listView);
        numberDataPerPages = 10;    // number of Cryptocurrencies returned
        coinArrayAdapter = new CoinArrayAdapter(getApplicationContext(), R.layout.list_of_coins);
        listView.setAdapter(coinArrayAdapter);
        listView.setFocusable(false);

        if(coinArrayAdapter.getCount()==0){
            try {
                jsonParse(coinArrayAdapter.getCount());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Use jsonParse method to get Cryptocurrencies.
        /*try {
            jsonParse(coinArrayAdapter.getCount());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        // OnClick Listener. Opens a new layout with general information about the Cryptocurrency clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Coin_details_tab.class);
                TextView nameView = (TextView) view.findViewById(R.id.coinName);
                TextView symbolView = (TextView) view.findViewById(R.id.coinSymbol);
                intent.putExtra(ARG_NAME_FROM_MAIN, nameView.getText());
                intent.putExtra(ARG_SYMBOL_FROM_MAIN, symbolView.getText());
                intent.putExtra(ARG_IMAGE_FROM_MAIN, coinArrayAdapter.getItem(position).getImageCoin());
                startActivity(intent);
            }
        });

        // Scroll Listener. Expands the list of loaded Cryptocurrencies.
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition() == totalItemCount -1 && listView.getCount() >= numberDataPerPages) {

                    try {
                        jsonParse(coinArrayAdapter.getCount());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

    }


    // This function parse JSON format.
    private void jsonParseCoinDetail(final int position) throws JSONException {
        int page = position / numberDataPerPages;
        String url = "https://min-api.cryptocompare.com/data/top/totalvolfull?tsym=USD";
        url = url + "&limit=" + Integer.toString(numberDataPerPages);
        url = url + "&page=" + Integer.toString(page);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("Data");
                    JSONObject objectData = data.getJSONObject(position % 10);
                    JSONObject raw = objectData.getJSONObject("RAW");
                    JSONObject rawUSD = raw.getJSONObject("USD");
                    JSONObject display = objectData.getJSONObject("DISPLAY");
                    JSONObject displayUSD = display.getJSONObject("USD");

                    String marketCap = rawUSD.getString("MKTCAP");              // 1. element
                    String totalVol = displayUSD.getString("TOTALVOLUME24H");   // 2. element
                    String directVol = displayUSD.getString("VOLUME24HOUR");
                    String rawVolume = rawUSD.getString("VOLUME24HOUR");
                    String rawTotalVolume = rawUSD.getString("TOTALVOLUME24H");
                    double ratio = Double.parseDouble(rawVolume) / Double.parseDouble(rawTotalVolume) * 100;
                    String ratioString = " (" + Double.toString(ratio) + "%)";

                    String directVol24 = directVol + ratioString; // 3. element

                    String directVolDolar = displayUSD.getString("VOLUME24HOURTO");     // 4. element
                    String open24 = displayUSD.getString("OPEN24HOUR");       // 5. element
                    String low24 = displayUSD.getString("LOW24HOUR");
                    String high24 = displayUSD.getString("HIGH24HOUR");
                    String lowHigh = low24 + " / " + high24;                        // 6. element


                    Coin coin = coinArrayAdapter.getItem(position);

                    View view1 = getLayoutInflater().inflate(R.layout.coin_details, null);
                    ImageView i = view1.findViewById(R.id.coinImage);
                    TextView t = view1.findViewById(R.id.textNameCoin);
                    t.setText(marketCap);

                    //coinDetails = new CoinDetails(coin, marketCap, totalVol, directVol24, directVolDolar, open24, lowHigh);

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


    private void jsonParse(int countCoins) throws JSONException {
        int urlPage;
        if(countCoins == 0) {
            urlPage=0;
        }
        else {
            //urlPage = countCoins / (numberDataPerPages +1) + 1;
            urlPage = countCoins / numberDataPerPages;
        }

        String url = "https://min-api.cryptocompare.com/data/top/totaltoptiervolfull?tsym=USD";
        url = url + "&limit=" + Integer.toString(numberDataPerPages);
        url = url + "&page=" + Integer.toString(urlPage);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("Data");
                    //JSONArray ja = data.getJSONArray("CoinInfo");
                    //JSONObject object = jsonArray.getJSONObject(0);
                    //JSONObject ja = jsonArray.getJSONObject("CoinInfo");
                    //JsonArray ja = jsonArray.getJSONArray("CoinInfo");
                    for(int i=0; i<data.length();i++) {
                        JSONObject objectData = data.getJSONObject(i);
                        JSONObject coinInfo = objectData.getJSONObject("CoinInfo");
                        //String coinName = coinInfo.getString("FullName");
                        String symbol = coinInfo.getString("Name");
                        String coinName = coinInfo.getString("FullName");
                        String imageUrl = "https://www.cryptocompare.com"+ coinInfo.getString("ImageUrl");
                        //coinName = Integer.toString(serialNumber) + coinName;
                        //serialNumber++;
                        Coin coin = new Coin(imageUrl, coinName, symbol);
                        coinArrayAdapter.add(coin);

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


    /*
    Drawable drawableURL(String url, String sourceName)
            throws java.net.MalformedURLException, java.io.IOException {
        return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), sourceName);

    }*/

    /*private void handleUp(View view) {
        FloatingActionButton FAB = (FloatingActionButton) view.findViewById(R.id.fabToTop);
        FAB.setOnClickListener( {
            if (listView.getCount() != 0) {
                listView.smoothScrollToPosition(0);
            }
        });
    }*/


}

