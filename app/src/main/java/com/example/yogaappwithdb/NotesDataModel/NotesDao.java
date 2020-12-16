package com.example.yogaappwithdb.NotesDataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yogaappwithdb.DataModel.Post;

import java.util.List;
@Dao
public interface NotesDao
{
        @Insert
        void insertNote(Note note);

        @Update
        void updateNote(Note note);

        @Delete
        void deleteNote(Note note);

        @Query("DELETE FROM Notes")
        public void deletAll();

        @Query("SELECT * from Notes")
        LiveData<List<Note>> getAllNotes();

}
