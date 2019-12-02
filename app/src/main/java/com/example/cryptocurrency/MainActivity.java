package com.example.cryptocurrency;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CoinArrayAdapter coinArrayAdapter;
    private RequestQueue mQueue;
    private Drawable drawable;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        listView = (ListView) findViewById(R.id.listView);
        coinArrayAdapter = new CoinArrayAdapter(getApplicationContext(), R.layout.list_of_coins);
        listView.setAdapter(coinArrayAdapter);



        try {
            jsonParse();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void jsonParse() throws JSONException {
        String url = "https://min-api.cryptocompare.com/data/top/totaltoptiervolfull?limit=10&tsym=USD&page=0";
        //JSONObject jObj = new JSONObject(url);
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
                        String symbol = coinInfo.getString("Name");
                        String coinName = coinInfo.getString("FullName");
                        String imageUrl = "https://www.cryptocompare.com"+ coinInfo.getString("ImageUrl");

                        Coin coin = new Coin(imageUrl, coinName, symbol, height);
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
    Drawable drawableURL(String url, String sourceName)
            throws java.net.MalformedURLException, java.io.IOException {
        return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), sourceName);

    }

}

