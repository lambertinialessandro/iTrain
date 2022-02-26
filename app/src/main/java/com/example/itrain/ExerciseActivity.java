package com.example.itrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.itrain.ExerciseDB.Exercise;

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
    private TextView settingTest;

    private RelativeLayout backgroundTimer;

    private ImageButton btnDellExercise;
    private ImageButton btnSettingsExercise;
    private ImageButton btnTimerExercise;

    private String fileName;
    private String path2file;

    private String message;
    private int time;
    private String setting;

    private AnimatorSet animation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        this.fileName = getIntent().getExtras().getString("name");
        this.path2file = getIntent().getExtras().getString("path2file");

        txtTitleExercises = (TextView) findViewById(R.id.txtTitleExercises);
        txtTitleExercises.setText(fileName);

        txtDataExercise = (EditText) findViewById(R.id.AE_txtDataExercise);
        txtDataExercise.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    saveDataOnFile();
                }
            }
        });

        JSONObject json = null;
        try {
            FileInputStream fis = new FileInputStream(path2file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            bufferedReader.close();
            inputStreamReader.close();
            fis.close();

            Log.d("###", stringBuilder.toString());
            json = new JSONObject(stringBuilder.toString());

            this.message = json.getString("message");
            this.time = json.getInt("time");
            this.setting = json.getString("setting");
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(this, "Problem", Toast.LENGTH_SHORT).show();

            this.message = "";
            this.time = 60;
            this.setting = "";
        }

        timeTest = (TextView) findViewById(R.id.AE_timeTest);
        settingTest = (TextView) findViewById(R.id.AE_settingTest);
        updateTexts();


        backgroundTimer = (RelativeLayout) findViewById(R.id.backgroundTimer);

        btnDellExercise = (ImageButton) findViewById(R.id.AE_btnDellExercise);
        btnDellExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExerciseActivity.this);
                alertDialog.setTitle("Delete exercise");

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
        btnSettingsExercise = (ImageButton) findViewById(R.id.AE_btnSettingsExercise);
        btnSettingsExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExerciseActivity.this);
                alertDialog.setTitle("Modify exercise");

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alter_dialog_modify_exercise, null);
                alertDialog.setView(dialogView);

                EditText editTextTime = (EditText) dialogView.findViewById(R.id.editTextTime);
                editTextTime.setText(String.valueOf(time));
                EditText editTextSetting = (EditText) dialogView.findViewById(R.id.editTextSetting);
                editTextSetting.setText(setting);

                alertDialog.setPositiveButton("Modify",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int appTime = Integer.parseInt(editTextTime.getText().toString());
                                String appSetting = editTextSetting.getText().toString();

                                // TODO check correctness

                                time = appTime;
                                setting = appSetting;

                                saveDataOnFile();
                                updateTexts();
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
        });
        btnTimerExercise = (ImageButton) findViewById(R.id.AE_btnTimerExercise);
        btnTimerExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point size = new Point();
                Display display = getWindowManager().getDefaultDisplay();
                display.getSize(size);
                int height = size.y-250;

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(backgroundTimer, "alpha", 1f);
                fadeIn.setDuration(1);

                ObjectAnimator GoDown = ObjectAnimator.ofFloat(backgroundTimer, "translationY", height);
                GoDown.setDuration(time* 1000L);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(backgroundTimer, "alpha", 0f);
                fadeOut.setDuration(1);

                ObjectAnimator GoUp = ObjectAnimator.ofFloat(backgroundTimer, "translationY", 0);
                GoUp.setDuration(1);

                animation = new AnimatorSet();
                //animation.play(fadeIn).with(whiteButton);
                animation.play(GoDown).after(fadeIn);
                animation.play(fadeOut).after(GoDown);
                //animation.play(fadeOut).with(colorButton)
                animation.play(GoUp).after(fadeOut);

                animation.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        btnTimerExercise.setClickable(false);
                        btnTimerExercise.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.btnTimerExerciseDisabled));

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btnTimerExercise.setClickable(true);
                        btnTimerExercise.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.btnTimerExerciseEnabled));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}
                });

                animation.start();

            }
        });
    }

    private void updateTexts() {
        txtDataExercise.setText(this.message);
        timeTest.setText(String.format("%d s", this.time));
        settingTest.setText(this.setting);
    }

    private void saveDataOnFile() {
        FileOutputStream fos = null;
        try {
            File ff = new File(this.path2file);
            fos = new FileOutputStream(ff);

            JSONObject json = new JSONObject();
            json.put("name", this.fileName);
            json.put("message", this.txtDataExercise.getText().toString());
            json.put("time", this.time);
            json.put("setting", this.setting);

            fos.write((json.toString()).getBytes());

            fos.flush();
            fos.close();
            Toast.makeText(ExerciseActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(ExerciseActivity.this, "Unable to save!", Toast.LENGTH_SHORT).show();
        }
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