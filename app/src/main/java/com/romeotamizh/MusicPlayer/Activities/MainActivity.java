package com.romeotamizh.MusicPlayer.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.R;
import com.romeotamizh.MusicPlayer.RecyclerViewAdapter;

import java.util.ArrayList;

import static com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper.seekBarListener;
import static com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper.seekBarOperations;

public class MainActivity extends AppCompatActivity   {

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static Context context;
    //public static Cursor cursor;
    Toolbar toolbar;
    RecyclerView recyclerView;
    public static boolean isFirstTime = true;
    public static boolean isFromMainActivity;
    static ViewStub viewStub;
    static View childLayout;
    static SeekbarWithFavourites seekBar;
    static FrameLayout parentLayout;
    static TextView currentPositionTextView;
    static TextView maxLengthTextView;
    Handler handler = new Handler();

    public static void onReturn() {

        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (!isFirstTime) {
                    isFromMainActivity = true;
                    Log.d("m", "true");
                    seekBar = childLayout.findViewById(R.id.seekBar);
                    maxLengthTextView = childLayout.findViewById(R.id.max_length);
                    currentPositionTextView = childLayout.findViewById(R.id.current_position);
                    seekBar.setmFavouriteBitmap(R.mipmap.red_play);
                    seekBarListener(seekBar, currentPositionTextView, "main");
                    seekBarOperations(seekBar, maxLengthTextView, "main");

                }
            }
        };*/

        if (!isFirstTime) {
            isFromMainActivity = true;
            Log.d("m", "true");
            seekBar = childLayout.findViewById(R.id.seekBar);
            maxLengthTextView = childLayout.findViewById(R.id.max_length);
            currentPositionTextView = childLayout.findViewById(R.id.current_position);
            seekBar.setmFavouriteBitmap(R.mipmap.red_play);
            seekBarListener(seekBar, currentPositionTextView, "main");
            seekBarOperations(seekBar, maxLengthTextView, "main");

        }
    }

    public static void openPlayScreen(final String mData, final String mTitle, final int mId) {

        Intent intent = new Intent(context, PlayScreenActivity.class);
        intent.putExtra("title", mTitle);
        intent.putExtra("data", mData);
        intent.putExtra("id", mId);
        context.startActivity(intent);
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!isFirstTime) {
                    parentLayout.addView(childLayout);
                }
                Log.d("pp", "ppp");


            }
        };
        countDownTimer.start();

        /*Handler h =new Handler();
        Runnable r =(new Runnable() {
            @Override
            public void run() {
                if(!isFirstTime){
                parentLayout.addView(childLayout);
            }
                Log.d("pp","ppp");

            }
        });
        h.postDelayed(r,2000);*/


    }


    public static void openPlayScreen(final String mData, final String mTitle) {
        Intent intent = new Intent(context, PlayScreenActivity.class);
        intent.putExtra("title", mTitle);
        intent.putExtra("data", mData);
        context.startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recyclerView);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        childLayout = inflater.inflate(R.layout.layout_seekbar, (ViewGroup) findViewById(R.id.seekBar_parent));
        parentLayout = findViewById(R.id.activity_main_seekBar_frame);
        /*SeekbarWithFavourites seekBar;

        seekBar = findViewById(R.id.seekBar);
*/


        setSupportActionBar(toolbar);
        if (!isFirstTime) {
            parentLayout.addView(childLayout);
        }
        initialize();


    }


    void initialize(){
        context = this;
        checkPermissions();
        addMusic();


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
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Integer> mDurationList = new ArrayList<>();
        ArrayList<Integer> mIdList = new ArrayList<>();
        ArrayList<String> mDataList = new ArrayList<>();
        Cursor cursor;
        Cursor cursorAlbum;
//        cursorAlbum = getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,"upper("+MediaStore.Audio.Albums.ALBUM_ID);



        cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                mTitleList.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurationList.add(cursor.getInt(cursor.getColumnIndex("duration")));
                mDataList.add(cursor.getString(cursor.getColumnIndex("_data")));
                mIdList.add(cursor.getInt(cursor.getColumnIndex("_id")));
                //musicFilesUri.add(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getInt(cursor.getColumnIndex("_id"))));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));

            }
            //cursor.moveToPosition(2);

            // Log.d("data", cursor.getString(cursor.getColumnIndex("track")));


            //cursor.close();
            initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList);
            // initializeRecyclerView(cursor);
        }


    }

    void initializeRecyclerView(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdlist) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitleList, mDurationList, mDataList, mIdlist, getBaseContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void initializeRecyclerView(Cursor cursor) {

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(cursor, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}



