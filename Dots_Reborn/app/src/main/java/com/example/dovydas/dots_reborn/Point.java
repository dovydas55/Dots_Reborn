package com.example.dovydas.dots_reborn;

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


    public Point(int x, int y, int color){
        _x = x;
        _y = y;
        _color = color;
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
