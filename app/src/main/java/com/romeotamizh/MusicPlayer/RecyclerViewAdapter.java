package com.romeotamizh.MusicPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<String> mImages = new ArrayList<>();
    ArrayList<String> mTitles = new ArrayList<>();
    ArrayList<Integer> mDurations = new ArrayList<>();
    ArrayList<String> mData = new ArrayList<>();
    LayoutInflater mInflater;

    Context mContext;

    MediaPlayer mediaPlayer = new MediaPlayer();

    public RecyclerViewAdapter(/*ArrayList<String> mImages ,*/ArrayList<String> mTitles, ArrayList<Integer> mDurations, ArrayList<String> mData, Context context) {
        //this.mImages = mImages;
        mInflater = LayoutInflater.from(context);
        this.mTitles = mTitles;
        this.mDurations = mDurations;
        this.mData = mData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(mTitles.get(position));
        Integer m = (mDurations.get(position) / 1000) / 60;
        Integer s = (mDurations.get(position) / 1000) % 60;
        if (s < 10)
            holder.duration.setText(m.toString() + ":" + "0" + s.toString());
        else
            holder.duration.setText(m.toString() + ":" + s.toString());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked " + mTitles.get(position), Toast.LENGTH_SHORT).show();
                MainActivity.playMusic(mData.get(position));


            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        TextView duration;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}
