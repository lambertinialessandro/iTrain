package com.example.itrain.ExerciseDB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class ExerciseRepository {
    private ExerciseDAO ExerciseDAO;
    private LiveData<List<Exercise>> AllExercises;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ExerciseRepository(Application application) {
        ExerciseDatabase db = ExerciseDatabase.getDatabase(application);
        ExerciseDAO = db.ExerciseDAO();
        AllExercises = ExerciseDAO.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Exercise>> getAll() {
        return AllExercises;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Exercise e) {
        ExerciseDatabase.databaseWriteExecutor.execute(() -> {
            ExerciseDAO.insert(e);
        });
    }
}
