package com.example.yogaappwithdb.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDAO {
    @Query("Select * from Posts")
    List<Post> getAllPosts();

    @Insert
    void insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("DELETE FROM Posts")
    public void deletAll();

    @Query("SELECT * from Posts where id =:id")
    Post loadPostByID(int id);
}
