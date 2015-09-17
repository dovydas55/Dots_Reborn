package com.example.dovydas.dots_reborn;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Dovydas on 9/10/15.
 * This class is loosely based on Yngvi Bjornsson's implementatin of 'InClassDrawingDemo'
 * Source: https://github.com/yngvib/InClassDrawingDemo/blob/master/app/src/main/java/com/example/yngvi/inclassdrawingdemo/BoardView.java
 */
public class BoardView extends View {

    private int _cellWidth;
    private int _cellHeight;

    private ArrayList<Point> _pointSet;
    private ArrayList<Point> _adjacentPoints;
    private boolean _explosion = false;
    private boolean _explodeAdjacent = false;

    private HashMap<Integer, String> _colorMap;
    private boolean _isMoving = false;
    private boolean _isMatch = false;
    private Point _selectedPoint;
    private GestureDetector gestureDetector;

    /* member variables for displaying the path */
    private Path _path = new Path();
    private Paint _paintPath;
    private ArrayList<Point> _cellPath = new ArrayList<>();
    private Random _rand;

    /* ************************** */
    /* for drawing grid on the canvas only for debugging */
    private Rect _rect = new Rect();
    private Paint _paint = new Paint();
    /* ****************************** */

    private GeneralEventHandler _eventHandler = null;
    private int NUM_CELLS = 6; /* default board size */
    private int NUM_COLORS = 5;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /* grid style parameters */
        _paint.setColor(Color.YELLOW);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(2);
        _paint.setAntiAlias(true);
        /* ********************* */

        _adjacentPoints = new ArrayList<>();

        _colorMap = new HashMap<>();
        _selectedPoint = null;
        _rand = new Random();

