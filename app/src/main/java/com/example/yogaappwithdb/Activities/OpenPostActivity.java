package com.example.yogaappwithdb.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yogaappwithdb.DataModel.DataConverter;
import com.example.yogaappwithdb.DataModel.Post;
import com.example.yogaappwithdb.DataModel.PostDatabase;
import com.example.yogaappwithdb.R;

public class OpenPostActivity extends AppCompatActivity {
    private TextView userName, userTitle;
    private Button savePost;
    private ImageView postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_post);
        setTitle("Selected post");

        initViews();

        Intent intent = getIntent();

        int id = intent.getIntExtra("postID", -1);
        if(id!= -1)
        {
            Post post = PostDatabase.getDBInstance(getApplicationContext()).postDAO().loadPostByID(id);

            //Toast.makeText(OpenPostActivity.this, "Uploaded successfully" + post.toString(), Toast.LENGTH_SHORT).show();

            setData(post);
        }
        else
        {
            Toast.makeText(OpenPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public void setData(Post post)
    {
        userName.setText(post.getName());
        userTitle.setText(post.getTitle());
        postImage.setImageBitmap(DataConverter.convertByteArrayToImage(post.getImage()));
    }

    private void initViews()
    {
        userName = findViewById(R.id.id_postUserName);
        userTitle = findViewById(R.id.id_postUserTitle);
        savePost = findViewById(R.id.id_postSaveButton);
        postImage = findViewById(R.id.id_postUserImage);
    }
}