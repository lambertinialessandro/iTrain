package com.example.itrain.ExerciseDB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {
    public abstract ExerciseDAO ExerciseDAO();

    private static volatile ExerciseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ExerciseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExerciseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExerciseDatabase.class, "iTrain_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ExerciseDAO dao = INSTANCE.ExerciseDAO();
                dao.deleteAll();

                Exercise exercise = new Exercise("Hello", "", 60, Exercise.enumType.type_leg, "");
                dao.insert(exercise);
                exercise = new Exercise("Hello", "", 60, Exercise.enumType.type_chest, "");
                dao.insert(exercise);
            });
        }
    };
}
