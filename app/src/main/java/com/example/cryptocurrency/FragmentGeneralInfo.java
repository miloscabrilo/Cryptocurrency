package com.example.cryptocurrency;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentGeneralInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SimpleDateFormat formatter;
    View view;
    String[] listToCompare = new String[] {
            "BTC", "ETH", "EVN", "DOGE", "ZEC", "USD", "EUR"
    };
    ArrayAdapter<TextView> arrayAdapterTV;
    TextView textView;
    TextView proba;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String symbolName;


    public FragmentGeneralInfo() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGeneralInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGeneralInfo newInstance(String param1, String param2) {
        FragmentGeneralInfo fragment = new FragmentGeneralInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private class TextViewHolder {
        TextView nameOfCoin;
        TextView valueOfCoin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            symbolName = getArguments().getString("symbolName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextViewHolder viewHolder = new TextViewHolder();
        //View view2 = inflater.inflate(R.layout.coin_details_tab, container, false);

        view = inflater.inflate(R.layout.fragment_general_info, container, false);

        textView = view.findViewById(R.id.textLowHigh3);
        textView.setText(symbolName);
        for(int i = 0; i < listToCompare.length; i++) {
            //viewHolder.nameOfCoin =  (Te)
            i++;
        }
        // 30-08-2019  => 1567123200
        // 1575676800
        String date = getDate(1574985800);
        //textView.setText(date);
        return view;

    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("mm", cal).toString();


        //String date = DateFormat.format("dd-MM-yyyy & hh-mm-ss", cal).toString();
        return date;
    }



}
