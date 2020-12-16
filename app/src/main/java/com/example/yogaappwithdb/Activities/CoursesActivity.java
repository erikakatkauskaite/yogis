package com.example.yogaappwithdb.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yogaappwithdb.R;
import com.example.yogaappwithdb.StuffForRequest.Challenge;
import com.example.yogaappwithdb.StuffForRequest.IndicatingView;
import com.example.yogaappwithdb.StuffForRequest.RequestOperator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesActivity extends AppCompatActivity implements RequestOperator.RequestOperatorListener, SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private boolean informationObtained;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private boolean itIsNotFirstTime;
    private float xDifference, yDifference, zDifference;
    private float minValue =5f;
    private Vibrator vibrator;

    private LocationManager locationManager;

    Button sendRequestButton;
    public  TextView title, total;
    private Challenge publication;
    private IndicatingView indicator;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        setTitle("Courses");

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_courses_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.id_plan_button:
                        startActivity(new Intent(getApplicationContext(), PlanActivity.class));
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
                    case R.id.id_courses_button:
                        return  true;
                }
                return false;
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        informationObtained = false;
        itIsNotFirstTime = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        indicator = (IndicatingView) findViewById(R.id.id_generated_graphic);
        total = (TextView) findViewById(R.id.id_total_challenges);

        sendRequestButton = (Button) findViewById(R.id.id_get_challenge);
        title = (TextView) findViewById(R.id.id_set_challenge);

        sendRequestButton.setOnClickListener(requestButtonClicked);

        registerSensorListener();

    }

    private void registerSensorListener()
    {
        if(sensorAccelerometer == null)
        {
            Toast.makeText(CoursesActivity.this, "No sensor", Toast.LENGTH_LONG).show();
            return;
        }
        if(informationObtained)
        {
            sensorManager.unregisterListener(CoursesActivity.this, sensorAccelerometer);
            informationObtained = false;
        }
        else
        {
            sensorManager.registerListener(CoursesActivity.this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            informationObtained = true;
        }
    }

    private void startChallengeAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.challenge_animation);
        title.startAnimation(animation);
    }

    private void startTotalChallengeAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.total_challenge_animation);
        total.startAnimation(animation);
    }

    public void setIndicatorStatus(final int status)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                indicator.setState(status);
                indicator.invalidate();
            }
        });
    }


    private void sendRequest()
    {
        RequestOperator ro = new RequestOperator();
        ro.setListener(this);
        ro.start();

    }

    public void updatePublication()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(publication!= null)
                {
                    title.setText(publication.getTitle());
                    total.setText("See other " + String.valueOf(publication.getTotal()) + " challenges!");
                }
                else
                {
                    title.setText("No challenges found");
                }
            }
        });
    }

    View.OnClickListener requestButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendRequest();
            setIndicatorStatus(IndicatingView.EXECUTING);
        }
    };

    @Override
    public void success(Challenge publication)
    {
        this.publication = publication;
        updatePublication();
        setIndicatorStatus(IndicatingView.SUCCESS);
        startChallengeAnimation();
        title.setVisibility(View.VISIBLE);
        startTotalChallengeAnimation();
    }

    @Override
    public void failed(int responseCode)
    {
        this.publication = null;
        updatePublication();
        setIndicatorStatus(IndicatingView.FAILED);
    }

    // SENSORS
    @Override
    protected void onPause() {
        super.onPause();
        if(sensorAccelerometer != null)
        {
            sensorManager.unregisterListener(CoursesActivity.this, sensorAccelerometer);
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
            sensorManager.registerListener(CoursesActivity.this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0.1f, this);
    }

    private String getOrientation(float[] orientation)
    {
        float x = orientation[0];
        float y = orientation[1];
        float z = orientation[2];

        String or = "";
        if(z > -2 )
        {
            or = " SCREEN FRONT IS UP";
            if(y > -2)
            {

                or += ", PHONE IS UP";
                if (x > 1)
                {
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.firstColor));
                    or += ", LEFT SIDE DOWN";
                }
                else if(x <= -1)
                {
                    or += ", RIGHT SIDE DOWN";
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.secondColor));
                }
                else
                {
                    or+= ", STRAIGHT";
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                }
            }
            else if(y<=-2)
            {
                or += ", PHONE UPSIDE DOWN";
                if (x > 1)
                {
                    or += ", LEFT SIDE DOWN";
                }
                else if(x <= -1)
                {
                    or += ", RIGHT SIDE DOWN";
                }
                else
                {
                    or+= ", STRAIGHT";
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                }
            }
        }

        else if (z <= -2)
        {
            or = " SCREEN BACK IS UP";
            if(y > -2)
            {
                or += ", PHONE IS UP";
                if (x > 1)
                {
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.thirdColor));
                    or += ", LEFT SIDE DOWN";
                }
                else if(x <= -1)
                {
                    or += ", RIGHT SIDE DOWN";
                }
                else
                {
                    or+= ", STRAIGHT";
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                }
            }
            else if(y<=-2)
            {
                vibrator.vibrate(500);
                or += ", PHONE IS DOWN";
                if (x > 1)
                {
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.fourthColor));
                    or += ", LEFT SIDE DOWN";
                }
                else if(x <= -1)
                {
                    or += ", RIGHT SIDE DOWN";
                }
                else
                {
                    or+= ", STRAIGHT";
                    sendRequestButton.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                }
            }
        }

        return or;
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
            getOrientation(sensorData);

            if(!itIsNotFirstTime)
            {
                //xValue.setText(String.valueOf(event.values[0]));
                //yValue.setText(String.valueOf(event.values[1]) + orientation);
                //zValue.setText(String.valueOf(event.values[2]));
            }

            if(itIsNotFirstTime)
            {
                xDifference = Math.abs(lastX - currentX);
                yDifference = Math.abs(lastY - currentY);
                zDifference = Math.abs(lastZ - currentZ);

                if((xDifference > minValue && yDifference > minValue) ||
                        (xDifference > minValue && zDifference > minValue) ||
                        ( yDifference > minValue && zDifference>minValue))
                {
                    //xValue.setText(String.valueOf(currentX));
                    //yValue.setText(String.valueOf(currentY) + orientation);
                   // zValue.setText(String.valueOf(currentZ));
                    sendRequest();
                    setIndicatorStatus(IndicatingView.EXECUTING);

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                    {
                        vibrator.vibrate(500);
                    }
                }
            }
            lastX = currentX;
            lastY = currentY;
            lastZ = currentZ;

            itIsNotFirstTime = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null)
        {
           // coordinates.setText(location.getLatitude() + "  " + location.getLongitude() );
        }
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