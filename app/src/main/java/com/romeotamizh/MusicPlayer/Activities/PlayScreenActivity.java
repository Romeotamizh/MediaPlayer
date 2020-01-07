package com.romeotamizh.MusicPlayer.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.romeotamizh.MusicPlayer.R;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.mData;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.mId;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.mTitle;


public class PlayScreenActivity extends AppCompatActivity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);


        //get intent
        // getIntentFunction();










    }



    private void getIntentFunction() {
        mTitle = getIntent().getStringExtra("title");
        mData = getIntent().getStringExtra("data");
        mId = getIntent().getIntExtra("id", 0);
        Log.d("id", String.valueOf(mId));


    }





    @Override
    public void onBackPressed() {


        super.onBackPressed();

    }






}
