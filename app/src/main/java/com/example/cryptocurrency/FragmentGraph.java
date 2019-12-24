package com.example.cryptocurrency;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
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
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FragmentGraph extends Fragment implements View.OnClickListener {

    private View view;
    private TextView addCompare, deleteCompare;
    private LineView lineView, lineView2, lineView3;
    private Button first1D, first1W, first2W, first1M;
    private Button second1D, second3D, second1W;
    private Button third1H, third3H, third1D;
    private Spinner addSpinner, deleteSpinner;
    private String symbolName;
    private List<String> listSymbol;
    private List<String> listOfSelectedSymbol;
    private List<PointF> pointsList = new ArrayList<>();
    private List<PointF> pointsList2 = new ArrayList<>();
    private List<PointF> pointsList3 = new ArrayList<>();
    private int[] timeAxis;
    private boolean internetAccess;
    private CryptocurrencyDatabase db;


    public FragmentGraph() {
    }

    public FragmentGraph(String symbol) {
        symbolName = symbol;
    }

    public FragmentGraph(String symbol, List<String> symbols, boolean internetAccess) {
        symbolName = symbol;
        listSymbol = symbols;
        listSymbol.remove(symbolName);
        listSymbol.remove("BTC");
        this.internetAccess = internetAccess;
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
        addCompare = (TextView) view.findViewById(R.id.add_Compared);
        addSpinner = (Spinner) view.findViewById(R.id.add_spinner);
        deleteCompare = (TextView) view.findViewById(R.id.delete_Compared);
        deleteSpinner = (Spinner) view.findViewById(R.id.delete_spinner);

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

        db = new CryptocurrencyDatabase(getContext());

        // The ability to select a symbol to display as a multiple comparison on the same graph.
        // Spinner consists of loaded symbols.
        addCompare.setText("Add a comparison to the " + symbolName + ":");
        addCompare.setTypeface(null, Typeface.BOLD);

        // The ability to delete a symbol from multiple comparison.
        // Spinner consists of shown symbols.
        deleteCompare.setText("Delete shown comparison with " + symbolName + ":");
        deleteCompare.setTypeface(null, Typeface.BOLD);

        listOfSelectedSymbol = new ArrayList<String>();
        listOfSelectedSymbol.add("Select");
        listOfSelectedSymbol.add("BTC");

        final ArrayAdapter<String> adapterForAdd = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listSymbol
        );
        final ArrayAdapter<String> adapterForDelete = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listOfSelectedSymbol
        );
        addSpinner.setAdapter(adapterForAdd);
        deleteSpinner.setAdapter(adapterForDelete);

        addSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(internetAccess) {
                    if(position != 0) {
                        listOfSelectedSymbol.add(adapterForAdd.getItem(position));
                        adapterForDelete.notifyDataSetChanged();
                        listSymbol.remove(position);
                        adapterForAdd.notifyDataSetChanged();
                        addSpinner.setSelection(0);
                        initialSetButtonColor();
                        initialDrawing();
                    }
                }
                else {
                    addSpinner.setSelection(0);
                    /*Toast toast = Toast.makeText(getContext(),
                            "No Internet Connection",
                            Toast.LENGTH_SHORT);
                    toast.show();*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(internetAccess) {
                    if(position != 0) {
                        listSymbol.add(adapterForDelete.getItem(position));
                        adapterForAdd.notifyDataSetChanged();
                        listOfSelectedSymbol.remove(position);
                        adapterForDelete.notifyDataSetChanged();
                        deleteSpinner.setSelection(0);
                        initialSetButtonColor();
                        initialDrawing();
                    }
                }
                else {
                    if(position != 0) {
                        /*db.deleteGraphLine(symbolName, adapterForDelete.getItem(position));
                        listSymbol.add(adapterForDelete.getItem(position));
                        adapterForAdd.notifyDataSetChanged();
                        listOfSelectedSymbol.remove(position);
                        adapterForDelete.notifyDataSetChanged();*/
                        deleteSpinner.setSelection(0);
                        //initialDrawing();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initial setting for Button color.
        initialSetButtonColor();

        // Initial drawing.
        initialDrawing();



        return view;
    }

    // Parse ArrayList to delete element "Select"
    public List<String> parseList(List<String> list) {
        List<String> newList = new ArrayList<>();
        if(list.size() > 1) {
            for(int i = 1; i < list.size(); i++)
                newList.add(i - 1, list.get(i));
        }
        return newList;
    }

    // InitialDrawing
    public void initialDrawing() {
        if(internetAccess) {
            db.deleteGraphs();
            // Initial graph plotting. By day compared - for one day.
            try {
                lineView.draw(1, "day", 2, 1, parseList(listOfSelectedSymbol), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Initial graph plotting. By hour compared - for one day.
            try {
                lineView2.draw(24, "hour", 5, 6, parseList(listOfSelectedSymbol), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Initial graph plotting. By minute compared - for one hour.
            try {
                lineView3.draw(60, "minute", 5, 6, parseList(listOfSelectedSymbol), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {

            drawGraphsOffline(lineView, "day", pointsList);
            drawGraphsOffline(lineView2, "hour", pointsList2);
            drawGraphsOffline(lineView3, "minute", pointsList3);
        }
    }

    // Setting initial color for all buttons.
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
        if(internetAccess) {
            switch (v.getId()) {
                case R.id.first1D:
                    try {
                        lineView.jsonParse(1, "day", 1, 1, parseList(listOfSelectedSymbol), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1W.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first1D.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first1W:
                    try {
                        lineView.jsonParse(7, "day", 5, 7, parseList(listOfSelectedSymbol), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first2W:
                    try {
                        lineView.jsonParse(14, "day", 5, 7, parseList(listOfSelectedSymbol), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first1M:
                    try {
                        lineView.jsonParse(30, "day", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.second1D:
                    try {
                        lineView2.jsonParse(24, "hour", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        second3D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.WHITE);
                        second1D.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.second3D:
                    try {
                        lineView2.jsonParse(72, "hour", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        second1D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.WHITE);
                        second3D.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.second1W:
                    try {
                        lineView2.jsonParse(168, "hour", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        second1D.setBackgroundColor(Color.WHITE);
                        second3D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third1h:
                    try {
                        lineView3.jsonParse(60, "minute", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        third1H.setBackgroundColor(Color.rgb(0, 157, 111));
                        third3H.setBackgroundColor(Color.WHITE);
                        third1D.setBackgroundColor(Color.WHITE);
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third3h:
                    try {
                        lineView3.jsonParse(180, "minute", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        third1H.setBackgroundColor(Color.WHITE);
                        third3H.setBackgroundColor(Color.rgb(0, 157, 111));
                        third1D.setBackgroundColor(Color.WHITE);
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third1D:
                    try {
                        lineView3.jsonParse(1440, "minute", 5, 6, parseList(listOfSelectedSymbol), symbolName);
                        third1H.setBackgroundColor(Color.WHITE);
                        third3H.setBackgroundColor(Color.WHITE);
                        third1D.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
        else {
            Toast toast = Toast.makeText(getContext(),
                    "No Internet Connection",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void drawGraphsOffline(LineView lineView, String timeFrame, List<PointF> points) {
        if(listOfSelectedSymbol.size() > 0)
            listOfSelectedSymbol.clear();
        listOfSelectedSymbol.add("Select");
        Cursor res = db.readGraphLine(symbolName,  timeFrame);
        timeAxis = new int[res.getCount()];
        int i = 0;
        int numColumns = 1;
        int numRows = 1;
        while(res.moveToNext()){
            PointF dot = new PointF(res.getFloat(2), res.getFloat(3));
            points.add(dot);
            numRows = res.getInt(6);
            numColumns = res.getInt(7);
            timeAxis[i] = res.getInt(4);
            if(!listOfSelectedSymbol.contains(res.getString(1)))
                listOfSelectedSymbol.add(res.getString(1));
            i++;
        }
        if(parseList(listOfSelectedSymbol).size() == 0)
            return;
        int[] time = new int[points.size() / parseList(listOfSelectedSymbol).size()];
        for(int j = 0; j < time.length; j++) {
            time[j] = timeAxis[j];
        }
        if(points.size() > 0)
            lineView.setAllDrawingParameters(points, time, timeFrame, numRows, numColumns, parseList(listOfSelectedSymbol), symbolName);

    }

}
