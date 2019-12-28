package com.romeotamizh.MusicPlayer.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.warkiz.widget.IndicatorSeekBar;

public class MySeekBar extends IndicatorSeekBar {
    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas){
        /*drawTrack(canvas);
        drawTickMarks(canvas);
        drawTickTexts(canvas);
        drawThumb(canvas);
        drawThumbText(canvas);*/
    }
}
