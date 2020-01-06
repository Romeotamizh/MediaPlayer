package com.romeotamizh.MusicPlayer.Activities;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper;

import java.util.ArrayList;

import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnReturnListener.OnReturnListenerInterface, OpenPlayScreenListener.OpenPlayScreenListenerInterface {

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static boolean isFirstTime = true;
    public static boolean isOpenPlayScreen = false;
    public static boolean isFromMainActivity;
    public static boolean isOnReturn = false;
    public static String mData;
    public static String mTitle;
    public static int mId;
    public static OnReturnListener onReturnListener;
    public static OpenPlayScreenListener openPlayScreenListener;
    public ImageView playPause;
    View childLayout;
    SeekbarWithFavourites seekBar;
    FrameLayout parentLayout;
    TextView mainTitle;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView currentPositionTextView;
    TextView maxLengthTextView;
    SeekBarWithFavouritesHelper seekBarWithFavouritesHelperMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recyclerView);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


        childLayout = inflater.inflate(R.layout.layout_linear_controls, (ViewGroup) findViewById(R.id.parent_layout_linear_controls));
        parentLayout = findViewById(R.id.activity_main_seekBar_frame);
        parentLayout.addView(childLayout);
        playPause = findViewById(R.id.main_play_or_pause);
        playPause.setOnClickListener(this);
        seekBar = childLayout.findViewById(R.id.seekBar);
        maxLengthTextView = childLayout.findViewById(R.id.max_length);
        currentPositionTextView = childLayout.findViewById(R.id.current_position);
        mainTitle = childLayout.findViewById(R.id.title_linear_screen);
        currentPositionTextView.setTextColor(getResources().getColor(R.color.White, getTheme()));
        maxLengthTextView.setTextColor(getResources().getColor(R.color.White, getTheme()));


        seekBarWithFavouritesHelperMain = new SeekBarWithFavouritesHelper(seekBar, currentPositionTextView, maxLengthTextView, playPause, "main");


        seekBar.setmFavouriteBitmap(R.mipmap.red_play);




        setSupportActionBar(toolbar);
        if (!isFirstTime) {
            parentLayout.setVisibility(View.VISIBLE);
        }
        initialize();

        onReturnListener = new OnReturnListener();
        onReturnListener.setListener(this);

        openPlayScreenListener = new OpenPlayScreenListener();
        openPlayScreenListener.setListener(this);




    }

    void initialize() {
        checkPermissions();
        addMusic();


    }

    void checkPermissions() {
        TedPermission tedPermission = new TedPermission(getBaseContext());
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
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


            cursor.close();
            initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList);
        }


    }

    void initializeRecyclerView(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdlist) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitleList, mDurationList, mDataList, mIdlist, getBaseContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_play_or_pause:
                seekBarWithFavouritesHelperMain.playPausePress();
                break;

            default:
                break;
        }

    }

    @Override
    public void onReturn() {
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.pause);

        Log.d("jjjlist", "list");
        if (!isFirstTime) {
            isFromMainActivity = true;
            Log.d("m", "true");
            isOnReturn = false;
            seekBarWithFavouritesHelperMain.seekBarListener();
            seekBarWithFavouritesHelperMain.seekBarOperations();

        }

    }

    @Override
    public void openPlayScreen() {
        Intent intent = new Intent(getBaseContext(), PlayScreenActivity.class);
        startActivity(intent);
        mainTitle.setText(mTitle);
        if (!isFirstTime) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parentLayout.setVisibility(View.VISIBLE);
                    Log.d("pp", "ppp");

                }
            });
        }
    }
}



