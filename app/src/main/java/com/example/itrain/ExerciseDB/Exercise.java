package com.example.itrain.ExerciseDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.itrain.ExerciseActivity;
import com.example.itrain.R;
import com.example.itrain.TrainingActivity;

@Entity(tableName = "Exercise")
public class Exercise {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "data")
    public String data;

    @ColumnInfo(name = "time")
    public int time;

    public enum enumType {
        type_leg(R.string.leg_type),
        type_chest(R.string.arm_type);

        private String stringValue;
        private int intValue;
        private enumType(int value) {
            //stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
    @ColumnInfo(name = "type")
    public enumType type;

    @ColumnInfo(name = "settings")
    public String settings;

    public Exercise(@NonNull String name, String data, int time, enumType type, String settings) {
        this.name = name;
        this.data = data;
        this.time = time;
        this.type = type;
        this.settings = settings;
    }
}
