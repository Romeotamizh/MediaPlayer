package com.romeotamizh.MediaPlayer.Adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;

import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;

public class lastPlayedViewPagerAdapter extends PagerAdapter {

    ImageView dotsPreviewImage;
    TextView dotsPreviewText;
    private int[] idList = new int[5];
    private Uri[] uriList = new Uri[5];
    private Context.MEDIATYPE[] mediaTypeList = new Context.MEDIATYPE[5];
    private Bitmap[] previews = new Bitmap[5];
    private CharSequence[] titleList = new CharSequence[5];

    public lastPlayedViewPagerAdapter() {


    }

    public Bitmap[] getPreviews() {
        return previews;
    }

    public void setPreviews(Bitmap[] previews) {
        this.previews = previews;
    }

    public int[] getIdList() {
        return idList;
    }

    public void setIdList(int[] idList) {
        this.idList = idList;
    }

    public Uri[] getUriList() {
        return uriList;
    }

    public void setUriList(Uri[] uriList) {
        this.uriList = uriList;
    }

    public Context.MEDIATYPE[] getMediaTypeList() {
        return mediaTypeList;
    }

    public void setMediaTypeList(Context.MEDIATYPE[] mediaTypeList) {
        this.mediaTypeList = mediaTypeList;
    }

    public CharSequence[] getTitleList() {
        return titleList;
    }

    public void setTitleList(CharSequence[] titleList) {
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return idList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = ((LayoutInflater) (getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.dots_preview, container, false);
        dotsPreviewImage = view.findViewById(R.id.dots_preview_image);
        dotsPreviewText = view.findViewById(R.id.dots_preview_text);
        dotsPreviewText.setText(titleList[position]);
        dotsPreviewImage.setImageBitmap(previews[position]);

        dotsPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayMedia.callBack(idList[position], mediaTypeList[position]);

            }
        });
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }


}
