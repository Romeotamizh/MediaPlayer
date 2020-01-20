package com.romeotamizh.MediaPlayer.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Size;

import com.romeotamizh.MediaPlayer.R;

import java.io.IOException;

import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;

public class Thumbnail {


    public static Bitmap setThumbnailImage(final Uri uri, final Size size, final CharSequence mTitle) {

        Bitmap bitmap;
        try {
            bitmap = getContext().getContentResolver().loadThumbnail(uri, size, null);
        } catch (IOException e) {
            bitmap = null;
        }
        if (bitmap != null)
            return bitmap;
        else {
            if (mTitle != null)
                return setAlphabetImage(mTitle.charAt(0), size);
            else
                return null;
        }
    }

    public static Bitmap setAlphabetImage(char x, Size size) {
        int w = size.getWidth();
        int h = size.getHeight();


        switch (Character.toLowerCase(x)) {

            case 'a':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_a), w, h, false);

            case 'b':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_b), w, h, false);


            case 'c':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_c), w, h, false);

            case 'd':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_d), w, h, false);

            case 'e':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_e), w, h, false);

            case 'f':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_f), w, h, false);


            case 'g':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_g), w, h, false);

            case 'h':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_h), w, h, false);


            case 'i':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_i), w, h, false);


            case 'j':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_j), w, h, false);

            case 'k':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_k), w, h, false);

            case 'l':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_l), w, h, false);

            case 'm':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_m), w, h, false);

            case 'n':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_n), w, h, false);

            case 'o':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_0), w, h, false);

            case 'p':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_p), w, h, false);

            case 'q':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_q), w, h, false);


            case 'r':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_r), w, h, false);


            case 's':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_s), w, h, false);


            case 't':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_t), w, h, false);


            case 'u':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_u), w, h, false);


            case 'v':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_v), w, h, false);

            case 'w':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_w), w, h, false);

            case 'x':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_x), w, h, false);

            case 'y':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_y), w, h, false);

            case 'z':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_z), w, h, false);

            default:
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_numbers), w, h, false);

        }

    }


}
