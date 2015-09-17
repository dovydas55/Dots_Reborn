package com.example.dovydas.dots_reborn;

import android.animation.ValueAnimator;
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
    private ValueAnimator _animator;


    public Point(int row, int col, int color, Paint paint, RectF circle, boolean match){
        this._row = row;
        this._col = col;
        this._color = color;
        this._circle = circle;
        this._paint = paint;
        this._marked = match;
        this._animator = new ValueAnimator();
    }

    /* Overloaded constructor */
    public Point(int col, int row){
        this._col = col;
        this._row = row;
    }

    public ValueAnimator getAnimator(){
        return _animator;
    }

    public RectF getCircle(){
        return _circle;
    }

    public Paint getPaint(){
        return _paint;
    }

    public void setPaint(Paint p) {
        _paint = p;
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
        _color = col;
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
