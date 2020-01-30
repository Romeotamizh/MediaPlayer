package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
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
import com.romeotamizh.MediaPlayer.Adapters.lastPlayedViewPagerAdapter;
import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;

import static com.romeotamizh.MediaPlayer.Helpers.Thumbnail.setThumbnailImage;


public class MainActivity extends AppCompatActivity implements PlayMedia.OnAddToRecentListListener//BottomNavigationView.OnNavigationItemSelectedListener , View.OnClickListener, View.OnLongClickListener, CustomSeekBar.OnSeekBarProgressListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, MenuItem.OnMenuItemClickListener, SlidingUpPanelLayout.PanelSlideListener, PlayMedia.PlayMusicListener, MediaPlayer.OnCompletionListener {
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
    lastPlayedViewPagerAdapter lastPlayedViewPagerAdapter;
    private TabLayout tabLayout;
    int[] idList = new int[5];
    private Toolbar toolbar;
    public static Context.GROUPBY groupByAudio = Context.GROUPBY.NOTHING;
    public static Context.GROUPBY groupByVideo = Context.GROUPBY.NOTHING;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Uri[] uriList = new Uri[5];
    Menu menu;

    public static int viewPagerSelectedPage;
    private Context.GROUPBY groupBy;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    CharSequence[] titleList = new CharSequence[5];
    Context.MEDIATYPE[] mediaTypeList = new Context.MEDIATYPE[5];
    Bitmap[] bitmaps = new Bitmap[5];
    private ViewPager lastPlayedViewPager;
    private WormDotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        viewPager = null;

        for (Fragment fragment : getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        //check permissions
        checkPermissions();


        //set viewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //setlastPlayedViewPager
        lastPlayedViewPager = findViewById(R.id.last_five_media);

        lastPlayedViewPagerAdapter = new lastPlayedViewPagerAdapter();


        //set tabs
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //set Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("MainSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        groupBy = Context.GROUPBY.valueOf(sharedPreferences.getString("GROUPBY", Context.GROUPBY.NOTHING.toString()));
        groupByAudio = groupByVideo = groupBy;

        PlayMedia.setOnAddToRecentListListener(this);

        for (int i_ = 0; i_ < 5; i_++) {
            idList[i_] = sharedPreferences.getInt("id" + i_, 0);
            uriList[i_] = sharedPreferences.getString("uri" + i_, null) == null ? null : Uri.parse(sharedPreferences.getString("uri" + i_, null));
            mediaTypeList[i_] = sharedPreferences.getString("mediaType" + i_, null) == null ? null : Context.MEDIATYPE.valueOf(sharedPreferences.getString("mediaType" + i_, null));
            titleList[i_] = sharedPreferences.getString("title" + i_, null);
        }


        int x = 0;


        for (Uri uri_ : uriList) {
            if (uri_ != null)
                bitmaps[x] = (setThumbnailImage(uri_, new Size(300, 300), titleList[x]));
            x++;
        }

        lastPlayedViewPagerAdapter.setIdList(idList);
        lastPlayedViewPagerAdapter.setUriList(uriList);
        lastPlayedViewPagerAdapter.setPreviews(bitmaps);
        lastPlayedViewPagerAdapter.setTitleList(titleList);
        lastPlayedViewPagerAdapter.setMediaTypeList(mediaTypeList);
        lastPlayedViewPagerAdapter.notifyDataSetChanged();
        lastPlayedViewPager.setAdapter(lastPlayedViewPagerAdapter);


        dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(lastPlayedViewPager);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        Context.callBack(item);

        if (groupBy == Context.GROUPBY.NOTHING)
            editor.putString("GROUPBY", Context.GROUPBY.ALBUM.toString()).commit();
        else
            editor.putString("GROUPBY", Context.GROUPBY.NOTHING.toString()).commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Context.callBack();
        if (isExitAudio && isExitVideo) {
            android.os.Process.killProcess(android.os.Process.myPid());


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());

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

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter;
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        Fragment audioFragment;
        audioFragment = new AudioFragment();
        Fragment videoFragment = null;
        videoFragment = new VideoFragment();

        adapter.addFragment(audioFragment, "AUDIO");
        adapter.addFragment(videoFragment, "VIDEO");
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPagerSelectedPage = position;
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerSelectedPage = position;

                // getSupportFragmentManager().findFragmentByTag("VIDEO")

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // adapter.getItem(0).getActivity().

    }


    @Override
    public void addToRecentList(int id, Uri uri, CharSequence title, Context.MEDIATYPE mediaType) {
        for (int id_ : idList)
            if (id == id_)
                return;

        {

            int[] i = Arrays.copyOf(idList, 4);
            Uri[] u = Arrays.copyOf(uriList, 4);
            Context.MEDIATYPE[] m = Arrays.copyOf(mediaTypeList, 4);
            CharSequence[] t = Arrays.copyOf(titleList, 4);

            idList[0] = id;
            uriList[0] = uri;
            mediaTypeList[0] = mediaType;
            titleList[0] = title;


            for (int i_ = 1; i_ < 5; i_++) {
                idList[i_] = i[i_ - 1];
                uriList[i_] = u[i_ - 1];
                mediaTypeList[i_] = m[i_ - 1];
                titleList[i_] = t[i_ - 1];

            }


            int x = 0;

            for (Uri uri_ : uriList) {
                if (uri_ != null)
                    bitmaps[x] = (setThumbnailImage(uri_, new Size(300, 300), title));
                x++;

            }

            for (int i_ = 0; i_ < 5; i_++) {

                editor.putInt("id" + i_, idList[i_]).commit();

                Uri uri_ = uriList[i_];
                if (uri_ == null) {

                    editor.putString("mediaType" + i_, null).commit();
                    editor.putString("uri" + i_, null).commit();
                    editor.putString("title" + i_, null).commit();

                } else {
                    editor.putString("mediaType" + i_, mediaTypeList[i_].toString()).commit();
                    editor.putString("uri" + i_, uri_.toString()).commit();
                    editor.putString("title" + i_, titleList[i_].toString()).commit();
                }


            }

            Log.d(Arrays.toString(idList), Arrays.toString(uriList));


            lastPlayedViewPagerAdapter.setIdList(idList);
            lastPlayedViewPagerAdapter.setUriList(uriList);
            lastPlayedViewPagerAdapter.setPreviews(bitmaps);
            lastPlayedViewPagerAdapter.setTitleList(titleList);
            lastPlayedViewPagerAdapter.setMediaTypeList(mediaTypeList);
            lastPlayedViewPagerAdapter.notifyDataSetChanged();
            lastPlayedViewPager.setAdapter(lastPlayedViewPagerAdapter);


        }
    }
}