        initializeColorMap();
        gestureDetector = new GestureDetector(context, new GestureListener());

    }


    /* this function can be used for shuffeling the points */
    public void shuffleBoard(){
        initializePoints();
        invalidate();
        if(_eventHandler != null){
            _eventHandler.controlSpecialOps();
        }
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

        initializePoints(); /* think about is there a better place where i could place it? */

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

        if ( !_cellPath.isEmpty() ) {
            _path.reset();
            Point point = _cellPath.get(0);
            _path.moveTo( colToX(point.getCol()) + _cellWidth / 2, rowToY(point.getRow()) + _cellHeight/2 );
            for ( int i = 1; i < _cellPath.size(); ++i ) {
                point = _cellPath.get(i);
                _path.lineTo( colToX(point.getCol()) + _cellWidth / 2, rowToY(point.getRow()) + _cellHeight/2 );
            }
            canvas.drawPath(_path, _paintPath);
        }


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
                    if(!_explosion && !_explodeAdjacent) {
                        _isMoving = true;
                        _pointSet.get(i).setMarked(true);
                        _selectedPoint = _pointSet.get(i);
                        _adjacentPoints = findAdjacentPoints();

                        _paintPath = createCustomPathPaint(_selectedPoint);
                        _cellPath.add(new Point(xToCol(x), yToRow(y)));
                    } /*else if(_explosion){
                        for(int j = 0; j < _pointSet.size(); j++){
                            if(_pointSet.get(j).getColor() == _pointSet.get(i).getColor() ){
                                _pointSet.get(j).setMarked(true);
                            }
                        }
                        _explosion = false;
                        _isMatch = true;

                    }*/

                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(_isMoving){
                for(int i = 0; i < _adjacentPoints.size(); i++){
                    if(_adjacentPoints.get(i).getCircle().contains(x,y) && _adjacentPoints.get(i).getColor() == _selectedPoint.getColor() && !_adjacentPoints.get(i).getMarked()){
                        /* match */
                        _isMatch = true;
                        _adjacentPoints.get(i).setMarked(true);
                        _pointSet.get(find(_adjacentPoints.get(i))).setMarked(true);
                        _selectedPoint = _adjacentPoints.get(i);
                        _adjacentPoints = findAdjacentPoints(); /* find new adjacent points */

                        /* adding point to the path */
                        if ( !_cellPath.isEmpty( ) ) {
                            int col = xToCol(x);
                            int row = yToRow(y);
                            Point last = _cellPath.get(_cellPath.size() - 1);
                            if (col != last.getCol() || row != last.getRow()) {
                                _cellPath.add(new Point(col, row));
                            }
                        }
                        invalidate();
                    }
                }

            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            _isMoving = false;
            _adjacentPoints.clear();
            _cellPath.clear();

            if(_isMatch){
                /* remove all marked points */
                for(int i = 0; i < _pointSet.size(); i++){
                    if(_pointSet.get(i).getMarked()){
                        int icr = checkHowManyRemoved(_pointSet.get(i).getCol(), _pointSet.get(i).getRow());

                        _pointSet.get(i).setMarked(false);
                        _pointSet.get(i).setRow(_pointSet.get(i).getRow() - icr + 1);

                        int c = _rand.nextInt(NUM_COLORS);
                        _pointSet.get(i).setCol(c);
                        _pointSet.get(i).setPaint(createPaintBrush(c));
                        animateMovement(colToX(_pointSet.get(i).getCol()), -1500, rowToY(_pointSet.get(i).getRow()), i);

                        if(_eventHandler != null){
                            _eventHandler.onUpdateScore();
                        }
                    }
                }
                if(_eventHandler != null){
                    _eventHandler.onUpdateMove(); /* a move has been made by user */
                }

            } else {
                for(int i = 0; i < _pointSet.size(); i++){
                    _pointSet.get(i).setMarked(false);
                }
            }

            _paintPath = null;
            _isMatch = false;
            invalidate();
        }

        return gestureDetector.onTouchEvent(event);
    }


    public void setGeneralHandler(GeneralEventHandler geh){
        _eventHandler = geh;
    }

    public void setExplosion(){
        _explosion = true;
    }

    public void setExplodeAdjancent(){
        _explodeAdjacent = true;
    }

    /***************************************************************************************/
    /********************************* PRIVATE METHODS *************************************/
    /***************************************************************************************/

    private int find(Point p){
        for(int i = 0; i < _pointSet.size(); i++){
            if(_pointSet.get(i).getRow() == p.getRow() && _pointSet.get(i).getCol() == p.getCol()){
                return i;
            }
        }
        return -1; /* not found */
    }

    private int checkHowManyRemoved(int col, int row){
        int counter = 0;
        for(Point p : _pointSet){
            if(p.getCol() == col && p.getRow() <= row && p.getMarked()){
                counter++;
            }
        }

        return counter;
    }

    private ArrayList<Point> findAdjacentPoints(){
        ArrayList<Point> arr = new ArrayList<>();
        for(Point p : _pointSet){
            if (adjacentPoint(p) && !p.getMarked()){
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
        for(int i = 0; i < NUM_CELLS; i++){
            for(int j = 0; j < NUM_CELLS; j++){
                int color = _rand.nextInt(NUM_COLORS); /* General formula rand.nextInt((max - min) + 1) + min;*/
                _pointSet.add(new Point(j, i, color, createPaintBrush(color), createCircle(j, i), false));
            }
        }

        for(int i = _pointSet.size() - 1; i >= 0; i--){
            animateMovement(colToX(_pointSet.get(i).getCol()), -1500, rowToY(_pointSet.get(i).getRow()), i);
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
        circle.inset(_cellWidth * 0.25f, _cellHeight * 0.25f);

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
        _colorMap.put(0, "#BA60BE");
        _colorMap.put(1, "#668CD8");
        _colorMap.put(2, "#57CEBF");
        _colorMap.put(3, "#6AC65F");
        _colorMap.put(4, "#E9D439");
        _colorMap.put(5, "#D86660");

    }

    /* custom paint for path */
    private Paint createCustomPathPaint(Point p){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(_colorMap.get(p.getColor())));
        paint.setStrokeWidth(15.0f);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    private void animateMovement(final int xT, final float yFrom, final float yTo, final int i) {
        _pointSet.get(i).getAnimator().removeAllUpdateListeners();
        _pointSet.get(i).getAnimator().setDuration(1500);
        _pointSet.get(i).getAnimator().setFloatValues(0.0f, 1.0f);
        _pointSet.get(i).getAnimator().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                //int x = (int) ((1.0 - ratio) * xT + ratio * xT);
                int y = (int) ((1.0 - ratio) * yFrom + ratio * yTo);
                _pointSet.get(i).getCircle().offsetTo(xT + _cellWidth / 2 - _pointSet.get(i).getCircle().height() / 2, y + _cellHeight / 2 - _pointSet.get(i).getCircle().width() / 2);

                invalidate();
            }
        });
        _pointSet.get(i).getAnimator().start();

    }

    /***************************************************************************************/
    /********************** Inner class for detecting double taps **************************/
    /***************************************************************************************/

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            for(int i = 0; i < _pointSet.size(); i++){
                if(_pointSet.get(i).getCircle().contains(x, y)){
                    if(_explosion){
                        for(int j = 0; j < _pointSet.size(); j++){
                            if(_pointSet.get(j).getColor() == _pointSet.get(i).getColor() ){
                                _pointSet.get(j).setMarked(true);
                            }
                        }
                        _explosion = false;
                        _isMatch = true;
                        if(_eventHandler != null){
                            _eventHandler.controlSpecialOps();
                        }

                    } else if(_explodeAdjacent){
                        _selectedPoint = _pointSet.get(i);
                        _adjacentPoints = findAdjacentPoints();
                        for(int j = 0; j < _adjacentPoints.size(); j++){
                            _adjacentPoints.get(j).setMarked(true);
                            _selectedPoint.setMarked(true);
                        }
                        _explodeAdjacent = false;
                        _isMatch = true;
                        if(_eventHandler != null){
                            _eventHandler.controlSpecialOps();
                        }
                    }
                }
            }
            return true;
        }
    }

}
