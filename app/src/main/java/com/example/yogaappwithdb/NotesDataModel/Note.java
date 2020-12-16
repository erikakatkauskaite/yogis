package com.example.yogaappwithdb.NotesDataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Title")
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Note(String name, String note) {
        this.name = name;
        this.note = note;
    }
}
