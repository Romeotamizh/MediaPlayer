package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.romeotamizh.MediaPlayer.Adapters.ViewPagerAdapter;
import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.Helpers.MyApplication;
import com.romeotamizh.MediaPlayer.R;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity //BottomNavigationView.OnNavigationItemSelectedListener , View.OnClickListener, View.OnLongClickListener, CustomSeekBar.OnSeekBarProgressListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, MenuItem.OnMenuItemClickListener, SlidingUpPanelLayout.PanelSlideListener, PlayMedia.PlayMusicListener, MediaPlayer.OnCompletionListener {
{
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static boolean isExitVideo = true;
    public static int seekBarMax;
    public static int seekBarWidth;
    public static boolean isFirstTime = true;
    public static boolean isExitAudio = true;
    public static boolean isSlideCollapsed = true;
    public static boolean isSongChanged = false;
    public static boolean isSlideExpanded = false;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    public static Context.GROUPBY groupByAudio = Context.GROUPBY.NOTHING;
    public static Context.GROUPBY groupByVideo = Context.GROUPBY.NOTHING;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check permissions
        checkPermissions();


        //set viewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //set tabs
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //set Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("MainSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        MyApplication.callBack(item);
        Log.d("io.", "lol");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MyApplication.callBack();
        if (isExitAudio && isExitVideo)
            super.onBackPressed();


    }


    void checkPermissions() {
        final TedPermission tedPermission = new TedPermission(getBaseContext());
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                tedPermission.setDeniedMessage("Permissions Needed");

            }
        };
        tedPermission.setPermissionListener(permissionListener).setDeniedMessage("Must Accept Permissions").setPermissions(permissions).check();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        Fragment audioFragment = new AudioFragment();
        Fragment videoFragment = new VideoFragment();

        adapter.addFragment(audioFragment, "AUDIO");
        adapter.addFragment(videoFragment, "VIDEO");
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Log.d("1", "1");
                    //   tab

                }
                // getSupportFragmentManager().findFragmentByTag("VIDEO")

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // adapter.getItem(0).getActivity().

    }

}



