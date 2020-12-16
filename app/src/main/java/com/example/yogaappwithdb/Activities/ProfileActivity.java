package com.example.yogaappwithdb.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yogaappwithdb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private Button uploadPost;
    private Button openIGButton;
    private Button openCompassButton;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_profile_button);
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
                    case R.id.id_meditation_button:
                        startActivity(new Intent(getApplicationContext(), MeditationActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_profile_button:
                        return  true;
                }
                return false;
            }
        });

        uploadPost = findViewById(R.id.id_upload_post);
        uploadPost.setOnClickListener(openUploadPostActivity);

       openIGButton = (Button) findViewById(R.id.id_open_ig);
       openIGButton.setOnClickListener(openIG);

        setTitle("Profile");

    }

    View.OnClickListener openIG = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("http://instagram.com/_u/oompa.loompae");
            Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
            instagram.setPackage("com.instagram.android");
            try {
                startActivity(instagram);
            }catch (ActivityNotFoundException e)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/oompa.loompae")));
            }
        }
    };

    //Open Upload Post activity
    View.OnClickListener openUploadPostActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, UploadPostActivity.class);
            startActivity(intent);
        }
    };
}