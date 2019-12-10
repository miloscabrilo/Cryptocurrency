package com.example.cryptocurrency;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CoinDetails{
    /*
    private Coin coin;
    private String marketCap;
    private String totVol24;
    private String dirVol24;
    private String dirVol24dolar;
    private String open24;
    private String lowHigh;
    private TextView coinName;
    private ImageView image;*/
    private ImageView image;
    private TextView coinName;
    private Spinner spinner;

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_info, container, false);
        //spinner = (Spinner) findViewById(R.id.spinner);
    }*/



    /*
    public CoinDetails(Coin coin, String marketCap, String totVol24, String dirVol24, String dirVol24Dolar, String open24, String lowHigh){
        setCoin(coin);
        setMarketCap(marketCap);
        setTotVol24(totVol24);
        setDirVol24(dirVol24);
        setDirVol24dolar(dirVol24Dolar);
        setOpen24(open24);
        setLowHigh(lowHigh);

    }

    public void initialize(){
        //coinName = (TextView) findViewById(R.ID.)
        //Intent intent = new Intent(this, )
    }

    public Coin getCoin() { return coin;}
    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    public String getMarketCap() { return marketCap;}
    public String getTotVol24() { return totVol24;}
    public String getDirVol24() { return dirVol24;}
    public String getDirVol24dolar() { return dirVol24dolar;}
    public String getOpen24() { return open24;}
    public String getLowHigh() { return lowHigh;}

    public void setMarketCap(String m) {    this.marketCap = m;}
    public void setTotVol24(String s) {     this.totVol24 = s;}
    public void setDirVol24(String s) {     this.dirVol24 = s;}
    public void setDirVol24dolar(String s){ this.dirVol24dolar = s;}
    public void setOpen24(String s) {       this.open24 = s;}
    public void setLowHigh(String s) {      this.lowHigh = s;}*/

}
