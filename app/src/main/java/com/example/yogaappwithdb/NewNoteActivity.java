package com.example.yogaappwithdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_NAME = "com.example.yogaappwithdb.EXTRA_NOTE_NAME";
    public static final String EXTRA_NOTE = "com.example.yogaappwithdb.EXTRA_NOTE";
    private EditText noteName;
    private EditText note;

    private Button cancel;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        noteName = findViewById(R.id.id_note_title_field);
        note = findViewById(R.id.id_note_field);

        cancel = findViewById(R.id.id_cancel_note);
        confirm = findViewById(R.id.id_confirm_note);

        cancel.setOnClickListener(cancelNewNote);
        confirm.setOnClickListener(confirmNewNote);

        setTitle("Add Note");

    }

    View.OnClickListener cancelNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener confirmNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String newNoteName = noteName.getText().toString();
            String newNote = note.getText().toString();;

            if(newNoteName.trim().isEmpty() || newNote.trim().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_LONG).show();
                return;
            }



            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE_NAME, newNoteName);
            intent.putExtra(EXTRA_NOTE, newNote);

            int position = intent.getExtras().getInt("position", -1);

            if(position!=-1)
            {
                intent.putExtra("position", position);
            }

            note.setText("");
            noteName.setText("");
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}