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
    private RecyclerView RecyclerViewExercises;
    private ProgressBar loadingExercises;

    private String pathDir;
    private ArrayList<String> names;

    protected CustomRecyclerViewTrainingAdapter customRecyclerViewTrainingAdapter;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        RecyclerViewExercises = (RecyclerView) findViewById(R.id.RecyclerViewExercises);
        loadingExercises = (ProgressBar) findViewById(R.id.loadingExercises);

        if (ContextCompat.checkSelfPermission(TrainingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(TrainingActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);

            if (ContextCompat.checkSelfPermission(TrainingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // TODO close the app instead toast
                Toast.makeText(TrainingActivity.this, "App not working anymore", Toast.LENGTH_SHORT).show();
            }
        }

        this.pathDir = getFilesDir().toString();
        File directory = new File(pathDir);
        if (!directory.exists() || !directory.isDirectory())
            directory.mkdir();

        File[] files = directory.listFiles();
        this.names = new ArrayList<String>();
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
        Collections.sort(names);

        Log.d("###", names.toString());


        File f = new File(pathDir);
        for (File ff : f.listFiles()){
            Log.d("###", ff.getName().toString());
            //ff.delete();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerViewExercises.setLayoutManager(layoutManager);

        this.customRecyclerViewTrainingAdapter = new CustomRecyclerViewTrainingAdapter(this, names);
        customRecyclerViewTrainingAdapter.setClickListener(this);

        RecyclerViewExercises.setAdapter(customRecyclerViewTrainingAdapter);

        loadingExercises.setVisibility(View.INVISIBLE);
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
        Spinner spinnerType = (Spinner) dialogView.findViewById(R.id.spinnerType);
        EditText editTextSetting = (EditText) dialogView.findViewById(R.id.editTextSetting);

        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = editTextTitleExercise.getText().toString();
                        int time = Integer.parseInt(editTextTime.getText().toString());
                        String type = spinnerType.getSelectedItem().toString();
                        String setting = editTextSetting.getText().toString();

                        FileOutputStream fos = null;
                        try {
                            File ff = new File(getFilesDir() +"/"+fileName+".txt");
                            fos = new FileOutputStream(ff);
                            // TODO
                            JSONObject json = new JSONObject();
                            json.put("name", fileName);
                            json.put("message", "");
                            json.put("time", time);
                            json.put("type", type);
                            json.put("setting", setting);
                            fos.write((json.toString()).getBytes());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int pos = customRecyclerViewTrainingAdapter.getItemCount();
                        names.add(fileName);
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

    public void sortNamesPerpetual(View view){
        Collections.sort(names);
        customRecyclerViewTrainingAdapter.notifyDataSetChanged();
    }
}