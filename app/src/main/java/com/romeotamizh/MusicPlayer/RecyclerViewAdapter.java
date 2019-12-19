package com.romeotamizh.MusicPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.romeotamizh.MusicPlayer.PlayScreenActivity.playMusic;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<String> mImages = new ArrayList<>();
    ArrayList<String> mTitles = new ArrayList<>();
    ArrayList<Integer> mDurations = new ArrayList<>();
    ArrayList<String> mDatas = new ArrayList<>();
    LayoutInflater mInflater;

    Context mContext;

    MediaPlayer mediaPlayer = new MediaPlayer();

    public RecyclerViewAdapter(ArrayList<String> mTitles, ArrayList<Integer> mDurations, ArrayList<String> mDatas, Context context) {
        //this.mImages = mImages;
        mInflater = LayoutInflater.from(context);
        this.mTitles = mTitles;
        this.mDurations = mDurations;
        this.mDatas = mDatas;
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
        final StringBuffer titleOnly = new StringBuffer();
        StringBuffer extension =  new StringBuffer();
        titleOnly.append(mTitles.get(position));
        for(int i = titleOnly.length()-1;i>=0;i--){
            extension.append(titleOnly.charAt(i));
            if(titleOnly.charAt(i)=='.') {
                titleOnly.delete(i, titleOnly.length());
                break;
            }

        }
        extension.reverse();
        extension.deleteCharAt(0);
        holder.title.setText(titleOnly.toString());
        holder.extension.setText(extension.toString());
        //holder.title.setText(mTitles.get(position));
        Integer m = (mDurations.get(position) / 1000) / 60;
        Integer s = (mDurations.get(position) / 1000) % 60;
        if (m == 0 && s == 0)
            holder.duration.setText("00:01");
        else {
            if (m < 10 && s < 10)
                holder.duration.setText("0" + m + ":" + "0" + s);

            else if (m < 10 && s >= 10)
                holder.duration.setText("0" + m + ":" + s);
            else if (m >= 10 && s < 10)
                holder.duration.setText(m + ":" + "0" + s);
            else
                holder.duration.setText(m + ":" + s);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // Toast.makeText(mContext, "Playing " + mTitles.get(position), Toast.LENGTH_SHORT).show();
                playMusic(mDatas.get(position), titleOnly.toString());


            }
        });


    }

    /*public  void openPlayScreen(){
        Intent intent = new Intent(MainActivity.context,PlayScreenActivity.class);
        MainActivity.context.startActivity(intent);


    }*/
    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        //mTitles.get(position)

        super.onViewDetachedFromWindow(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        TextView duration;
        TextView extension;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            extension = itemView.findViewById(R.id.extension);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }


}
