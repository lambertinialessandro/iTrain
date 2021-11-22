package com.example.itrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class TrainingActivity extends AppCompatActivity {
    private ListView listViewExercises;
    private ProgressBar loadingExercises;

    protected CustomListViewTrainingAdapter customListViewTrainingAdapter;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        listViewExercises = (ListView) findViewById(R.id.listViewExercises);
        loadingExercises = (ProgressBar) findViewById(R.id.loadingExercises);

        if (ContextCompat.checkSelfPermission(TrainingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(TrainingActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);

            if (ContextCompat.checkSelfPermission(TrainingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // TODO close the app instead toast
                Toast.makeText(TrainingActivity.this, "App not working anymore", Toast.LENGTH_SHORT).show();
            }
        }

        String pathDir = getFilesDir().toString();
        File directory = new File(pathDir);
        if (!directory.exists() || !directory.isDirectory())
            directory.mkdir();

        File[] files = directory.listFiles();
        ArrayList<String> names = new ArrayList<String>();
        for (File fileName : files) {
            if (!fileName.isFile())
                continue;

            String name = "";
            try {
                name = fileName.getName().split(".txt")[0];
            }catch(ArrayIndexOutOfBoundsException e){
                fileName.delete();
                continue;
            }

            names.add(name);
        }

        //Collections.sort(names);
        this.customListViewTrainingAdapter = new CustomListViewTrainingAdapter(TrainingActivity.this, pathDir, names);

        listViewExercises.setAdapter(customListViewTrainingAdapter);

        loadingExercises.setVisibility(View.INVISIBLE);
    }

    public void addNewExerciseClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrainingActivity.this);
        alertDialog.setTitle("Add exercise name:");

        final EditText fileNameEditText = new EditText(TrainingActivity.this);
        final EditText timeEditText = new EditText(TrainingActivity.this);
        final EditText typeEditText = new EditText(TrainingActivity.this);
        alertDialog.setView(fileNameEditText);
        // TODO
        //alertDialog.setView(timeEditText);
        //alertDialog.setView(typeEditText);
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = fileNameEditText.getText().toString();
                        //int time = Integer.parseInt(timeEditText.getText().toString());
                        //String type = typeEditText.getText().toString();

                        FileOutputStream fos = null;
                        try {
                            File ff = new File(getFilesDir() +"/"+fileName+".txt");
                            fos = new FileOutputStream(ff);
                            // TODO
                            /*JSONObject json = new JSONObject();
                            json.put("name", fileName);
                            json.put("message", "");
                            json.put("time", 60);//time);
                            json.put("type", "");//type);
                            fos.write((json.toString()).getBytes());*/
                            fos.write(("").getBytes());
                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } /*catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        customListViewTrainingAdapter.add(fileName);
                        customListViewTrainingAdapter.notifyDataSetChanged();
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

}