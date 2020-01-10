package com.romeotamizh.MusicPlayer.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.romeotamizh.MusicPlayer.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.seekBarMax;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.seekBarWidth;

@SuppressLint("AppCompatCustomView")
public class SeekBarWithFavourites extends SeekBar {
    public static int[] mFavouritesPositionsList = null;
    private Bitmap mFavouriteBitmap = null;

    private static OnSeekBarProgressListener listener;

    public SeekBarWithFavourites(Context context) {
        super(context);
        init(null);

    }

    public SeekBarWithFavourites(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeekBarWithFavourites(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(final AttributeSet attributeSet) {
        final TypedArray attributesArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SeekbarWithFavourites, 0, 0);
        final int dotsArrayResource = attributesArray.getResourceId(R.styleable.SeekbarWithFavourites_dots_Position, 0);
        if (dotsArrayResource != 0) {
            mFavouritesPositionsList = getResources().getIntArray(dotsArrayResource);

        }
        final int dotDrawableId = attributesArray.getResourceId(R.styleable.SeekbarWithFavourites_dots_drawable, 0);

        if (dotDrawableId != 0) {
            mFavouriteBitmap = BitmapFactory.decodeResource(getResources(), dotDrawableId);

        }

    }

    public SeekBarWithFavourites(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public static void setSeekBarProgress(OnSeekBarProgressListener listener) {
        SeekBarWithFavourites.listener = listener;

    }

    public static void callBack(int progress) {
        if (listener != null)
            listener.onSeekBarProgressChange(progress);
    }

    public void setFavouritesPositionsList(final int[] favouritesPositionsList) {
        mFavouritesPositionsList = favouritesPositionsList;
        invalidate();
    }

    public void setFavouriteBitmap(final int mFavouriteBitmapResource) {
        mFavouriteBitmap = BitmapFactory.decodeResource(getResources(), mFavouriteBitmapResource).createScaledBitmap(mFavouriteBitmap, 18, 18, false);
        invalidate();
    }


    public interface OnSeekBarProgressListener {
        void onSeekBarProgressChange(int progress);
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final BigDecimal max = BigDecimal.valueOf(seekBarMax);

        BigDecimal step = BigDecimal.valueOf(0);
        if (max.intValue() != 0)
            step = BigDecimal.valueOf(seekBarWidth).divide(max, 10, RoundingMode.CEILING);
        //Log.d("pos", Arrays.toString(mFavouritesPositionsList));

        if (null != mFavouritesPositionsList && 0 != mFavouritesPositionsList.length && null != mFavouriteBitmap) {
            // draw dots if we have ones
            for (int position : mFavouritesPositionsList) {



                int x = (step.multiply(BigDecimal.valueOf(position)).intValue());
                if (position > 0)
                    canvas.drawBitmap(mFavouriteBitmap, x, 0, null);
            }
        }
    }


}
