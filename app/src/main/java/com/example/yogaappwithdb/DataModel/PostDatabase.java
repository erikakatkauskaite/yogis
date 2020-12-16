package com.example.yogaappwithdb.DataModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Post.class, version = 1, exportSchema = false)
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase postDB = null;

    public abstract PostDAO postDAO();

    public static synchronized PostDatabase getDBInstance(Context context)
    {
        if(postDB == null)
        {
            postDB = Room.databaseBuilder(context.getApplicationContext(), PostDatabase.class, "post19b2").allowMainThreadQueries().build();
        }
        return postDB;
    }
}
