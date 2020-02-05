/**
 * Database class DatabaseHandler which is extended from SQLiteOpenHelper.
 * This class is used to create Database, and 3 Tables. Users can read from tables and write into tables.
 */

package com.example.cryptocurrency;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "Cryptocurrency.db";
    // Tables name
    private static final String TABLE_COINS = "Cryptocurrency_table";
    private static final String TABLE_SELECTED_COIN = "Selected_coins";
    private static final String TABLE_GRAPH_LINE = "Graph_lines";

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

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
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

    /**
     * Insert single Coin into table Cryptocurrency_table. Result - boolean. For successful
     * database entry, the function returns true, in the opposite returns false.
     *
     * @param coin      - Cryptocurrency
     * @return boolean  - True for successful database entry, in the opposite false
     */
    public boolean writeCoinIntoDB(Coin coin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, coin.getNameCoin());
        contentValues.put(SYMBOL, coin.getSymbolCoin());
        contentValues.put(IMAGE, coin.getImageCoin());
        long result = db.insert(TABLE_COINS, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /**
     * Insert General Information into table Selected_coin. Result - boolean. For successful
     * database entry, the function returns true, in the opposite returns false.
     *
     * @param symbol        - Symbol of selected Cryptocurrency
     * @param textGenInfo   - General info of selected Cryptocurrency
     * @param textCompValue - Comparison values for selected Cryptocurrency
     * @return boolean      - True for successful database entry, in the opposite false
     */
    public boolean writeGeneralInfoIntoDB(String symbol, String textGenInfo, String textCompValue) {
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

    /**
     * Insert graph lines with dots into the table Graph_line. Result - boolean. For successful
     * database entry, the function returns true, in the opposite returns false.
     *
     * @param symbolFrom        - Symbol of selected Cryptocurrency
     * @param symbolsInString   - Symbols used for comparison
     * @param dotsXstring       - List of x values for PointF objects
     * @param dotsYstring       - List of y values for PointF objects
     * @param timeAxisString    - String of Time - seconds
     * @param timeFrame         - Time frame, e.g. "day", "hour", "minute"
     * @param numRows           - Number of rows for graph plotting
     * @param numColumns        - Number of columns for graph plotting
     * @return boolean          - True for successful database entry, in the opposite false
     */
    public boolean writeAllGraphLinesIntoDB(String symbolFrom, String symbolsInString, String dotsXstring, String dotsYstring, String timeAxisString, String timeFrame, int numRows, int numColumns) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYM_FROM, symbolFrom);
        contentValues.put(SYM_TO, symbolsInString );
        contentValues.put(POINT_X, dotsXstring);
        contentValues.put(POINT_Y, dotsYstring);
        contentValues.put(TIME, timeAxisString);
        contentValues.put(TIME_FRAME, timeFrame);
        contentValues.put(NUM_ROWS, numRows);
        contentValues.put(NUM_COLUMNS, numColumns);
        long result = db.insert(TABLE_GRAPH_LINE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    // Getting all data from table Cryptocurrency_table - All Cryptocurrencies.
    public Cursor readCoinsFromDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_COINS, null);
        return res;
    }

    // Getting all data from the table Selected_coin.
    public Cursor readGeneralInfoFromDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SELECTED_COIN, null);
        return res;
    }

    /**
     * Read graph lines from the table for specified symbol and time frame.
     *
     * @param symbolFrom    - Symbol of selected Cryptocurrency
     * @param timeFrame     - Time frame, e.g. "day", "hour", "minute"
     * @return Cursor       - Result is found from query
     */
    public Cursor readGraphLineFromDB(String symbolFrom, String timeFrame) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GRAPH_LINE + " WHERE " + SYM_FROM + " = '" + symbolFrom +
                "' AND " + TIME_FRAME + " = '" + timeFrame + "'", null);
        return res;
    }

    /**
     * Delete all graph lines from the table for specified symbol.
     *
     * @param symbolFrom    - Symbol of selected Cryptocurrency
     */
    public void deleteGraphBySymbol(String symbolFrom) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE + " WHERE " + SYM_FROM + " = '" + symbolFrom + "'");
        db.close();
    }

    /**
     * Delete Graph line from table Graph_line for the parameters used.
     *
     * @param symbolFrom    - Symbol of selected Cryptocurrency
     * @param timeFrame     - Time frame, e.g. "day", "hour", "minute"
     */
    public void deleteGraphTimeFrame(String symbolFrom, String timeFrame) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE + " WHERE " + SYM_FROM + " = '" + symbolFrom +
                "' AND " + TIME_FRAME + " = '" + timeFrame + "'");
        db.close();
    }

    // Delete content from all tables in the database.
    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_COINS);
        db.execSQL("DELETE FROM "+ TABLE_SELECTED_COIN);
        db.execSQL("DELETE FROM "+ TABLE_GRAPH_LINE);
        db.close();
    }

}
