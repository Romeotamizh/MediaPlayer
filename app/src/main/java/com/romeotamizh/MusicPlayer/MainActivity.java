package com.romeotamizh.MusicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    ArrayList<String> mTitles = new ArrayList<>();
    ArrayList<Integer> mDurations = new ArrayList<>();
    ArrayList<String> mData = new ArrayList<>();
    Toolbar toolbar;
    RecyclerView recyclerView;
    public  static MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        initialize();


    }
    void initialize(){
        //requestPermissions();

        TedPermission tedPermission = new TedPermission(getBaseContext());
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
               // Toast.makeText(getBaseContext(),"permission granted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        tedPermission.setPermissionListener(permissionListener).setDeniedMessage("Must Accept Permissions").setPermissions(permissions).check();
        addMusic();


        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitles,mDurations,mData,getBaseContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }





    private void addMusic() {

        Cursor cursor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                mTitles.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurations.add(cursor.getInt(cursor.getColumnIndex("duration")));
                mData.add(cursor.getString(cursor.getColumnIndex("_data")));
                //musicFilesUri.add(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getInt(cursor.getColumnIndex("_id"))));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));
            }
            cursor.moveToFirst();
            Log.d("data",cursor.getString(cursor.getColumnIndex("_data")));
            cursor.moveToFirst();
            cursor.close();


        }




    }

     public static void playMusic(String mData){

        try {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.setDataSource(mData);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}



