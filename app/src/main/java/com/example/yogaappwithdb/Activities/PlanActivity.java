package com.example.yogaappwithdb.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yogaappwithdb.NewNoteActivity;
import com.example.yogaappwithdb.NoteViewModel;
import com.example.yogaappwithdb.NotesAdapter;
import com.example.yogaappwithdb.NotesDataModel.Note;
import com.example.yogaappwithdb.NotesDataModel.NotesDatabase;
import com.example.yogaappwithdb.R;
import com.example.yogaappwithdb.StuffForRequest.IndicatingView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private NoteViewModel noteViewModel;
    FloatingActionButton addNewNoteButton;

    RecyclerView recyclerView;

    public static final int ADD_NOTE_REQUEST = 1;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private boolean informationObtained;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private boolean itIsNotFirstTime;
    private float xDifference, yDifference, zDifference;
    private float minValue =5f;
    private Vibrator vibrator;
    private LocationManager locationManager;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        informationObtained = false;
        itIsNotFirstTime = false;

        setTitle("Notes");


        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_plan_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.id_courses_button:
                        startActivity(new Intent(getApplicationContext(), CoursesActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_meditation_button:
                        startActivity(new Intent(getApplicationContext(), MeditationActivity.class));
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
                    case R.id.id_plan_button:
                        return  true;
                }
                return false;
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        addNewNoteButton = findViewById(R.id.id_add_button);
        addNewNoteButton.setOnClickListener(addNewNote);

        recyclerView = findViewById(R.id.id_notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NotesAdapter notesAdapter = new NotesAdapter();
        recyclerView.setAdapter(notesAdapter);

        registerSensorListener();

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Triggered everytimie data changes
                //Update Recycler view
                notesAdapter.setNotesList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(notesAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(PlanActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    View.OnClickListener addNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PlanActivity.this, NewNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            String noteName = data.getStringExtra(NewNoteActivity.EXTRA_NOTE_NAME);
            String note = data.getStringExtra(NewNoteActivity.EXTRA_NOTE);

            Note newNote = new Note(noteName, note);
            noteViewModel.insert(newNote);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerSensorListener()
    {
        if(sensorAccelerometer == null)
        {
            Toast.makeText(PlanActivity.this, "No sensor", Toast.LENGTH_LONG).show();
            return;
        }
        if(informationObtained)
        {
            //startAndStop.setText("Start");
            sensorManager.unregisterListener(PlanActivity.this, sensorAccelerometer);
            informationObtained = false;
        }
        else
        {
            sensorManager.registerListener(PlanActivity.this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            //startAndStop.setText("Stop");
            informationObtained = true;
        }
    }
    // SENSORS
    @Override
    protected void onPause() {
        super.onPause();
        if(sensorAccelerometer != null)
        {
            sensorManager.unregisterListener(PlanActivity.this, sensorAccelerometer);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.locationManager.removeUpdates(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(sensorAccelerometer != null && informationObtained)
        {
            sensorManager.registerListener(PlanActivity.this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0.1f, this);
    }

    private void addNewByLeft(float[] orientation)
    {
        float x = orientation[0];
        float y = orientation[1];
        float z = orientation[2];

        if(z > -2 && y > -2 && x > 1 ) {
            Intent intent = new Intent(PlanActivity.this, NewNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        }

    }
    //FIRST TASK
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            currentX = event.values[0];
            currentY = event.values[1];
            currentZ = event.values[2];

            float sensorData[] ={currentX, currentY, currentZ};

            addNewByLeft(sensorData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}
