package com.romeotamizh.MusicPlayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    ArrayList<String> mTitles = new ArrayList<>();
    ArrayList<Integer> mDurations = new ArrayList<>();
    public static Context context;
    public static Cursor cursor;
    ArrayList<String> mDatas = new ArrayList<>();
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

    public static void openPlayScreen(final String mData, final String mTitle) {
        Intent intent = new Intent(context, PlayScreenActivity.class);
        intent.putExtra("title", mTitle);
        context.startActivity(intent);


    }

    void initialize(){
        context = this;
        checkPermissions();
        addMusic();
        initializeRecyclerView();


    }

    void checkPermissions() {
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

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addMusic() {


        cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                mTitles.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurations.add(cursor.getInt(cursor.getColumnIndex("duration")));
                mDatas.add(cursor.getString(cursor.getColumnIndex("_data")));
                //musicFilesUri.add(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getInt(cursor.getColumnIndex("_id"))));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));
            }
            cursor.moveToFirst();
            Log.d("data", cursor.getString(cursor.getColumnIndex("_data")));
            cursor.moveToFirst();
        }


    }

    void initializeRecyclerView() {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitles, mDurations, mDatas, getBaseContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}



