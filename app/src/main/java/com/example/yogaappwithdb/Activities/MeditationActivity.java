package com.example.yogaappwithdb.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yogaappwithdb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class MeditationActivity extends AppCompatActivity  {

    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView rewindButton, playButton, pauseButton, forwardButton;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    int red;

    private  BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        setTitle("Meditation room");

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_meditation_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.id_courses_button:
                        startActivity(new Intent(getApplicationContext(), CoursesActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_plan_button:
                        startActivity(new Intent(getApplicationContext(), PlanActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_world_space_button:
                        startActivity(new Intent(getApplicationContext(), WorldSpaceActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_profile_button:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_meditation_button:
                        return  true;
                }
                return false;
            }
        });

        ContentResolver contentResolver = getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 90);
        playerPosition = findViewById(R.id.id_player_position);
        playerDuration = findViewById(R.id.id_Player_duration);
        seekBar = findViewById(R.id.id_seek_bar);
        rewindButton = findViewById(R.id.id_rew_button);
        playButton = findViewById(R.id.id_play_button);
        pauseButton = findViewById(R.id.id_pause_button);
        forwardButton = findViewById(R.id.id_forward_button);

        //Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.meditation);

        //Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //Set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                //Handler post delay for 0.5 sec
                handler.postDelayed(this, 500);
            }
        };

        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //Convert miliseconds to minutes and secs
        String sDuration = convertFormat(duration);
        //Set duration on text view
        playerDuration.setText(sDuration);

        playButton.setOnClickListener(play);
        pauseButton.setOnClickListener(pause);
        forwardButton.setOnClickListener(forward);
        rewindButton.setOnClickListener(rewind);

        seekBar.setOnSeekBarChangeListener(seekbar);
        mediaPlayer.setOnCompletionListener(media);

    }

    MediaPlayer.OnCompletionListener media = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //Hide pause button
            pauseButton.setVisibility(View.GONE);
            //Show play button
            playButton.setVisibility(View.VISIBLE);
            //Set media player to initial position
            mediaPlayer.seekTo(0);
        }
    };

    // SEEKBAR
    SeekBar.OnSeekBarChangeListener seekbar = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //Check condition
            if(fromUser)
            {
                //When drag the seek bar
                //Set progress on seek bar
                mediaPlayer.seekTo(progress);
            }
            //Set current position on text view
            playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    //PLAY BUTTON
    View.OnClickListener play = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //Hide play button
            ContentResolver contentResolver = getContentResolver();
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 10);
            playButton.setVisibility(View.GONE);
            //Show pause button
            pauseButton.setVisibility(View.VISIBLE);
            //Start media
            mediaPlayer.start();
            //Set max on seek bar
            seekBar.setMax(mediaPlayer.getDuration());
            //Start handler
            handler.postDelayed(runnable, 0);

        }
    };

    //PAUSE BUTTON
    View.OnClickListener pause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentResolver contentResolver = getContentResolver();
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 90);
            //Hide play button
            pauseButton.setVisibility(View.GONE);
            //Show pause button
            playButton.setVisibility(View.VISIBLE);
            //Start media
            mediaPlayer.pause();
            //Start handler
            handler.removeCallbacks(runnable);
        }
    };

    //FORWARD BUTTON
    View.OnClickListener forward = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Get current position of media player
            int currentPosition = mediaPlayer.getCurrentPosition();
            //Get duration of media
            int duration = mediaPlayer.getDuration();
            //Check condition
            if(mediaPlayer.isPlaying() && duration != currentPosition)
            {
                //When media is playing and duration is not equal to current postion
                //Fast forward for 5 seconds
                currentPosition = currentPosition + 5000;
                //Set current position on text view
                playerPosition.setText((convertFormat(currentPosition)));
                //set progress seek bar
                mediaPlayer.seekTo(currentPosition);
            }
        }
    };

    //REWIND BUTTON
    View.OnClickListener rewind = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //get current position of media player
            int currentPosition = mediaPlayer.getCurrentPosition();
            //check condition
            if(mediaPlayer.isPlaying() && currentPosition >5000)
            {
                //When media is playng and current position is greater than 5 seconds
                //Rewind for 5 seconds
                currentPosition = currentPosition - 5000;
                //Get current position on text view
                playerPosition.setText(convertFormat(currentPosition));
                //set progress on seek bar
                mediaPlayer.seekTo(currentPosition);

            }
        }
    };


    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration)
    {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}