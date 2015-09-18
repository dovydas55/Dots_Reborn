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
        return this._animator;
    }

    public RectF getCircle(){
        return this._circle;
    }

    public Paint getPaint(){
        return this._paint;
    }

    public void setPaint(Paint p) {
        this._paint = p;
    }

    public int getColor(){
        return this._color;
    }

    public int getRow(){
        return this._row;
    }

    public void setRow(int row){
        this._row = row;
    }

    public int getCol(){
        return this._col;
    }

    public void setColor(int col){
        this._color = col;
    }

    public boolean getMarked(){
        return this._marked;
    }

    public void setMarked(boolean marked){
        this._marked = marked;
    }



}
