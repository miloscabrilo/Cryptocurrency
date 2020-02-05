/**
 * An asynchronous task for receiving data from internet, plotting the graph and storing data in database.
 */

package com.example.cryptocurrency;

import android.graphics.PointF;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReceivePoints extends AsyncTask<Void, Void, Void> {
    private String data;
    private String timeFrame;
    private URL url;
    private int numberOfData;
    private String selSymbol;
    private int numRows, numColumns;
    private int[] timeAxis;
    private List<String> listOfSymbols = new ArrayList<>();
    private List<PointF> points;
    private GraphView graph;
    private String dotsXstring;
    private String dotsYstring;
    private String symbolsInString;
    private String timeAxisString;

    // Initial setup for this object.
    public ReceivePoints(int numberOfData, String timeFrame, int numRows, int numColumns, String selSymbol, List<String> listOfSymbols, GraphView graph){
        this.numberOfData = numberOfData;
        this.timeFrame = timeFrame;
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.selSymbol = selSymbol;
        this.listOfSymbols = listOfSymbols;
        this.timeAxis = new int[numberOfData + 1];
        this.graph = graph;
        this.data = "";
        this.points = new ArrayList<>();
        this.dotsXstring = "";
        this.dotsYstring = "";
        this.symbolsInString = "";
        this.timeAxisString = "";
    }

    // Receiving data from url connections. JSON procedure is used to separate necessary data.
    @Override
    protected Void doInBackground(Void... voids) {
        for ( int i = 0; i < listOfSymbols.size(); i++) {
            // Preparing URL address.
            try {
                url = new URL("https://min-api.cryptocompare.com/data/v2/histo" + timeFrame +
                        "?fsym=" + selSymbol + "&tsym=" + listOfSymbols.get(i) + "&limit=" + numberOfData);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Generating JSON array using string - data. Cropping all characters between symbols '[' and ']'.
                String line = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                data = data.substring(data.indexOf("["));
                data = data.substring(0, data.indexOf("]") + 1);
                JSONArray JAdata = new JSONArray(data);

                // Deserializing all JSON objects and reading necessary data. (X and Y values, time values)
                for(int j = 0; j < JAdata.length(); j++) {
                    JSONObject JOdata = (JSONObject) JAdata.get(j);
                    String y = JOdata.getString("close");
                    PointF dot = new PointF(j, Float.valueOf(y));
                    points.add(dot);
                    dotsXstring = dotsXstring + j + "\n";
                    dotsYstring = dotsYstring + y + "\n";
                    if(points.size() <= numberOfData + 1) {     // Only once reading.
                        timeAxis[j] = Integer.valueOf(JOdata.getString("time"));
                        timeAxisString = timeAxisString + JOdata.getString("time") + "\n";
                    }
                }
                data = "";
                symbolsInString = symbolsInString + listOfSymbols.get(i) + "\n";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected  void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);

        // Drawing graph after receiving data from internet.
        // Storing data into database.
        if( points.size() == listOfSymbols.size() * (numberOfData + 1)) {
            graph.setAllDrawingParameters(points, timeAxis, timeFrame, numRows, numColumns, listOfSymbols, selSymbol);
            if( !symbolsInString.equals(""))
                MainActivity.db.writeAllGraphLinesIntoDB(selSymbol, symbolsInString, dotsXstring, dotsYstring, timeAxisString, timeFrame, numRows, numColumns);
        }
    }

}
