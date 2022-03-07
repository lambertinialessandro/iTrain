package com.example.itrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TrainingActivity extends AppCompatActivity implements CustomRecyclerViewTrainingAdapter.ItemClickListener {
    private RecyclerView ta_RecyclerView;
    private ProgressBar ta_ProgressBar;
    private TextView ta_txtTitle;

    private String pathDir;
    private ArrayList<String> namesFiles;

    protected CustomRecyclerViewTrainingAdapter customRecyclerViewTrainingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ta_RecyclerView = (RecyclerView) findViewById(R.id.AT_recyclerViewExercise);
        ta_ProgressBar = (ProgressBar) findViewById(R.id.AT_progressBarExercise);
        ta_txtTitle = (TextView) findViewById(R.id.AT_txtTitle);

        this.pathDir = getIntent().getExtras().getString("path");
        ta_txtTitle.setText(getIntent().getExtras().getString("nameTraining"));

        loadData();
    }

    public void loadData(){
        File directory = new File(pathDir);
        File[] files = directory.listFiles();
        this.namesFiles = new ArrayList<String>();
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

            namesFiles.add(name);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ta_RecyclerView.setLayoutManager(layoutManager);

        this.customRecyclerViewTrainingAdapter = new CustomRecyclerViewTrainingAdapter(this, namesFiles);
        customRecyclerViewTrainingAdapter.setClickListener(this);

        ta_RecyclerView.setAdapter(customRecyclerViewTrainingAdapter);

        ta_ProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        String name = customRecyclerViewTrainingAdapter.getName(position);
        intent.putExtra("name", name);
        intent.putExtra("path2file", this.pathDir+"/"+name+".txt");
        this.startActivity(intent);
    }

    public void addNewExerciseClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrainingActivity.this);
        alertDialog.setTitle("Add exercise");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alter_dialog_add_exercise, null);
        alertDialog.setView(dialogView);

        EditText editTextTitleExercise = (EditText) dialogView.findViewById(R.id.editTextTitleExercise);
        EditText editTextTime = (EditText) dialogView.findViewById(R.id.editTextTime);
        EditText editTextSetting = (EditText) dialogView.findViewById(R.id.editTextSetting);

        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = editTextTitleExercise.getText().toString();
                        int time = Integer.parseInt(editTextTime.getText().toString());
                        String setting = editTextSetting.getText().toString();

                        FileOutputStream fos = null;
                        try {
                            File ff = new File(pathDir +"/"+fileName+".txt");
                            fos = new FileOutputStream(ff);
                            // TODO
                            JSONObject json = new JSONObject();
                            json.put("name", fileName);
                            json.put("message", "");
                            json.put("time", time);
                            json.put("setting", setting);
                            fos.write((json.toString()).getBytes());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                        int pos = customRecyclerViewTrainingAdapter.getItemCount();
                        namesFiles.add(fileName);
                        customRecyclerViewTrainingAdapter.notifyItemInserted(pos);
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

    public void dellTrainClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrainingActivity.this);
        alertDialog.setTitle("Delete workout?");

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File f = new File(pathDir);
                        for (File ff : f.listFiles()){
                            ff.delete();
                        }

                        f.delete();
                        finish();

                        // TODO open new training without backstory
                        /*
                        Intent intent = new Intent(TrainingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        TrainingActivity.this.startActivity(intent);
                        */
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}