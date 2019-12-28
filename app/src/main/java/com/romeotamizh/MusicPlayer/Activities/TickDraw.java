package com.romeotamizh.MusicPlayer.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.warkiz.widget.IndicatorSeekBar;

public class TickDraw extends IndicatorSeekBar {
    public static final int g = 0;

    public TickDraw(Context context) {
        super(context);
    }

    public TickDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TickDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public  void onDraw(Canvas canvas){

    };
}
