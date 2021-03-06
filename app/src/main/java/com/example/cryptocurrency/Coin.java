/**
 * Class Coin: 3 main parameters - name, symbol and image.
 */

package com.example.cryptocurrency;


public class Coin {

    private String imageCoin;
    private String nameCoin;
    private String symbolCoin;

    public Coin(String image, String name, String symbol) {
        this.setImageCoin(image);
        this.setNameCoin(name);
        this.setSymbolCoin(symbol);
    }
    public String getNameCoin() { return nameCoin; }

    public void setNameCoin(String nameCoin) { this.nameCoin = nameCoin;}

    public String getSymbolCoin() { return symbolCoin; }

    public void setSymbolCoin(String symbolCoin) { this.symbolCoin = symbolCoin;}

    public String getImageCoin() { return imageCoin; }

    public void setImageCoin(String imageCoin) { this.imageCoin = imageCoin;}


}

