package com.example.dovydas.dots_reborn;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by dovydas on 9/10/2015.
 */
public class Point {
    private int _row;
    private int _col;
    private double _x;
    private double _y;
    private int _color;
    private boolean _marked;
    private RectF _circle;
    private Paint _paint;


    public Point(int row, int col, int color, Paint paint, RectF circle, boolean match){
        _row = row;
        _col = col;
        _color = color;
        _circle = circle;
        _paint = paint;
        _marked = match;
    }

    public RectF getCircle(){
        return _circle;
    }

    public Paint getPaint(){
        return _paint;
    }

    public int getColor(){
        return _color;
    }

    public int getRow(){
        return _row;
    }

    public void setRow(int row){
        _row = row;
    }

    public int getCol(){
        return _col;
    }

    public void setCol(int col){
        _col = col;
    }

    public boolean getMarked(){
        return _marked;
    }

    public void setMarked(boolean marked){
        _marked = marked;
    }


    public double getX(){
        return _x;
    }

    public void setX(double x){
        _x = x;
    }

    public double getY(){
        return _y;
    }

    public void setY(double y){
        _y = y;
    }




}
