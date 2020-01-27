package com.romeotamizh.MediaPlayer.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.romeotamizh.MediaPlayer.Helpers.MediaInfoDatabase;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.AudioFragment.tAudio;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.groupByAudio;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.groupByVideo;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isSongChanged;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.VideoFragment.isInFinalList;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.VideoFragment.tVideo;
import static com.romeotamizh.MediaPlayer.Helpers.Thumbnail.setThumbnailImage;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private boolean one, two, three, four, five, six, seven, eight = false;
    private ArrayList<CharSequence> titleList = new ArrayList<>();
    private ArrayList<CharSequence> durationList = new ArrayList<>();
    private ArrayList<CharSequence> extensionList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private ArrayList<Uri> uriList = new ArrayList<>();
    private ArrayList<Bitmap> thumbs = new ArrayList<>();
    private com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE mediaType;
    private LayoutInflater inflater;
    private Context context;
    private int listSize;
    private com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT contextScreen;
    private MediaInfoDatabase mediaInfoDatabase;


    public RecyclerViewAdapter(MediaInfoDatabase mediaInfoDatabase, com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT contextScreen, Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (contextScreen == com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT.MAIN) {
            this.mediaInfoDatabase = mediaInfoDatabase;
            this.idList = mediaInfoDatabase.getIdList();
            this.uriList = mediaInfoDatabase.getUriList();
            this.titleList = mediaInfoDatabase.getTitleList();
            this.extensionList = mediaInfoDatabase.getExtensionList();
            this.durationList = mediaInfoDatabase.getDurationList();

        }


        this.contextScreen = contextScreen;
        this.mediaType = mediaInfoDatabase.getMediaType();
        this.listSize = this.idList.size();
        this.thumbs = new ArrayList<>();
        if (contextScreen == com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT.MAIN)
            addThumbsInEightThreads();
        else
            for (Uri uri : uriList)
                this.thumbs.add(setThumbnailImage(uri, new Size(80, 80), titleList.get(uriList.indexOf(uri))));


    }

    public void setContextScreen(com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT contextScreen) {
        this.contextScreen = contextScreen;
    }

    public void setDurationList(ArrayList<CharSequence> durationList) {
        this.durationList = durationList;
    }

    public void setExtensionList(ArrayList<CharSequence> extensionList) {
        this.extensionList = extensionList;
    }

    public void setIdList(ArrayList<Integer> idList) {
        this.idList = idList;
    }

    public void setUriList(ArrayList<Uri> uriList) {
        this.uriList = uriList;
    }

    public void setThumbs(ArrayList<Bitmap> thumbs) {
        this.thumbs = thumbs;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void addThumbsInEightThreads() {
        final ArrayList<Uri> tempUriList = this.uriList;
        final Size size = new Size(80, 80);
        final int subListSize = listSize / 8;
        final int listStart1 = 0;
        final int listStart2 = listStart1 + subListSize;
        final int listStart3 = listStart2 + subListSize;
        final int listStart4 = listStart3 + subListSize;
        final int listStart5 = listStart4 + subListSize;
        final int listStart6 = listStart5 + subListSize;
        final int listStart7 = listStart6 + subListSize;
        final int listStart8 = listStart7 + subListSize;
        final ArrayList<Bitmap> t1 = new ArrayList<>();
        final ArrayList<Bitmap> t2 = new ArrayList<>();
        final ArrayList<Bitmap> t3 = new ArrayList<>();
        final ArrayList<Bitmap> t4 = new ArrayList<>();
        final ArrayList<Bitmap> t5 = new ArrayList<>();
        final ArrayList<Bitmap> t6 = new ArrayList<>();
        final ArrayList<Bitmap> t7 = new ArrayList<>();
        final ArrayList<Bitmap> t8 = new ArrayList<>();


        final CharSequence[] titleList = new CharSequence[listSize];
        this.titleList.toArray(titleList);


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart1;
                List<Uri> uriArrayList1 = tempUriList.subList(listStart1, listStart2);
                for (Uri uri : uriArrayList1)
                    t1.add(setThumbnailImage(uri, size, titleList[i++]));
                one = true;


            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart2;
                List<Uri> uriArrayList2 = tempUriList.subList(listStart2, listStart3);
                for (Uri uri : uriArrayList2)
                    t2.add(setThumbnailImage(uri, size, titleList[i++]));
                two = true;
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart3;
                final List<Uri> uriArrayList3 = tempUriList.subList(listStart3, listStart4);
                for (Uri uri : uriArrayList3)
                    t3.add(setThumbnailImage(uri, size, titleList[i++]));
                three = true;


            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart4;
                final List<Uri> uriArrayList4 = tempUriList.subList(listStart4, listStart5);
                for (Uri uri : uriArrayList4)
                    t4.add(setThumbnailImage(uri, size, titleList[i++]));
                four = true;

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart5;
                final List<Uri> uriArrayList5 = tempUriList.subList(listStart5, listStart6);
                for (Uri uri : uriArrayList5)
                    t5.add(setThumbnailImage(uri, size, titleList[i++]));
                five = true;

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart6;
                final List<Uri> uriArrayList6 = tempUriList.subList(listStart6, listStart7);
                for (Uri uri : uriArrayList6)
                    t6.add(setThumbnailImage(uri, size, titleList[i++]));

                six = true;

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart7;
                final List<Uri> uriArrayList7 = tempUriList.subList(listStart7, listStart8);
                for (Uri uri : uriArrayList7)
                    t7.add(setThumbnailImage(uri, size, titleList[i++]));
                seven = true;

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = listStart8;
                final List<Uri> uriArrayList8 = tempUriList.subList(listStart8, listSize);
                for (Uri uri : uriArrayList8)
                    t8.add(setThumbnailImage(uri, size, titleList[i++]));
                eight = true;

            }
        }).start();


        while (true) {
            if (one && two && three && four && five && six && seven && eight) {
                Log.d("tutu", "kkkk");
                thumbs.addAll(t1);
                thumbs.addAll(t2);
                thumbs.addAll(t3);
                thumbs.addAll(t4);
                thumbs.addAll(t5);
                thumbs.addAll(t6);
                thumbs.addAll(t7);
                thumbs.addAll(t8);
                if (mediaType == com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE.VIDEO)
                    tVideo = thumbs;
                if (mediaType == com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE.AUDIO)
                    tAudio = thumbs;
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





        /* if(x)
            for (int i = 0; i < listSize;i++) {
                if (this.thumbs.get(i)== null) {
                    this.thumbs.remove(i);
                    this.thumbs.add(i, setAlphabetImages(titleList[i]));
                }
            }*/

    }


    public void setTitleList(ArrayList<CharSequence> titleList) {
        this.titleList = titleList;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if ((groupByVideo == com.romeotamizh.MediaPlayer.Helpers.Context.GROUPBY.ALBUM || groupByAudio == com.romeotamizh.MediaPlayer.Helpers.Context.GROUPBY.ALBUM)) {
            if (!isInFinalList && contextScreen != com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT.PLAY)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grid, parent, false);
            else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        }
        return new ViewHolder(view);

    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(titleList.get(position));
        holder.extension.setText(extensionList.get(position));
        holder.duration.setText(durationList.get(position));
        holder.circleImageView.setImageBitmap(this.thumbs.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSongChanged = true;
                Log.d("mmm", mediaType.toString());
                if (mediaType == com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE.VIDEO && groupByVideo == com.romeotamizh.MediaPlayer.Helpers.Context.GROUPBY.ALBUM && !isInFinalList) {
                    PlayMedia.callBack(mediaInfoDatabase.getIdsInAlbum(titleList.get(position)).get(0), mediaType);
                    if (mediaType == com.romeotamizh.MediaPlayer.Helpers.Context.MEDIATYPE.VIDEO)
                        isInFinalList = true;

                } else {
                    PlayMedia.callBack(idList.get(position), mediaType);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSize;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView title;
        TextView duration;
        TextView extension;
        RelativeLayout parentLayout;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.subText2);
            extension = itemView.findViewById(R.id.subText1);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            circleImageView = itemView.findViewById(R.id.circle_image_list);
        }

    }


}
