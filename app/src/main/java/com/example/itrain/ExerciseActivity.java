package com.example.itrain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class ExerciseActivity extends AppCompatActivity {
    private TextView txtTitleExercises;
    private EditText txtDataExercise;
    private TextView timeTest;
    private TextView typeTest;

    private ImageButton btnDellExercise;
    private ImageButton btnSettingsExercise;
    private ImageButton btnTimerExercise;

    private String name;
    private String path2file;
    private int time;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        this.name = getIntent().getExtras().getString("name");
        this.path2file = getIntent().getExtras().getString("path2file");

        txtTitleExercises = (TextView) findViewById(R.id.txtTitleExercises);
        txtTitleExercises.setText(name);

        txtDataExercise = (EditText) findViewById(R.id.txtDataExercise);
        txtDataExercise.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    FileOutputStream fos = null;
                    try {
                        File ff = new File(path2file);
                        fos = new FileOutputStream(ff);
                        fos.write((txtDataExercise.getText().toString()).getBytes());
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
        StringBuilder message = new StringBuilder();
        JSONObject json = null;
        try {
            FileInputStream fis = new FileInputStream(path2file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                message.append(line);

                line = bufferedReader.readLine();
                if (line != null)
                    message.append("\n");
            }

            //json = new JSONObject(message.toString());

            bufferedReader.close();
            inputStreamReader.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } /*catch (JSONException e) {
            e.printStackTrace();

            json = new JSONObject();
            try {
                json.put("name", "NaS");
                json.put("message", "");
                json.put("time", 60);
                json.put("type", "Leg");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }*/
        /*try {
            txtDataExercise.setText(json.get("message").toString());
            this.time = Integer.parseInt(json.get("time").toString());
            this.type = json.get("type").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        /*timeTest = (TextView) findViewById(R.id.timeTest);
        timeTest.setText(String.valueOf(this.time));
        typeTest = (TextView) findViewById(R.id.typeTest);
        typeTest.setText(this.type);*/
        txtDataExercise.setText(message);

        btnDellExercise = (ImageButton) findViewById(R.id.btnDellExercise);
        btnDellExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExerciseActivity.this);
                alertDialog.setTitle("Detele exercise");

                final EditText input = new EditText(ExerciseActivity.this);
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(path2file);
                                file.delete();

                                // TODO open new training without backstory
                                Intent intent = new Intent(ExerciseActivity.this, TrainingActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ExerciseActivity.this.startActivity(intent);
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
        });
        btnSettingsExercise = (ImageButton) findViewById(R.id.btnSettingsExercise);
        btnSettingsExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnTimerExercise = (ImageButton) findViewById(R.id.btnTimerExercise);
        btnTimerExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(ExerciseActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}