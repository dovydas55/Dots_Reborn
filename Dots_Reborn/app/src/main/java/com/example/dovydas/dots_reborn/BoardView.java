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
    private ArrayList<Point> _matchedPoints;

    private HashMap<Integer, String> _colorMap;
    private boolean _isMoving;
    private Point _selectedPoint;
    private Point _firstContact;
    private boolean _isMatch = false;


    private Rect _rect = new Rect();
    private Paint _paint = new Paint();

    private int NUM_CELLS = 6; /* default board size */


    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        _paint.setColor(Color.RED);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(2);
        _paint.setAntiAlias(true);

        _pointSet = new ArrayList<>();
        _matchedPoints = new ArrayList<>();

        _colorMap = new HashMap<>();
        _isMoving = false;
        _selectedPoint = null;
        _firstContact = null;
        _isMatch = false;

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
                    _selectedPoint = p;
                    _firstContact = p;
                    _matchedPoints.add(p); /* adding very first point of the sequence */

                    Log.d("PlayGameActivity", "*********************************************************");
                    Log.d("PlayGameActivity", "COLUMN  " + Integer.toString(_selectedPoint.getCol()));
                    Log.d("PlayGameActivity", "ROW  " + Integer.toString(_selectedPoint.getRow()));
                    Log.d("PlayGameActivity", "COLOR  " + Integer.toString(_selectedPoint.getColor()));
                    Log.d("PlayGameActivity", "*********************************************************");

                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(_isMoving){
                    for(int i = 0; i < _pointSet.size(); i++) {
                        if (_pointSet.get(i).getColor() == _selectedPoint.getColor() && adjacentPoint(_pointSet.get(i))) {

                            _selectedPoint = _pointSet.get(i);
                            _matchedPoints.add(_pointSet.get(i)); /* still need to add first point */
                            _isMatch = true;
                            _pointSet.remove(i);

                            //Log.d("PlayGameActivity", "*************************** MATCH ******************************");
                            //Log.d("PlayGameActivity", "COLUMN (prev - cur)  " + Integer.toString(_firstPointInSequence.getCol()) + "  <-->  " + Integer.toString(_selectedPoint.getCol()));
                            //Log.d("PlayGameActivity", "ROW (prev - cur)  " + Integer.toString(_firstPointInSequence.getRow()) + "  <-->  " + Integer.toString(_selectedPoint.getRow()));
                            //Log.d("PlayGameActivity", "COLOR (prev - cur)  " + Integer.toString(_prevPoint.getColor()) + "  <-->  " + Integer.toString(_selectedPoint.getColor()));
                            //Log.d("PlayGameActivity", "****************************************************************");

                        }
                    }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            _selectedPoint = null;
            _isMoving = false;
            if(_isMatch){
                for(int i = 0; i < _pointSet.size(); i++){
                    if(_firstContact.getRow() == _pointSet.get(i).getRow() && _firstContact.getCol() == _pointSet.get(i).getCol()){
                        _pointSet.remove(i);
                    }
                }
                _isMatch = false;
            }

            invalidate(); /* delete matched points */
            /********************/
            _matchedPoints.clear(); /* for now just delete all matched points */
            /********************/
        }

        return true;
    }



    /***************************************************************************************/
    /********************************* PRIVATE METHODS *************************************/
    /***************************************************************************************/

    private boolean adjacentPoint(Point p){
        if(_selectedPoint.getRow() == p.getRow()){
            if(_selectedPoint.getCol() + 1 == p.getCol()) {
                return true;
            }
            else if(_selectedPoint.getCol() - 1 == p.getCol()) {
                return true;
            }
        } else if(_selectedPoint.getCol() == p.getCol()){
            if(_selectedPoint.getRow() + 1 == p.getRow()) {
                return true;
            }
            else if(_selectedPoint.getRow() - 1 == p.getRow()) {
                return true;
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
                _pointSet.add(new Point(i, j, color, createPaintBrush(color), createCircle(i, j)));
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
