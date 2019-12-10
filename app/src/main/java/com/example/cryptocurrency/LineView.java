package com.example.cryptocurrency;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class LineView extends View {
    private Paint lineChart = new Paint();
    private Paint textChart = new Paint();
    private Paint axisChart = new Paint();
    private PointF A, B;
    private Rect rect = new Rect(100, 100, 700, 500);

    private String symbolName;
    private int width, height;
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private boolean[][] cellChecked;

    public LineView(Context context) {
        super(context);
    }


    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawGraph(float values, Paint paintText, Paint paintAxis, Paint paintLine){

    }

    /*public void initializeArguments(Paint paintText, Paint paintAxis, Paint paintLine) {
        axisChart = paintAxis;
        //axisChart.setStyle(Paint.Style.STROKE);
        lineChart = paintLine;
        axisChart = paintAxis;
    }*/

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*lineChart.setColor(Color.RED);
        lineChart.setStrokeWidth(10);
        axisChart.setColor(Color.rgb(100, 200, 255));
        axisChart.setStrokeWidth(3);
        axisChart.setStyle(Paint.Style.STROKE);
        textChart.setColor(Color.BLACK);
        textChart.setTextSize(20);
        canvas.drawLine(A.x, A.y, B.x, B.y, lineChart);
        canvas.drawText("BTC", 200, 200, textChart);
        canvas.drawRect(rect, axisChart);*/
        lineChart.setColor(Color.RED);
        lineChart.setStrokeWidth(10);
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        width = getWidth();
        height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {

                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            blackPaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }

        //canvas.drawLine(A.x, A.y, B.x, B.y, lineChart);
        //canvas.drawText(symbolName, 300, 300, lineChart);
        super.onDraw(canvas);
    }

    public void setA(PointF point) {
        A = point;
    }
    public void setB(PointF point) {
        B = point;
    }

    public void draw(){
        invalidate();
        requestLayout();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if((int)(event.getX()) < cellWidth * numColumns && (int)(event.getY()) < cellHeight * numRows) {
                cellChecked[column][row] = !cellChecked[column][row];
                invalidate();
            }
        }

        return true;
    }*/

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = (getWidth()) / numColumns;
        cellHeight = (getHeight()) / numRows;

        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }


    public void setSymbolName(String name) {
        symbolName = name;
    }
}
