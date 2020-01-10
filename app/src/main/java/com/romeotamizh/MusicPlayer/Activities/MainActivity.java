package com.romeotamizh.MusicPlayer.Activities;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.romeotamizh.MusicPlayer.AudioFragment;
import com.romeotamizh.MusicPlayer.R;
import com.romeotamizh.MusicPlayer.VideoFragment;
import com.romeotamizh.MusicPlayer.ViewPagerAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener//View.OnClickListener, View.OnLongClickListener, SeekBarWithFavourites.OnSeekBarProgressListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, MenuItem.OnMenuItemClickListener, SlidingUpPanelLayout.PanelSlideListener, PlayMusic.PlayMusicListener, MediaPlayer.OnCompletionListener {
{
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    public static int seekBarMax;
    public static int seekBarWidth;
    public static boolean isFirstTime = true;
    public static boolean isSlideCollapsed = true;
    public static boolean isSongChanged = false;
    public static boolean isSlideExpanded = false;
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check permissions
        checkPermissions();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //set Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //  bottomNavigationView = findViewById(R.id.bootom_navigation);
        // bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }


  /*  @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }*/


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



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
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



