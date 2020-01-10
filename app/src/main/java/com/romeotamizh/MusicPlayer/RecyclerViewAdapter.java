package com.romeotamizh.MusicPlayer;

import android.content.Context;
import android.util.Log;
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
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isSongChanged;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //private ArrayList<String> mImages
    private ArrayList<String> mTitleList;
    private ArrayList<Integer> mDurationList;
    private ArrayList<Integer> mIdList = new ArrayList<Integer>();
    private ArrayList<String> mDataList = new ArrayList<String>();
    private LayoutInflater mInflater;
    private com.romeotamizh.MusicPlayer.Helpers.Context.MEDIATYPE mediaType;

    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdList, Context context, com.romeotamizh.MusicPlayer.Helpers.Context.MEDIATYPE mediaType) {
        //this.mImages = mImages;
        mInflater = LayoutInflater.from(context);
        this.mTitleList = mTitleList;
        this.mDurationList = mDurationList;
        this.mDataList = mDataList;
        this.mContext = context;
        this.mIdList = mIdList;
        this.mediaType = mediaType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RecyclerViewAdapter recyclerViewAdapter = this;
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
                isSongChanged = true;
                isFirstTime = false;
                if (recyclerViewAdapter.mediaType == com.romeotamizh.MusicPlayer.Helpers.Context.MEDIATYPE.AUDIO)
                    PlayMusic.callBack(position);
                else
                    Log.d("hahahaah", "ajjaajja");

            }
        });


    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
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
