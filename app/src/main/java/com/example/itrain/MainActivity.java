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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CustomRecyclerViewTrainingAdapter.ItemClickListener {
    private RecyclerView ma_RecyclerView;
    private ProgressBar ma_ProgressBar;

    private String pathDir;
    private ArrayList<String> namesDirs;

    protected CustomRecyclerViewTrainingAdapter customRecyclerViewTrainingAdapter;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma_RecyclerView = (RecyclerView) findViewById(R.id.AM_recyclerViewTraining);
        ma_ProgressBar = (ProgressBar) findViewById(R.id.AM_progressBarTraining);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // TODO close the app instead toast
                Toast.makeText(MainActivity.this, "App not working anymore", Toast.LENGTH_SHORT).show();
            }
        }

        this.pathDir = getFilesDir().toString();
        File directory = new File(this.pathDir);

        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
        } else {
            File[] files = directory.listFiles();
            this.namesDirs = new ArrayList<String>();
            for (File fileName : files) {
                if (!fileName.isFile())
                    continue;

                String name = "";
                try {
                    name = fileName.getName();
                }catch(ArrayIndexOutOfBoundsException e){
                    fileName.delete();
                    continue;
                }

                namesDirs.add(name);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ma_RecyclerView.setLayoutManager(layoutManager);

        this.customRecyclerViewTrainingAdapter = new CustomRecyclerViewTrainingAdapter(this, namesDirs);
        customRecyclerViewTrainingAdapter.setClickListener(this);

        ma_RecyclerView.setAdapter(customRecyclerViewTrainingAdapter);

        ma_ProgressBar.setVisibility(View.INVISIBLE);
    }

    public void addNewTrainClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add training");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alter_dialog_add_training, null);
        alertDialog.setView(dialogView);

        EditText adat_editTextTitleExercise = (EditText) dialogView.findViewById(R.id.ADAT_editTextTitleTraining);

        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = adat_editTextTitleExercise.getText().toString();

                        File directory = new File(pathDir+"/"+fileName);
                        if (!directory.exists() || !directory.isDirectory()) {
                            directory.mkdir();

                            int pos = customRecyclerViewTrainingAdapter.getItemCount();
                            namesDirs.add(fileName);
                            customRecyclerViewTrainingAdapter.notifyItemInserted(pos);
                        }
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, TrainingActivity.class);
        String name = customRecyclerViewTrainingAdapter.getName(position);
        intent.putExtra("path", pathDir+"/"+name);
        intent.putExtra("nameTraining", name);
        this.startActivity(intent);
    }
}