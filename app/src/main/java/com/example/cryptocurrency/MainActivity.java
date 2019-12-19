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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARG_NAME_FROM_MAIN = "name_coin";
    public static final String ARG_IMAGE_FROM_MAIN = "image_coin";
    public static final String ARG_SYMBOL_FROM_MAIN = "symbol_coin";
    public static final String ARG_LIST_SYMBOL = "list_symbol";
    private List<String> listSymbol;
    private ListView listView;
    private CoinArrayAdapter coinArrayAdapter;
    private RequestQueue mQueue;
    private int numberDataPerPages;


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


        // Use jsonParse method to get Cryptocurrencies.
        if (coinArrayAdapter.getCount() == 0) {
            try {
                jsonParse(coinArrayAdapter.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
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

}