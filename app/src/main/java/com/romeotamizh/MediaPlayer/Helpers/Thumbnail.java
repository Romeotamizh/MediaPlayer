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
                return setAlphabetImage(mTitle.charAt(0));
            else
                return null;
        }
    }

    public static Bitmap setAlphabetImage(char x) {


        switch (Character.toLowerCase(x)) {

            case 'a':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_a), 80, 80, false);

            case 'b':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_b), 80, 80, false);


            case 'c':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_c), 80, 80, false);

            case 'd':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_d), 80, 80, false);

            case 'e':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_e), 80, 80, false);

            case 'f':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_f), 80, 80, false);


            case 'g':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_g), 80, 80, false);

            case 'h':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_h), 80, 80, false);


            case 'i':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_i), 80, 80, false);


            case 'j':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_j), 80, 80, false);

            case 'k':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_k), 80, 80, false);

            case 'l':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_l), 80, 80, false);

            case 'm':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_m), 80, 80, false);

            case 'n':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_n), 80, 80, false);

            case 'o':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_0), 80, 80, false);

            case 'p':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_p), 80, 80, false);

            case 'q':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_q), 80, 80, false);


            case 'r':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_r), 80, 80, false);


            case 's':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_s), 80, 80, false);


            case 't':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_t), 80, 80, false);


            case 'u':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_u), 80, 80, false);


            case 'v':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_v), 80, 80, false);

            case 'w':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_w), 80, 80, false);

            case 'x':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_x), 80, 80, false);

            case 'y':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_y), 80, 80, false);

            case 'z':
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_z), 80, 80, false);

            default:
                return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.alphabets_numbers), 80, 80, false);

        }

    }


}
