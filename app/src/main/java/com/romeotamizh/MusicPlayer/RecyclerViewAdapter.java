package com.romeotamizh.MusicPlayer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isFirstTime;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isFromMainActivity;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.openPlayScreen;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isBackPressed;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isSongChanged;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //ArrayList<String> mImages = new ArrayList<>();
    ArrayList<String> mTitleList = new ArrayList<String>();
    ArrayList<Integer> mDurationList = new ArrayList<Integer>();
    ArrayList<Integer> mIdList = new ArrayList<Integer>();
    ArrayList<String> mDataList = new ArrayList<String>();
    LayoutInflater mInflater;
    Cursor cursor;

    Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdList, Context context) {
        //this.mImages = mImages;
        mInflater = LayoutInflater.from(context);
        this.mTitleList = mTitleList;
        this.mDurationList = mDurationList;
        this.mDataList = mDataList;
        this.mContext = context;
        this.mIdList = mIdList;
    }

    public RecyclerViewAdapter(Cursor cursor, Context context) {
        mInflater = LayoutInflater.from(context);
        this.cursor = cursor;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mTitleList.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                this.mDurationList.add(cursor.getInt(cursor.getColumnIndex("duration")));
                this.mDataList.add(cursor.getString(cursor.getColumnIndex("_data")));
                this.mIdList.add(cursor.getInt(cursor.getColumnIndex("_id")));
            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final StringBuffer titleOnly = new StringBuffer();
        StringBuffer extension = new StringBuffer();
        titleOnly.append(mTitleList.get(position));
        for (int i = titleOnly.length() - 1; i >= 0; i--) {
            extension.append(titleOnly.charAt(i));
            if (titleOnly.charAt(i) == '.') {
                titleOnly.delete(i, titleOnly.length());
                break;
            }

        }
        extension.reverse();
        extension.deleteCharAt(0);
        holder.title.setText(titleOnly);
        holder.extension.setText(extension);
        holder.duration.setText(TimeFormat.formatTime(mDurationList.get(position)));
        holder.image.setImageResource(setAlphabetImages(mTitleList.get(position)));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                resetFavouritesOperation("music");
                isSongChanged = true;
                isFromMainActivity = false;
                isBackPressed = false;
                isFirstTime = false;

                openPlayScreen(mDataList.get(position), titleOnly.toString(), mIdList.get(position));


            }
        });


    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        //mTitleList.get(position)

        super.onViewDetachedFromWindow(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        TextView duration;
        TextView extension;
        CardView parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            extension = itemView.findViewById(R.id.extension);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            image = itemView.findViewById(R.id.image);
        }

    }


}
