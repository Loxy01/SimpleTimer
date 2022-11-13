package com.learn.timer;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    public static final String file = "save_value_def_time.txt";
    public static final String file_progressSeekBar = "save_sec.txt";

    String setDefTime;
    NumberPicker numberPicker_min;
    NumberPicker numberPicker_sec;

    int getNumberPickerInt_min;
    int getNumberPickerInt_sec;

    SeekBar seekbar;
    TextView time;
    MediaPlayer mediaPlayer;
    Button start;
    AudioManager audio;
    TextView textViewZero;
    int max_volume;
    int min;
    int sec;
    int one_sec = 1000;
    public boolean isStart;
    CountDownTimer timer;
    Context context = MainActivity.this;
    String titletextcolor;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.timer);
        start = findViewById(R.id.button);
        seekbar = findViewById(R.id.seekBar);
        isStart = false;
        seekbar.setMax(1800); //seekbar его значение которое в секундах
        seekbar.setProgress(0);// по умолчанию будет 0 секунд устанавливаться
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_bell);
        audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        max_volume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        setDefaultNightMode(MODE_NIGHT_NO);

        titletextcolor = "Simple Timer";

        setTitle(titletextcolor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                min = i / 60; //Тут прогресс с seekbar делится на 60 секунд, чтобы получить минуты
                sec = i - (min * 60);
                int CountDownTimerSeconds = ((i - (min * 60)) * one_sec);

                String minString = "";
                String secString = "";

                if (min < 10) {
                    minString = "0" + min;
                } else {
                    minString = String.valueOf(min);
                }

                if (sec < 10) {
                    secString = "0" + sec;
                } else {
                    secString = String.valueOf(sec);
                }

                time.setText(minString + ":" + secString);

                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isStart) {
                            start.setText("Stop");
                            seekbar.setEnabled(false);
                            isStart = true;

                            timer = new CountDownTimer(seekbar.getProgress() * 1000, 1000) {
                                @Override
                                public void onTick(long l) {

                                    int min = (int) (l / 1000) / 60;
                                    int sec = (int) (l / 1000) - (min * 60);

                                    String Strmin = "";
                                    String Strsec = "";
                                    if (min < 10) {
                                        Strmin = "0" + min;
                                    } else {
                                        Strmin = String.valueOf(min);
                                    }

                                    if (sec < 10) {
                                        Strsec = "0" + sec;
                                    } else {
                                        Strsec = String.valueOf(sec);
                                    }

                                    time.setText(Strmin + ":" + Strsec);
                                }

                                @Override
                                public void onFinish() {
                                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, max_volume, 0); // этот параметр автоматически устанавливает звук на максимум
                                    mediaPlayer.start();// тут прозвучит звонок колокольчика
                                    int number_time = (getNumberPickerInt_min * 60) + getNumberPickerInt_sec;
                                    seekbar.setProgress(number_time);
                                    /*time.setText("00:00");*/

                                    long l = seekbar.getProgress() * 1000; //Тут обнова, но так работает

                                    int min = (int) (l / 1000) / 60;
                                    int sec = (int) (l / 1000) - (min * 60);

                                    String Strmin = "";
                                    String Strsec = "";
                                    if (min < 10) {
                                        Strmin = "0" + min;
                                    } else {
                                        Strmin = String.valueOf(min);
                                    }

                                    if (sec < 10) {
                                        Strsec = "0" + sec;
                                    } else {
                                        Strsec = String.valueOf(sec);
                                    }
                                    LoadDefTime();
                                }
                            };
                            timer.start();
                        } else {
                            timer.cancel();

                            String Strmin = "";
                            String Strsec = "";

                            if(getNumberPickerInt_min<10){
                                Strmin = "0"+getNumberPickerInt_min;
                            } else {
                                Strmin = String.valueOf(getNumberPickerInt_min);
                            }

                            if(getNumberPickerInt_sec < 10){
                                Strsec = "0"+getNumberPickerInt_sec;
                            } else{
                                Strsec = String.valueOf(getNumberPickerInt_sec);
                            }

                            LoadDefTime();
                            LoadSeekBarProgress();
                            start.setText("Start");
                            seekbar.setEnabled(true);

                            isStart = false;
                        }
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_xml, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SettingsMenu:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.SetTime:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_layout, null);

                numberPicker_min = view.findViewById(R.id.NumberPicker_min);
                numberPicker_sec = view.findViewById(R.id.numberPicker_sec);

                numberPicker_min.setMinValue(0);
                numberPicker_min.setMaxValue(29);

                numberPicker_sec.setMinValue(0);
                numberPicker_sec.setMaxValue(59);

                AlertDialog dialog = builder//Тут идет действие с диалогом
                    .setView(view).show();

                Button CancelButton = view.findViewById(R.id.materialButtonCancel);
                CancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button OkButton = view.findViewById(R.id.materialButtonOk);
                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {dialog.dismiss();

                        getNumberPickerInt_min = numberPicker_min.getValue();
                        getNumberPickerInt_sec = numberPicker_sec.getValue();

                        String Strmin = "";
                        String Strsec = "";

                        if(getNumberPickerInt_min<10){
                            Strmin = "0"+getNumberPickerInt_min;
                        } else {
                            Strmin = String.valueOf(getNumberPickerInt_min);
                        }

                        if(getNumberPickerInt_sec < 10){
                            Strsec = "0"+getNumberPickerInt_sec;
                        } else{
                            Strsec = String.valueOf(getNumberPickerInt_sec);
                        }

                        setDefTime = Strmin + ":" + Strsec;

                        time.setText(setDefTime);
                        int seekBarValue = (getNumberPickerInt_min*60)+getNumberPickerInt_sec;
                        seekbar.setProgress(seekBarValue);

                        SaveSeekBarProgress(seekBarValue);

                        SaveDefTime(setDefTime);
                    }
                });
                break;
        }
        return true;
    }

    public void SaveDefTime(String stateDefTime) {
        String getValue = stateDefTime;
        FileOutputStream out = null;

        try {
            out = openFileOutput(file, MODE_PRIVATE);
            out.write(getValue.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void SaveSeekBarProgress(int seekBarProgress){
        String getValue = String.valueOf(seekBarProgress);
        FileOutputStream file = null;
        try {
            file = openFileOutput(file_progressSeekBar, MODE_PRIVATE);
            file.write(getValue.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(file != null){
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void LoadSeekBarProgress(){
        FileInputStream setText = null;
        String setValue_to_SeekBar = null;
        try {
            setText = openFileInput(file_progressSeekBar);

            InputStreamReader readByteTypeText = new InputStreamReader(setText);
            BufferedReader bufferedReader = new BufferedReader(readByteTypeText);

            StringBuilder stringBuilder = new StringBuilder();
            String text;

            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
            setValue_to_SeekBar = stringBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(setText != null){
                try {
                    setText.close();
                    seekbar.setProgress(Integer.parseInt(setValue_to_SeekBar));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void LoadDefTime(){
        FileInputStream setText = null;
        String stateThemeNow = null;
        try {
            setText = openFileInput(file);

            InputStreamReader readByteTypeText = new InputStreamReader(setText);
            BufferedReader bufferedReader = new BufferedReader(readByteTypeText);

            StringBuilder stringBuilder = new StringBuilder();
            String text;

            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
            stateThemeNow = stringBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(setText != null){
                try {
                    setText.close();
                    time.setText(stateThemeNow);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}