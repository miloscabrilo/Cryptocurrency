/**
 * First - General information fragment.
 */

package com.example.cryptocurrency;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FragmentGeneralInfo extends Fragment {
    private View view;
    private ArrayList<String> listGenInfo, listCompareValue;
    private TextView captionGenInfo, captionComValue;
    private TextView textGenInfo, textCompValue;
    private String[] listToCompare;
    private RequestQueue mQueue;
    private String symbolName;
    private boolean internetAccess;
    private CryptocurrencyDatabase db;

    public FragmentGeneralInfo() {
    }
    public FragmentGeneralInfo(String symbol, boolean internetAccess) {
        symbolName = symbol;
        this.internetAccess = internetAccess;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_info, container, false);
        captionGenInfo = (TextView) view.findViewById(R.id.captionGenInfo);
        captionGenInfo.setText("Full list of information for: " + symbolName);
        captionComValue = (TextView) view.findViewById(R.id.captionCompValue);
        captionComValue.setText("Current value of: " + symbolName);
        captionGenInfo.setTypeface(null, Typeface.BOLD);
        captionComValue.setTypeface(null, Typeface.BOLD);
        textGenInfo = (TextView) view.findViewById(R.id.textGenInfo);
        textCompValue = (TextView) view.findViewById(R.id.textCompValue);
        mQueue = Volley.newRequestQueue(getContext());
        listToCompare = new String[] {
                "BTC", "ETH", "EVN", "DOGE", "ZEC", "USD", "EUR"
        };
        listGenInfo = new ArrayList<>();
        listCompareValue = new ArrayList<>();

        db = new CryptocurrencyDatabase(getContext());
        // Checking internet Access. If Internet is available, use data from URL, otherwise use data from DB if exist.
        if(internetAccess) {
            try {
                jsonParse();
                jsonParseComparedValues();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            // Reading from database.
            Cursor res = db.readSelectedCoin();
            while(res.moveToNext()) {
                if( res.getString(0).equals(symbolName)) {
                    textGenInfo.setText(res.getString(1));
                    textCompValue.setText(res.getString(2));
                    break;
                }
            }
        }

        return view;
    }

    // JSON deserialize method for compare selected Cryptocurrency with string array Cryptocurrencies.
    public void jsonParseComparedValues() throws JSONException {
        //https://min-api.cryptocompare.com/data/pricemultifull?fsyms=ETC&tsyms=USD,ETH,EVN,DOGE,ZEC,USD,EUR
        String url = "https://min-api.cryptocompare.com/data/price?fsym=" + symbolName + "&tsyms=" + listToCompare[0];
        for(int i = 1; i < listToCompare.length; i++)
            url = url + "," + listToCompare[i];

        // Json request for obtain parameters for graph view.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for(int i = 0; i < listToCompare.length; i++) {
                                listCompareValue.add(symbolName + " compared to " + listToCompare[i] + " = " + response.getString(listToCompare[i]));
                            }
                            // Adding text into TextView.
                            if(listCompareValue.size() > 0)
                                textCompValue.setText(listCompareValue.get(0));
                            for (int i = 1; i < listCompareValue.size(); i++) {
                                textCompValue.append("\n");
                                textCompValue.append(listCompareValue.get(i));
                            }
                            // Insert general information and comparison values for selected Cryptocurrency in database
                            db.insertGenInfo(symbolName, textGenInfo.getText().toString(), textCompValue.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mQueue.add(request);
    }

    // JSON deserialize method for showing full information of selected Cryptocurrency.
    public void jsonParse() throws JSONException {

        //https://min-api.cryptocompare.com/data/pricemultifull?fsyms=ETC&tsyms=USD
        String url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" + symbolName + "&tsyms=USD";

        // Json request for obtain parameters for selected Cryptocurrency.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject display = response.getJSONObject("DISPLAY");
                            JSONObject symbol = display.getJSONObject(symbolName);
                            JSONObject usd = symbol.getJSONObject("USD");
                            Iterator<?> keys = usd.keys();      // Data is unknown.
                            while( keys.hasNext() ) {
                                String name = (String) keys.next();
                                String value = usd.getString(name);
                                listGenInfo.add(name + ": " + value);
                            }
                            // Adding text into TextView.
                            if(listGenInfo.size() > 0)
                                textGenInfo.setText(listGenInfo.get(0));
                            for (int i = 1; i < listGenInfo.size(); i++ ) {
                                textGenInfo.append("\n");
                                textGenInfo.append(listGenInfo.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mQueue.add(request);
    }

}