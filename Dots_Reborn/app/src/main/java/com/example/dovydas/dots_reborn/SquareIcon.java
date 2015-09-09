package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by gunnhildur on 09/09/15.
 */
public class SquareIcon extends ImageView {
    public SquareIcon(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int width, int height){
        int minDimension = Math.min(width, height);
        setMeasuredDimension(minDimension, minDimension);
    }
}
