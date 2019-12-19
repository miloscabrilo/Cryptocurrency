package com.example.cryptocurrency;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FragmentGraph extends Fragment implements View.OnClickListener {

    private View view;
    private TextView textCompare;
    private LineView lineView, lineView2, lineView3;
    private Button first1D, first1W, first2W, first1M;
    private Button second1D, second3D, second1W;
    private Button third1H, third3H, third1D;
    private Spinner spinnerSymbol;
    private String symbolName;
    private List<String> listSymbol;
    private List<String> selectedSymbol;


    public FragmentGraph() {
    }

    public FragmentGraph(String symbol) {
        symbolName = symbol;
    }

    public FragmentGraph(String symbol, List<String> symbols) {
        symbolName = symbol;
        listSymbol = symbols;
        listSymbol.remove(symbolName);
        listSymbol.remove("BTC");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineView = (LineView) view.findViewById(R.id.lineView);
        lineView2 = (LineView) view.findViewById(R.id.lineView2);
        lineView3 = (LineView) view.findViewById(R.id.lineView3);
        first1D = (Button) view.findViewById(R.id.first1D);
        first1W = (Button) view.findViewById(R.id.first1W);
        first2W = (Button) view.findViewById(R.id.first2W);
        first1M = (Button) view.findViewById(R.id.first1M);
        second1D = (Button) view.findViewById(R.id.second1D);
        second3D = (Button) view.findViewById(R.id.second3D);
        second1W = (Button) view.findViewById(R.id.second1W);
        third1H = (Button) view.findViewById(R.id.third1h);
        third3H = (Button) view.findViewById(R.id.third3h);
        third1D = (Button) view.findViewById(R.id.third1D);
        textCompare = (TextView) view.findViewById(R.id.textCompared);
        spinnerSymbol = (Spinner) view.findViewById(R.id.spinner);

        // Setting onClick method for all buttons.
        first1D.setOnClickListener(this);
        first1W.setOnClickListener(this);
        first2W.setOnClickListener(this);
        first1M.setOnClickListener(this);
        second1D.setOnClickListener(this);
        second3D.setOnClickListener(this);
        second1W.setOnClickListener(this);
        third1H.setOnClickListener(this);
        third3H.setOnClickListener(this);
        third1D.setOnClickListener(this);

        // Initial setting for Button color.
        initialSetButtonColor();



        textCompare.setText("Add a comparison to the " + symbolName + ":");
        textCompare.setTypeface(null, Typeface.BOLD);


        //listSymbol.add(0, "");
        //listSymbol.remove(symbolName);
        //editListSymbol(symbolName);
        selectedSymbol = new ArrayList<String>();
        selectedSymbol.add("BTC");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listSymbol
        );
        spinnerSymbol.setAdapter(adapter);
        spinnerSymbol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    selectedSymbol.add(adapter.getItem(position));
                    listSymbol.remove(position);
                    adapter.notifyDataSetChanged();
                    spinnerSymbol.setSelection(0);
                    initialSetButtonColor();
                    //jsonParse(
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Initial graph plotting. By day compared - for one day.
        // Setting number of columns and rows in which graph is plotted.
        lineView.setNumColumns(1);
        lineView.setNumRows(1);
        lineView.setSymbolName(symbolName);
        try {
            lineView.draw(1, "day");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initial graph plotting. By hour compared - for one day.
        lineView2.setNumColumns(6);
        lineView2.setNumRows(5);
        lineView2.setSymbolName(symbolName);
        try {
            lineView2.draw(24, "hour");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initial graph plotting. By minute compared - for one hour.
        lineView3.setNumColumns(6);
        lineView3.setNumRows(5);
        lineView3.setSymbolName(symbolName);
        try {
            lineView3.draw(60, "minute");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void initialSetButtonColor() {
        first1W.setBackgroundColor(Color.WHITE);
        first2W.setBackgroundColor(Color.WHITE);
        first1M.setBackgroundColor(Color.WHITE);
        second3D.setBackgroundColor(Color.WHITE);
        second1W.setBackgroundColor(Color.WHITE);
        third3H.setBackgroundColor(Color.WHITE);
        third1D.setBackgroundColor(Color.WHITE);

        // Initial activated Buttons.
        first1D.setBackgroundColor(Color.rgb(0, 157, 111));
        second1D.setBackgroundColor(Color.rgb(0, 157, 111));
        third1H.setBackgroundColor(Color.rgb(0, 157, 111));
    }

    // Redefine onClick method for Buttons.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first1D:
                lineView.setNumColumns(1);
                lineView.setNumRows(1);
                try {
                    lineView.jsonParse(1, "day");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                first1W.setBackgroundColor(Color.WHITE);
                first2W.setBackgroundColor(Color.WHITE);
                first1M.setBackgroundColor(Color.WHITE);
                first1D.setBackgroundColor(Color.rgb(0, 157, 111));
                break;
            case R.id.first1W:
                lineView.setNumColumns(7);
                lineView.setNumRows(5);
                try {
                    lineView.jsonParse(7, "day");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                first1D.setBackgroundColor(Color.WHITE);
                first2W.setBackgroundColor(Color.WHITE);
                first1M.setBackgroundColor(Color.WHITE);
                first1W.setBackgroundColor(Color.rgb(0, 157, 111));
                break;
            case R.id.first2W:
                lineView.setNumColumns(7);
                lineView.setNumRows(5);
                try {
                    lineView.jsonParse(14, "day");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                first1D.setBackgroundColor(Color.WHITE);
                first1W.setBackgroundColor(Color.WHITE);
                first1M.setBackgroundColor(Color.WHITE);
                first2W.setBackgroundColor(Color.rgb(0, 157, 111));
                break;
            case R.id.first1M:
                lineView.setNumColumns(6);
                lineView.setNumRows(5);
                try {
                    lineView.jsonParse(30, "day");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                first1D.setBackgroundColor(Color.WHITE);
                first1W.setBackgroundColor(Color.WHITE);
                first2W.setBackgroundColor(Color.WHITE);
                first1M.setBackgroundColor(Color.rgb(0, 157, 111));
                break;
            case R.id.second1D:
                lineView2.setNumColumns(6);
                lineView2.setNumRows(5);
                try {
                    lineView2.jsonParse(24, "hour");
                    second3D.setBackgroundColor(Color.WHITE);
                    second1W.setBackgroundColor(Color.WHITE);
                    second1D.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.second3D:
                lineView2.setNumColumns(6);
                lineView2.setNumRows(5);
                try {
                    lineView2.jsonParse(72, "hour");
                    second1D.setBackgroundColor(Color.WHITE);
                    second1W.setBackgroundColor(Color.WHITE);
                    second3D.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.second1W:
                lineView2.setNumColumns(6);
                lineView2.setNumRows(5);
                try {
                    lineView2.jsonParse(168, "hour");
                    second1D.setBackgroundColor(Color.WHITE);
                    second3D.setBackgroundColor(Color.WHITE);
                    second1W.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.third1h:
                lineView3.setNumColumns(6);
                lineView3.setNumRows(5);
                try {
                    lineView3.jsonParse(60, "minute");
                    third1H.setBackgroundColor(Color.rgb(0, 157, 111));
                    third3H.setBackgroundColor(Color.WHITE);
                    third1D.setBackgroundColor(Color.WHITE);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.third3h:
                lineView3.setNumColumns(6);
                lineView3.setNumRows(5);
                try {
                    lineView3.jsonParse(180, "minute");
                    third1H.setBackgroundColor(Color.WHITE);
                    third3H.setBackgroundColor(Color.rgb(0, 157, 111));
                    third1D.setBackgroundColor(Color.WHITE);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.third1D:
                lineView3.setNumColumns(6);
                lineView3.setNumRows(5);
                try {
                    lineView3.jsonParse(1440, "minute");
                    third1H.setBackgroundColor(Color.WHITE);
                    third3H.setBackgroundColor(Color.WHITE);
                    third1D.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

}
