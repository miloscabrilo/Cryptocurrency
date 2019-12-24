package com.example.cryptocurrency;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.PointF;

public class CryptocurrencyDatabase extends SQLiteOpenHelper {
    //private static final String DATABASE_NAME = "Cryptocurrency.db";
    // Database name
    private static final String DATABASE_NAME = "Cryptocurrency.db";
    // Tables name
    private static final String TABLE_COINS = "Cryptocurrency_table";
    private static final String TABLE_SELECTED_COIN = "Selected_coin";
    private static final String TABLE_GRAPH_LINE = "Graph_line";


    // Fields for table TABLE_COINS
    private static final String NAME = "name";
    private static final String SYMBOL = "symbol";
    private static final String IMAGE = "image";

    // Fields for table TABLE_SELECTED_COIN
    private static final String SYM = "sym";
    private static final String GEN_INFO = "general_info";
    private static final String COMP_VALUE = "compared_value";

    // Fields for table TABLE_GRAPH_LINE
    private static final String SYM_FROM = "sym_from";
    private static final String SYM_TO = "sym_to";
    private static final String POINT_X = "point_x";
    private static final String POINT_Y = "point_y";
    private static final String TIME = "time";
    private static final String TIME_FRAME = "time_frame";
    private static final String NUM_ROWS = "num_rows";
    private static final String NUM_COLUMNS = "num_columns";

    //private static CryptocurrencyDatabase instance;

    public CryptocurrencyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }


    public CryptocurrencyDatabase(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_COINS + " (" + NAME + " TEXT, " +SYMBOL + " TEXT, " + IMAGE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_SELECTED_COIN + " (" +SYM + " TEXT, " + GEN_INFO + " TEXT, " + COMP_VALUE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_GRAPH_LINE + " (" +SYM_FROM + " TEXT, " + SYM_TO + " TEXT, " +
                POINT_X + " FLOAT, " + POINT_Y + " FLOAT, " + TIME + " INTEGER, " + TIME_FRAME + " TEXT, " + NUM_ROWS +
                " INTEGER, " + NUM_COLUMNS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_COIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRAPH_LINE);
        onCreate(db);
    }

    public Cursor getData(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public boolean insertCoins(String name, String symbol, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(SYMBOL, symbol);
        contentValues.put(IMAGE, image);
        long result = db.insert(TABLE_COINS, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertGenInfo(String symbol, String textGenInfo, String textCompValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYM, symbol);
        contentValues.put(GEN_INFO, textGenInfo);
        contentValues.put(COMP_VALUE, textCompValue);
        long result = db.insert(TABLE_SELECTED_COIN, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertGraphLine(String symbolFrom, String symbolTo, float pointX, float pointY, int time, String timeFrame, int numRows, int numColumns) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYM_FROM, symbolFrom);
        contentValues.put(SYM_TO, symbolTo);
        contentValues.put(POINT_X, pointX);
        contentValues.put(POINT_Y, pointY);
        contentValues.put(TIME, time);
        contentValues.put(TIME_FRAME, timeFrame);
        contentValues.put(NUM_ROWS, numRows);
        contentValues.put(NUM_COLUMNS, numColumns);
        long result = db.insert(TABLE_GRAPH_LINE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor readCoins(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_COINS, null);
        return res;
    }

    public Cursor readSelectedCoin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SELECTED_COIN, null);
        return res;
    }

    public Cursor readGraphLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GRAPH_LINE, null);
        return res;
    }

    public Cursor readGraphLine(String symbolFrom, String timeFrame) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GRAPH_LINE + " WHERE " + SYM_FROM + " = '" + symbolFrom +
                "' AND " + TIME_FRAME + " = '" + timeFrame + "'", null);
        return res;
    }

    public void deleteGraphLine(String symbolFrom, String symbolTo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE + " WHERE " + SYM_FROM + " = '" + symbolFrom +
                "' AND " + SYM_TO + " = '" + symbolTo + "'");
        db.close();
    }

    public void deleteSecetedCoin()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SELECTED_COIN);
        db.close();
    }

    public void deleteCoins() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_COINS);
        db.close();
    }

    public void deleteGraphs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE);
        db.close();
    }

    public void deleteAllTAbles() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_COINS);
        db.execSQL("DELETE FROM "+ TABLE_SELECTED_COIN);
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE);
        db.close();
    }

}
