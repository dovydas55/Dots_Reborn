package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Dovydas on 9/10/15.
 */
public class BoardView extends View {

    private int _cellWidth;
    private int _cellHeight;
    private ArrayList<Point> _pointSet;
    private ArrayList<Point> _adjacentPoints;

    private HashMap<Integer, String> _colorMap;
    private boolean _isMoving;
    private Point _selectedPoint;

    /* for drawing grid on the canvas */
    private Rect _rect = new Rect();
    private Paint _paint = new Paint();
    /* ****************************** */

    private int NUM_CELLS = 6; /* default board size */


    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /* grid style parameters */
        _paint.setColor(Color.RED);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(2);
        _paint.setAntiAlias(true);
        /* ********************* */


        _pointSet = new ArrayList<>();
        _adjacentPoints = new ArrayList<>();

        _colorMap = new HashMap<>();
        _isMoving = false;
        _selectedPoint = null;

        initializeColorMap();
    }

    /* this function can be used for shuffeling the points */
    public void schuffleBoard(){
        initializePoints();
        invalidate();
    }


    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int   boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int   boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        _cellWidth = boardWidth / NUM_CELLS;
        _cellHeight = boardHeight / NUM_CELLS;

        initializePoints(); /* WHERE IS THE BEST PLACE TO CALL THIS? */

    }

    @Override
    protected void onDraw(Canvas canvas){
        /* grid is only used while in development for debugging */
        canvas.drawRect(_rect, _paint);
        for ( int row = 0; row < NUM_CELLS; ++row ) {
            for ( int col = 0; col < NUM_CELLS; ++col ) {
                int x = col * _cellWidth;
                int y = row * _cellHeight;
                _rect.set( x, y, x + _cellWidth, y + _cellHeight );
                _rect.offset( getPaddingLeft(), getPaddingTop() );
                canvas.drawRect( _rect, _paint );
            }
        }
        /* ********************************************************* */


        /* draw points on canvas */
        for(Point point : _pointSet){
            canvas.drawOval(point.getCircle(), point.getPaint()); /* draw all points */
        }

    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            for(Point p : _pointSet){
                if(p.getCircle().contains(x, y)){
                    /* USER HAS CLICKED ON THIS CIRCLE */
                    _isMoving = true;
                    p.setMarked(true);
                    _selectedPoint = p;
                    _adjacentPoints = findAdjacentPoints();


                    Log.d("PlayGameActivity", "*********************************************************");
                    Log.d("PlayGameActivity", "COLUMN , ROW (x, y) " + Integer.toString(_selectedPoint.getCol()) + "  " + Integer.toString(_selectedPoint.getRow()) );
                    Log.d("PlayGameActivity", "-- Printing out adjacent points --");
                    Log.d("PlayGameActivity", "NUMBER OF ADJACENT POINTS: " + Integer.toString(_adjacentPoints.size()));
                    for(Point i : _adjacentPoints){
                        Log.d("PlayGameActivity", "COLUMN , ROW (x, y) " + Integer.toString(i.getCol()) + "  " + Integer.toString(i.getRow()) );
                    }
                    Log.d("PlayGameActivity", "*********************************************************");

                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(_isMoving){
                    /* DO something */
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            _isMoving = false;
            _adjacentPoints.clear();
            Log.d("PlayGameActivity", "Adjacent point array have been cleared: " + Integer.toString(_adjacentPoints.size()));
            Log.d("PlayGameActivity", "*********************************************************");

        }

        return true;
    }



    /***************************************************************************************/
    /********************************* PRIVATE METHODS *************************************/
    /***************************************************************************************/

    private ArrayList<Point> findAdjacentPoints(){
        ArrayList<Point> arr = new ArrayList<>();
        for(Point p : _pointSet){
            if(adjacentPoint(p) && !p.getMarked()){
                arr.add(p);
            }
        }
        return arr;
    }

    private boolean adjacentPoint(Point p){
        if(_selectedPoint.getRow() == p.getRow()){
            if(_selectedPoint.getCol() + 1 == p.getCol()) {
                return true;
            }
            else if(_selectedPoint.getCol() - 1 == p.getCol()) {
                return true;
            } else {
                return false;
            }
        } else if(_selectedPoint.getCol() == p.getCol()){
            if(_selectedPoint.getRow() + 1 == p.getRow()) {
                return true;
            }
            else if(_selectedPoint.getRow() - 1 == p.getRow()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void initializePoints(){
        /* initially color set is 0 - 5 */
        Random rand = new Random();
        for(int i = 0; i < NUM_CELLS; i++){
            for(int j = 0; j < NUM_CELLS; j++){
                int color = rand.nextInt(6); /* General formula rand.nextInt((max - min) + 1) + min;*/
                _pointSet.add(new Point(j, i, color, createPaintBrush(color), createCircle(j, i), false));
            }
        }
    }

    private Paint createPaintBrush(int color){
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor(_colorMap.get(color)));
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setAntiAlias(true);
        return circlePaint;
    }

    private RectF createCircle(int row, int col){
        RectF circle = new RectF();
        circle.set(0, 0, _cellWidth, _cellHeight);
        circle.inset(_cellWidth * 0.2f, _cellHeight * 0.2f);

        circle.offset(colToX(col), rowToY(row));
        return circle;
    }

    private int xToCol( int x ) {
        return (x - getPaddingLeft()) / _cellWidth;
    }
    private int yToRow( int y ) {
        return (y - getPaddingTop()) / _cellHeight;
    }
    private int colToX( int col ) {
        return  col * _cellWidth + getPaddingLeft();
    }
    private int rowToY( int row ) {
        return  row * _cellHeight + getPaddingTop();
    }

    private void initializeColorMap(){
        _colorMap.put(0, "#FF0000");
        _colorMap.put(1, "#00FF00");
        _colorMap.put(2, "#0000FF");
        _colorMap.put(3, "#FF00FF");
        _colorMap.put(4, "#993333");
        _colorMap.put(5, "#006666");

    }

    /***************************************************************************************/

}
