package com.example.itrain.ExerciseDB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.itrain.ExerciseDB.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Query("SELECT * FROM exercise")
    LiveData<List<Exercise>> getAll();

    /*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);*/

    @Insert
    void insert(Exercise e);

    @Delete
    void delete(Exercise e);

    @Query("DELETE FROM exercise")
    void deleteAll();
}
