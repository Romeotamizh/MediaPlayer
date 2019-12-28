package com.romeotamizh.MusicPlayer.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.romeotamizh.MusicPlayer.R;

@SuppressLint("AppCompatCustomView")
public class DottedSeekbar extends SeekBar {
    private int[] mDotsPositions = null;
    private Bitmap mDotBitmap = null;

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
        mDotBitmap = BitmapFactory.decodeResource(getResources(), dotsResource);
        invalidate();
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        final int step = width / getMax();

        if (null != mDotsPositions && 0 != mDotsPositions.length && null != mDotBitmap) {
            // draw dots if we have ones
            for (int position : mDotsPositions) {
                canvas.drawBitmap(Bitmap.createScaledBitmap(mDotBitmap, 12, 12, false), position * step, 0, null);
            }
        }
    }


}
