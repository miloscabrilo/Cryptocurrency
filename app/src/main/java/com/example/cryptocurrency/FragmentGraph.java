package com.example.cryptocurrency;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FragmentGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private PointF A = new PointF(10, 20);
    private PointF B = new PointF(100, 500);
    private List<PointF> dotsForDay, dotsForHour, dotsForMinute;
    private int[] timeForDay, timeForHour, timeForMinute;
    private Paint paint;
    private Canvas canvas;
    private LineView lineView, lineView2, lineView3;
    TextView textView;

    private RequestQueue mQueue;
    // graphView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String symbolName;


    public FragmentGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGraph newInstance(String param1, String param2) {
        FragmentGraph fragment = new FragmentGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            symbolName = getArguments().getString("symbolName");
        }


        //textView.findViewById(R.id.textChart);
        //textView.setText("Ajde upali");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineView = (LineView) view.findViewById(R.id.lineView);
        lineView2 = (LineView) view.findViewById(R.id.lineView2);
        lineView3 = (LineView) view.findViewById(R.id.lineView3);
        //paint = new Paint();
        //paint.setColor(Color.RED);
        //paint.setStrokeWidth(10);
        //canvas.drawLine(0, 0, 1, 1, paint);
        //pixelGrid.setNumColumns(4);
        //pixelGrid.setNumRows(6);
        //int width = 300;
        //int height = 200;
        //lineView.setDimensions(width, height);
        lineView.setNumColumns(7);
        lineView.setNumRows(6);
        /*lineView2.setNumColumns(2);
        lineView2.setNumRows(3);
        lineView3.setNumColumns(10);
        lineView3.setNumRows(10);-*/

        //lineView.setSymbolName(symbolName);

        //lineView.setA(A);
        //lineView.setB(B);
        lineView.draw();
        lineView2.setNumColumns(6);
        lineView2.setNumRows(6);
        lineView2.draw();

        lineView3.draw();
        lineView3.setNumColumns(10);
        lineView3.setNumRows(6);
        lineView3.draw();

        /*lineView2 = (LineView) view.findViewById(R.id.lineView2);
        //lineView2.setDimensions(width, height);
        lineView2.setNumColumns(3);
        lineView2.setNumRows(7);
        lineView2.setA(A);
        lineView2.setB(B);
        lineView2.draw();*/
        return view;
    }


    private void jsonParse(int numberOfData, String time) throws JSONException {
        // time = "day" for dayly graph
        // time = "hour" for hourly graph
        // time = "minute" for minutely graph

        // Exapmles of links
        //https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&tsym=USD&limit=10
        //https://min-api.cryptocompare.com/data/v2/histominute?fsym=ETH&tsym=BTC&limit=2
        //https://min-api.cryptocompare.com/data/v2/histoday?fsym=ETC&tsym=BTC&limit=100
        String urlCoin = "https://min-api.cryptocompare.com/data/v2/histo" + time +
                "?fsym=" + symbolName  + "&tsym=BTC&limit=" + Integer.toString(numberOfData);
        final String sharedTime = time;
        // Historycal data per minute
        //String urlCoin = "https://min-api.cryptocompare.com/data/v2/histominute?fsym=";
        //urlCoin = urlCoin + symbolName + "&tsym=BTC&limit=" + Integer.toString(numberOfData);
        // BTC&tsym=USD&limit=2";
        //String url = "https://min-api.cryptocompare.com/data/top/totaltoptiervolfull?tsym=USD";
        //url = url + "&limit=" + Integer.toString(numerOfData);
        if(sharedTime == "day")
            timeForDay = new int[numberOfData+1];
        else if(sharedTime == "hour")
            timeForHour = new int[numberOfData+1];
        else
            timeForMinute = new int[numberOfData+1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlCoin, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray data = response.getJSONArray("Data");
                    for(int i=0; i<data.length();i++) {
                        JSONObject objectData = data.getJSONObject(i);
                        //JSONObject time = objectData.getJSONObject("time");

                        PointF dot = new PointF();
                        if(sharedTime == "day") {
                            timeForDay[i] = Integer.valueOf(objectData.getString("time"));
                        }
                        else if(sharedTime == "hour") {
                            timeForHour[i] = Integer.valueOf(objectData.getString("time"));
                        }
                        else {
                            timeForMinute[i] = Integer.valueOf(objectData.getString("time"));
                        }

                        dot.y = Float.valueOf(objectData.getString("open"));
                        dot.x = i;



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

    private String getMonthTextFromNum(String month){
        switch (month){
            case "01":
                month = "Jan"; break;
            case "02":
                month = "Feb"; break;
            case "03":
                month = "Mar"; break;
            case "04":
                month = "Apr"; break;
            case "05":
                month = "May"; break;
            case "06":
                month = "Jun"; break;
            case "07":
                month = "Jul"; break;
            case "08":
                month = "Aug"; break;
            case "09":
                month = "Sep"; break;
            case "10":
                month = "Okt"; break;
            case "11":
                month = "Nov"; break;
            case "12":
                month = "Dec"; break;
                default:
                    month = "Jan"; break;
        }
        return month;
    }


    private String getDate(long time, String nameTime) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(time * 1000);
        String month, day, hour, minute;
        month = DateFormat.format("MM", cal).toString();
        month = getMonthTextFromNum(month);
        day = DateFormat.format("dd", cal).toString();
        hour = DateFormat.format("hh", cal).toString();
        minute = DateFormat.format("mm", cal).toString();
        if(nameTime == "day"){

            return month + " " + day;
        }
        else if(nameTime == "hour") {
            return month + " " + day + " " + hour + ":00";
        }
        else if(nameTime == "minute") {
            return month + " " + day + " " + hour + " " + minute;
        }
        else
            return "";
    }






}
