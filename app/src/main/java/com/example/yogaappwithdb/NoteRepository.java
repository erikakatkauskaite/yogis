package com.example.yogaappwithdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.yogaappwithdb.NotesDataModel.Note;
import com.example.yogaappwithdb.NotesDataModel.NotesDao;
import com.example.yogaappwithdb.NotesDataModel.NotesDatabase;

import java.util.List;

public class NoteRepository {

    private NotesDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application)
    {
        NotesDatabase database = NotesDatabase.getDBInstance(application);
        noteDao = database.notesDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note)
    {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note)
    {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note)
    {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes()
    {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private  NotesDao notesDao;

        private InsertNoteAsyncTask(NotesDao notesDao)
        {
            this.notesDao = notesDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.insertNote(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private  NotesDao notesDao;

        private UpdateNoteAsyncTask(NotesDao notesDao)
        {
            this.notesDao = notesDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.updateNote(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private  NotesDao notesDao;

        private DeleteNoteAsyncTask(NotesDao notesDao)
        {
            this.notesDao = notesDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            notesDao.deleteNote(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private  NotesDao notesDao;

        private DeleteAllNoteAsyncTask(NotesDao notesDao)
        {
            this.notesDao = notesDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            notesDao.deletAll();
            return null;
        }
    }

}
