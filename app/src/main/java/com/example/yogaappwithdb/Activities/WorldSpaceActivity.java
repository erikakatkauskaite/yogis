package com.example.yogaappwithdb.Activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;
import androidx.viewpager2.widget.ViewPager2;

import com.example.yogaappwithdb.DataModel.Post;
import com.example.yogaappwithdb.DataModel.PostDAO;
import com.example.yogaappwithdb.DataModel.PostDatabase;
import com.example.yogaappwithdb.PostRecViewAdapter;
import com.example.yogaappwithdb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorldSpaceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostDAO postDAO;
    SensorManager sensorManager;
    Sensor orientationSensor;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_space);

        setTitle("All Posts");

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_world_space_button);
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
                    case R.id.id_profile_button:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_meditation_button:
                        startActivity(new Intent(getApplicationContext(), MeditationActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_world_space_button:
                        return  true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.id_postsRecView);

        postDAO = PostDatabase.getDBInstance(this).postDAO();

        PostRecViewAdapter postRecycler = new PostRecViewAdapter( this,postDAO.getAllPosts());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postRecycler);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        int r = getWindowManager().getDefaultDisplay()
                .getRotation();

       // Post post = PostDatabase.getDBInstance(getApplicationContext()).postDAO().loadPostByID(0);
        //Toast.makeText(WorldSpaceActivity.this, "Uploaded successfully" + post.getId(), Toast.LENGTH_SHORT).show();

        //postDAO.deletAll();

    }

}