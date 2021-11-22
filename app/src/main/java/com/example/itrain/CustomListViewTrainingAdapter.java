package com.example.itrain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CustomListViewTrainingAdapter extends ArrayAdapter<String> {
    private TrainingActivity context;
    private String pathDir;
    private ArrayList<String> names;

    protected CustomListViewTrainingAdapter(TrainingActivity context, String pathDir, ArrayList<String> names) {
        super(context, R.layout.training_listactivity_row, names);

        this.context = context;
        this.pathDir = pathDir;
        this.names = names;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.training_listactivity_row, null, true);
            viewHolder = new ViewHolder(r, position);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.txtTitleExercises.setText(names.get(position));

        return r;
    }

    private class ViewHolder{
        private TextView txtTitleExercises;
        private ImageButton btnInfoExercise;

        public ViewHolder(View view, int position){
            txtTitleExercises = (TextView) view.findViewById(R.id.txtTitleExercises);

            btnInfoExercise = (ImageButton) view.findViewById(R.id.btnInfoExercise);
            btnInfoExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ExerciseActivity.class);
                    intent.putExtra("name", names.get(position));
                    intent.putExtra("path2file", pathDir+"/"+names.get(position)+".txt");
                    context.startActivity(intent);
                }
            });
        }

    }
}
