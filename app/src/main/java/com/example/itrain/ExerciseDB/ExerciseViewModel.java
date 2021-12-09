package com.example.itrain.ExerciseDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseRepository exerciseRepository;

    private final LiveData<List<Exercise>> AllExercises;

    public ExerciseViewModel (Application application) {
        super(application);
        exerciseRepository = new ExerciseRepository(application);
        AllExercises = exerciseRepository.getAll();
    }

    LiveData<List<Exercise>> getAll() { return AllExercises; }

    public void insert(Exercise e) { exerciseRepository.insert(e); }
}
