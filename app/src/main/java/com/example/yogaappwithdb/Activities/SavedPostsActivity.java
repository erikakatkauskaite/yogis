package com.example.yogaappwithdb.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.yogaappwithdb.R;

public class SavedPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        setTitle("Saved posts");
    }
}