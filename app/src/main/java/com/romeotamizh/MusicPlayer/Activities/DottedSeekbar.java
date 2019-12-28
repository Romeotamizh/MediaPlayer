package com.romeotamizh.MusicPlayer.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.romeotamizh.MusicPlayer.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.seekBarMax;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.seekBarwidth;

@SuppressLint("AppCompatCustomView")
public class DottedSeekbar extends SeekBar {
    public static int[] mDotsPositions = null;
    private Bitmap mDotBitmap = null;
    private Bitmap mDotBitmapSmall;

    public DottedSeekbar(Context context) {
        super(context);
        init(null);

    }

    public DottedSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DottedSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DottedSeekbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(final AttributeSet attributeSet) {
        final TypedArray attributesArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.DottedSeekbar, 0, 0);
        final int dotsArrayResource = attributesArray.getResourceId(R.styleable.DottedSeekbar_dots_Position, 0);
        if (dotsArrayResource != 0) {
            mDotsPositions = getResources().getIntArray(dotsArrayResource);

        }
        final int dotDrawableId = attributesArray.getResourceId(R.styleable.DottedSeekbar_dots_drawable, 0);

        if (dotDrawableId != 0) {
            mDotBitmap = BitmapFactory.decodeResource(getResources(), dotDrawableId);

        }

    }


    public void setDots(final int[] dots) {
        mDotsPositions = dots;
        invalidate();
    }

    /**
     * @param dotsResource resource id to be used for dots drawing
     */
    public void setDotsDrawable(final int dotsResource) {
        mDotBitmap = BitmapFactory.decodeResource(getResources(), dotsResource).createScaledBitmap(mDotBitmap, 18, 18, false);
        //mDotBitmapSmall = Bitmap.createScaledBitmap(mDotBitmap, 18, 18, false);
        invalidate();
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);


        final BigDecimal step = BigDecimal.valueOf(seekBarwidth).divide(BigDecimal.valueOf(seekBarMax), 10, RoundingMode.CEILING);
        Log.d("pos", Arrays.toString(mDotsPositions));


        if (null != mDotsPositions && 0 != mDotsPositions.length && null != mDotBitmap) {
            // draw dots if we have ones
            for (int position : mDotsPositions) {
                Log.d("pos", String.valueOf(position));
                Log.d("step", String.valueOf(step));
                Log.d("width", String.valueOf(seekBarwidth));
                int x = (step.multiply(BigDecimal.valueOf((double) position))).intValue();
                canvas.drawBitmap(mDotBitmap, x + 9, 10, null);
            }
        }
    }


}
