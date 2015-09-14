package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;


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
    private boolean _isMoving = false;
    private boolean _isMatch = false;
    private Point _selectedPoint;
    private int _gameScore = 0;
    private TextView _displayScore;
    private TextView _displayTimeOrMoves;
    private String _gameMode;

    private int _secondsLeft = 0;
    private int _movesLeft;

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

        _adjacentPoints = new ArrayList<>();
        _colorMap = new HashMap<>();
        _selectedPoint = null;

        initializeColorMap();


    }

    /* this function can be used for shuffeling the points */
    public void shuffleBoard(){
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
            for(int i = 0; i < _pointSet.size(); i++){
                if(_pointSet.get(i).getCircle().contains(x, y)){
                    /* USER HAS CLICKED ON THIS CIRCLE */
                    _isMoving = true;
                    _pointSet.get(i).setMarked(true);
                    _selectedPoint = _pointSet.get(i);
                    _adjacentPoints = findAdjacentPoints();

                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(_isMoving){
                for(int i = 0; i < _adjacentPoints.size(); i++){
                    if(_adjacentPoints.get(i).getCircle().contains(x,y) && _adjacentPoints.get(i).getColor() == _selectedPoint.getColor() && !_adjacentPoints.get(i).getMarked()){
                        /* match */
                        _isMatch = true;
                        _adjacentPoints.get(i).setMarked(true);
                        _selectedPoint = _adjacentPoints.get(i);
                        _adjacentPoints = findAdjacentPoints(); /* find new adjacent points */
                    }
                }

            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            _isMoving = false;
            _adjacentPoints.clear();
            if(_isMatch){
                /* remove all marked points */
                for(int i = 0; i < _pointSet.size(); i++){
                    if(_pointSet.get(i).getMarked() == true){
                        _pointSet.remove(i);
                        i--;
                        updateScore();
                    }
                }
                if(_gameMode.equals("Move mode")){
                    decrementMoveCounter();
                }
            } else {
                for(int i = 0; i < _pointSet.size(); i++){
                    _pointSet.get(i).setMarked(false);
                }
            }

            _isMatch = false;
            invalidate();
            
        }

        return true;
    }


    /* init score */
    public void setScoreView(TextView v){
        _displayScore = v;
        _gameScore = -1;
        updateScore();
    }

    public void setMovesOrTime(TextView v, String mode){
        _displayTimeOrMoves = v;
        _gameMode = mode;

        if(_gameMode.equals("Move mode")){
            _movesLeft = 31;
            decrementMoveCounter();
        } else if(_gameMode.equals("Time mode")){
            startTimeCounter();
        }
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
        _pointSet = new ArrayList<>();
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

    private void decrementMoveCounter(){
        _movesLeft -= 1;
        _displayTimeOrMoves.setText("Moves " + Integer.toString(_movesLeft));
    }

    /* initialize and start time countdown */
    private void startTimeCounter(){
        new CountDownTimer(30000, 100) {
            public void onTick(long ms) {
                if (Math.round((float)ms / 1000.0f) != _secondsLeft)
                {
                    _secondsLeft = Math.round((float)ms / 1000.0f);
                    _displayTimeOrMoves.setText("Time " + _secondsLeft );
                }
            }

            public void onFinish() {
                _displayTimeOrMoves.setText("Time 0");
            }
        }.start();

    }

    private void updateScore(){
        _gameScore += 1;
        _displayScore.setText("Score: " + Integer.toString(_gameScore));
    }

    private void resetScore(){
        _gameScore = 0;
    }

    /***************************************************************************************/

}
