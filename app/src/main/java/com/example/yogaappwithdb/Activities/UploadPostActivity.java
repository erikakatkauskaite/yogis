package com.example.yogaappwithdb.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yogaappwithdb.DataModel.DataConverter;
import com.example.yogaappwithdb.DataModel.Post;
import com.example.yogaappwithdb.DataModel.PostDAO;
import com.example.yogaappwithdb.DataModel.PostDatabase;
import com.example.yogaappwithdb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UploadPostActivity extends AppCompatActivity {

    PostDAO postDAO;

    private Button takePicture;
    private Button savePost;
    private Button seeAllPosts;

    private ImageView imageView;
    private Bitmap bmpImage;
    private ImageView eyes;

    private BottomNavigationView bottomNavigationView;

    private EditText name, title;

    boolean hasCameraFlash = false;
    private static final int CAMERA_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        imageView = (ImageView) findViewById(R.id.id_postUserImage);
        bmpImage = null;

        setTitle("Add New Post");

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.id_profile_button);
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
                    case R.id.id_courses_button:
                        startActivity(new Intent(getApplicationContext(), CoursesActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.id_profile_button:
                        return  true;
                }
                return false;
            }
        });

        name = (EditText) findViewById(R.id.id_postUserName);
        title = (EditText) findViewById(R.id.id_postUserTitle);

        takePicture = (Button) findViewById(R.id.id_take_picture);
        savePost = (Button) findViewById(R.id.id_upload_post);
        seeAllPosts = (Button) findViewById(R.id.id_see_all_posts);

        takePicture.setOnClickListener(takePictureFunction);
        savePost.setOnClickListener(savePostFunction);
        seeAllPosts.setOnClickListener(seeAllPostsFunction);

        postDAO = PostDatabase.getDBInstance(this).postDAO();
        eyes = (ImageView) findViewById(R.id.id_eyes);

        ActivityCompat.requestPermissions(UploadPostActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraID = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraID, false);
        }
        catch (CameraAccessException e)
        {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void flashLightOn()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraID = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraID, true);
        }
        catch (CameraAccessException e)
        {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void blinkFlashShort()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String myString = "010101";
        long blinkDelay = 50;
        for (int i =0; i< myString.length(); i++)
        {
            if(myString.charAt(i) == '0')
            {
                try
                {
                    String cameraID = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraID, true);
                }
                catch (CameraAccessException e)
                {

                }
            }
            else
            {
                try {
                    String cameraID = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraID, false);
                }
                catch (CameraAccessException e)
                {

                }
            }
            try {
                Thread.sleep(blinkDelay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void blinkFlashLong()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String myString = "010101";
        long blinkDelay = 200;
        for (int i =0; i< myString.length(); i++)
        {
            if(myString.charAt(i) == '0')
            {
                try
                {
                    String cameraID = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraID, true);
                }
                catch (CameraAccessException e)
                {

                }
            }
            else
            {
                try {
                    String cameraID = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraID, false);
                }
                catch (CameraAccessException e)
                {

                }
            }
            try {
                Thread.sleep(blinkDelay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void eyesFadeInAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.eyes_animation);
        eyes.startAnimation(animation);
    }
    private void eyesFadeOutAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.eyes_fade_out);
        eyes.startAnimation(animation);
    }

    final int CAMERA_INTENT = 99;
    //Open UploadPostActivity function
    View.OnClickListener takePictureFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(intent, CAMERA_INTENT);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CAMERA_INTENT:
                if(resultCode == Activity.RESULT_OK)
                {
                    bmpImage = (Bitmap) data.getExtras().get("data");
                    if(bmpImage != null)
                    {
                        imageView.setImageBitmap(bmpImage);
                    }
                    else {
                        Toast.makeText(UploadPostActivity.this, "Image is not set", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(UploadPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //Open UploadPostActivity function
    View.OnClickListener savePostFunction = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {

            if(name.getText().toString().isEmpty() || title.getText().toString().isEmpty() || bmpImage == null)
            {
                Toast.makeText(UploadPostActivity.this, "Something is missing", Toast.LENGTH_SHORT).show();
                eyes.setVisibility(View.VISIBLE);
                eyesFadeInAnimation();
            }
            else {
                eyesFadeOutAnimation();
                Post post = new Post();
                post.setName(name.getText().toString());
                post.setTitle(title.getText().toString());
                post.setImage(DataConverter.convertImageToByteArray(bmpImage)); //method we created in DataConvertor class

                postDAO.insertPost(post);
                Toast.makeText(UploadPostActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                blinkFlashShort();
                blinkFlashLong();
                blinkFlashShort();
            }

        }
    };

    //Open UploadPostActivity function
    View.OnClickListener seeAllPostsFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UploadPostActivity.this, WorldSpaceActivity.class);
            startActivity(intent);
        }
    };
}