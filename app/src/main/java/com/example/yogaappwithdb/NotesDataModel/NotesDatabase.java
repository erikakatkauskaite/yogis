package com.example.yogaappwithdb.NotesDataModel;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.yogaappwithdb.DataModel.Post;
import com.example.yogaappwithdb.DataModel.PostDAO;
import com.example.yogaappwithdb.DataModel.PostDatabase;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase{


        private static NotesDatabase notesDBInstance = null;

        public abstract NotesDao notesDao();

        public static synchronized NotesDatabase getDBInstance(Context context)
        {
            if(notesDBInstance == null)
            {
                notesDBInstance = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "notes_database").allowMainThreadQueries().addCallback(roomCallback).build();

            }
            return notesDBInstance;
        }

        private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
        {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateNotesDBAsyncTask(notesDBInstance).execute();
            }
        };

        private static  class PopulateNotesDBAsyncTask extends AsyncTask<Void, Void, Void>
        {
            private NotesDao notesDao;
            private PopulateNotesDBAsyncTask(NotesDatabase db)
            {
                notesDao = db.notesDao();
            }
            @Override
            protected Void doInBackground(Void... voids) {
                notesDao.insertNote(new Note("Title1", "Lalalalalalaa"));
                notesDao.insertNote(new Note("Title2", "Lalalalalalaa"));
                notesDao.insertNote(new Note("Title3", "Lalalalalalaa"));
                return null;
            }
        }
}
