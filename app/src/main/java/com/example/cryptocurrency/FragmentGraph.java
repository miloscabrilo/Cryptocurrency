/**
 * Second - Graph fragment.
 */

package com.example.cryptocurrency;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
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
    private GraphView graphView, graphView2, graphView3;
    private Button first1D, first1W, first2W, first1M;
    private Button second1D, second3D, second1W;
    private Button third1H, third3H, third1D;
    private Spinner addSpinner, deleteSpinner;
    private String symbolName;
    private List<String> listOfAllAvailableSymbols;
    private List<String> listOfAddedSymbolsOnGraph;
    private List<PointF> pointsList = new ArrayList<>();
    private List<PointF> pointsList2 = new ArrayList<>();
    private List<PointF> pointsList3 = new ArrayList<>();
    private int[] timeAxis;

    public FragmentGraph() {
    }


    public FragmentGraph(String symbol, List<String> symbols) {
        symbolName = symbol;
        listOfAllAvailableSymbols = symbols;
        listOfAllAvailableSymbols.remove(symbolName);
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
        graphView = (GraphView) view.findViewById(R.id.graphView);
        graphView2 = (GraphView) view.findViewById(R.id.graphView2);
        graphView3 = (GraphView) view.findViewById(R.id.graphView3);
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

        // The ability to select a symbol to display as a multiple comparison on the same graph.
        // Spinner consists of loaded symbols.
        addCompare.setText("Add a comparison to the " + symbolName + ":");
        addCompare.setTypeface(null, Typeface.BOLD);

        // The ability to delete a symbol from multiple comparison.
        // Spinner consists of shown symbols.
        deleteCompare.setText("Delete shown comparison with " + symbolName + ":");
        deleteCompare.setTypeface(null, Typeface.BOLD);

        listOfAddedSymbolsOnGraph = new ArrayList<String>();
        listOfAddedSymbolsOnGraph.add("Select");
        if (!symbolName.equals("BTC")) {
            listOfAddedSymbolsOnGraph.add("BTC");
            listOfAllAvailableSymbols.remove("BTC");
        }
        else {  // if Selected symbol is BTC. Show comparison with first non BTC coin.
            for(int i = 0; i < listOfAllAvailableSymbols.size(); i++)
                if(!listOfAllAvailableSymbols.get(i).equals("BTC") && !listOfAllAvailableSymbols.get(i).equals("Select")) {
                    listOfAddedSymbolsOnGraph.add(listOfAllAvailableSymbols.get(i));
                    listOfAllAvailableSymbols.remove(listOfAllAvailableSymbols.get(i));
                    break;
                }

        }

        final ArrayAdapter<String> adapterForAdd = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listOfAllAvailableSymbols
        );
        final ArrayAdapter<String> adapterForDelete = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listOfAddedSymbolsOnGraph
        );
        addSpinner.setAdapter(adapterForAdd);
        deleteSpinner.setAdapter(adapterForDelete);

        addSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isNetworkConnected()) {
                    if(position != 0) {
                        listOfAddedSymbolsOnGraph.add(adapterForAdd.getItem(position));
                        adapterForDelete.notifyDataSetChanged();
                        listOfAllAvailableSymbols.remove(position);
                        adapterForAdd.notifyDataSetChanged();
                        addSpinner.setSelection(0);
                        MainActivity.db.deleteGraphs();
                        initialSetButtonColor();
                        initialDrawing();
                    }
                }
                else {
                    addSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isNetworkConnected()) {
                    if(position != 0) {
                        listOfAllAvailableSymbols.add(adapterForDelete.getItem(position));
                        adapterForAdd.notifyDataSetChanged();
                        listOfAddedSymbolsOnGraph.remove(position);
                        adapterForDelete.notifyDataSetChanged();
                        deleteSpinner.setSelection(0);
                        MainActivity.db.deleteGraphBySymbol(symbolName);
                        initialSetButtonColor();
                        initialDrawing();
                    }
                }
                else {
                    if(position != 0) {
                        deleteSpinner.setSelection(0);
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
    public List<String> deleteFirstElemOfList(List<String> list) {
        List<String> newList = new ArrayList<>();
        if(list.size() > 1) {
            for(int i = 1; i < list.size(); i++)
                newList.add(i - 1, list.get(i));
        }
        return newList;
    }

    // InitialDrawing
    public void initialDrawing() {
        if(isNetworkConnected()) {
            // Initial graph plotting. By day compared - for one day.
            try {
                graphView.draw(1, "day", 2, 1, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Initial graph plotting. By hour compared - for one day.
            try {
                graphView2.draw(24, "hour", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Initial graph plotting. By minute compared - for one hour.
            try {
                graphView3.draw(60, "minute", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {

            // Draw Graph lines in case users don't have an internet access.
            drawGraphsOffline(graphView, "day", pointsList);
            drawGraphsOffline(graphView2, "hour", pointsList2);
            drawGraphsOffline(graphView3, "minute", pointsList3);
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

        if(isNetworkConnected()) {
            // Initial activated Buttons.
            first1D.setBackgroundColor(Color.rgb(0, 157, 111));
            second1D.setBackgroundColor(Color.rgb(0, 157, 111));
            third1H.setBackgroundColor(Color.rgb(0, 157, 111));
        } else {
            first1D.setBackgroundColor(Color.WHITE);
            second1D.setBackgroundColor(Color.WHITE);
            third1H.setBackgroundColor(Color.WHITE);
        }

    }

    // Redefine onClick method for Buttons.
    @Override
    public void onClick(View v) {
        if(isNetworkConnected()) {
            switch (v.getId()) {
                case R.id.first1D:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "day");
                    try {
                        graphView.readGraphPointsFromUrl(1, "day", 1, 1, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1W.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first1D.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first1W:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "day");
                    try {
                        graphView.readGraphPointsFromUrl(7, "day", 5, 7, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first2W:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "day");
                    try {
                        graphView.readGraphPointsFromUrl(14, "day", 5, 7, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.first1M:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "day");
                    try {
                        graphView.readGraphPointsFromUrl(30, "day", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    first1D.setBackgroundColor(Color.WHITE);
                    first1W.setBackgroundColor(Color.WHITE);
                    first2W.setBackgroundColor(Color.WHITE);
                    first1M.setBackgroundColor(Color.rgb(0, 157, 111));
                    break;
                case R.id.second1D:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "hour");
                    try {
                        graphView2.readGraphPointsFromUrl(24, "hour", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                        second3D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.WHITE);
                        second1D.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.second3D:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "hour");
                    try {
                        graphView2.readGraphPointsFromUrl(72, "hour", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                        second1D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.WHITE);
                        second3D.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.second1W:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "hour");
                    try {
                        graphView2.readGraphPointsFromUrl(168, "hour", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                        second1D.setBackgroundColor(Color.WHITE);
                        second3D.setBackgroundColor(Color.WHITE);
                        second1W.setBackgroundColor(Color.rgb(0, 157, 111));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third1h:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "minute");
                    try {
                        graphView3.readGraphPointsFromUrl(60, "minute", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                        third1H.setBackgroundColor(Color.rgb(0, 157, 111));
                        third3H.setBackgroundColor(Color.WHITE);
                        third1D.setBackgroundColor(Color.WHITE);
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third3h:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "minute");
                    try {
                        graphView3.readGraphPointsFromUrl(180, "minute", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
                        third1H.setBackgroundColor(Color.WHITE);
                        third3H.setBackgroundColor(Color.rgb(0, 157, 111));
                        third1D.setBackgroundColor(Color.WHITE);
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case R.id.third1D:
                    MainActivity.db.deleteGraphTimeFrame(symbolName, "minute");
                    try {
                        graphView3.readGraphPointsFromUrl(1440, "minute", 5, 6, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
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

    /**
     * Read data from the database for given parameters.
     *
     * @param graphView  - Graph view in which the graph is drawn
     * @param timeFrame - Time frame, e.g. "day", "hour", "minute"
     * @param points    - List of points which would be displayed
     */
    public void drawGraphsOffline(GraphView graphView, String timeFrame, List<PointF> points) {
        if(listOfAddedSymbolsOnGraph.size() > 0)
            listOfAddedSymbolsOnGraph.clear();
        listOfAddedSymbolsOnGraph.add("Select");
        Cursor res = MainActivity.db.readGraphLineFromDB(symbolName,  timeFrame);
        timeAxis = new int[res.getCount()];
        int i = 0;
        int numColumns = 1;
        int numRows = 1;
        // Reading point by point from database.
        while(res.moveToNext()){
            PointF dot = new PointF(res.getFloat(2), res.getFloat(3));
            points.add(dot);
            numRows = res.getInt(6);
            numColumns = res.getInt(7);
            timeAxis[i] = res.getInt(4);
            if(!listOfAddedSymbolsOnGraph.contains(res.getString(1)))
                listOfAddedSymbolsOnGraph.add(res.getString(1));
            i++;
        }
        if(deleteFirstElemOfList(listOfAddedSymbolsOnGraph).size() == 0)
            return;
        int[] time = new int[points.size() / deleteFirstElemOfList(listOfAddedSymbolsOnGraph).size()];
        for(int j = 0; j < time.length; j++) {
            time[j] = timeAxis[j];
        }
        if(points.size() > 0)
            // Call method for setting and drawing Graph.
            graphView.setAllDrawingParameters(points, time, timeFrame, numRows, numColumns, deleteFirstElemOfList(listOfAddedSymbolsOnGraph), symbolName);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
