package com.example.yogaappwithdb.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "Posts")
public class Post {
    @PrimaryKey(autoGenerate = true)
     int id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Title")
    private String title;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) //Bytes
    private byte[] image;
    private boolean isExpanded;



    public Post(int id, String name, String title, byte[] image, boolean isExpanded) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.image = image;
        isExpanded = false;
    }

    public Post() {
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", image=" + Arrays.toString(image) +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
