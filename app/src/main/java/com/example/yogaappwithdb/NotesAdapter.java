package com.example.yogaappwithdb;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import com.example.yogaappwithdb.NotesDataModel.Note;
import com.example.yogaappwithdb.NotesDataModel.NotesDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
{
    private List<Note> notesList = new ArrayList<>();
    private Activity context;
    private NotesDatabase database;

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesAdapter.ViewHolder holder, int position) {

        Note currentNote = notesList.get(position);

        //database = NotesDatabase.getDBInstance(context);

        holder.noteLabel.setText(currentNote.getName());
        holder.note.setText(currentNote.getNote());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setNotesList(List<Note> notes)
    {
        this.notesList = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position)
    {
        return notesList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView noteLabel, note;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            noteLabel = itemView.findViewById(R.id.id_note_name);
            note = itemView.findViewById(R.id.id_note);
        }
    }
}