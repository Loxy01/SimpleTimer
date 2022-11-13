package com.learn.timer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsActivity extends AppCompatActivity {
    protected static final String file_name = "save_text.txt";
    protected static final String file_checkBox = "check_box.txt";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch DarkTheme;
    TextView VkLink;
    TextView AppVersion;
    TextView EnableSound;
    CheckBox checkBox_EnableSound;

    boolean DarkModeIsAvailable;

    ConstraintLayout l;
    String ValueThemeState;

    AudioManager audio;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AppVersion = findViewById(R.id.AppVersion);
        VkLink = findViewById(R.id.OpenVkAccount);
        DarkTheme = findViewById(R.id.ChangeTheme);
        l = findViewById(R.id.SettingsLayout);
        EnableSound = findViewById(R.id.sound_enabled_text);
        checkBox_EnableSound = findViewById(R.id.check_sound_enabled);

        audio = (AudioManager) getSystemService(AUDIO_SERVICE);

        LoadStateThemeMode(l,DarkTheme,VkLink,AppVersion,EnableSound);
        LoadStateCheckBox();

        String getAppVersion = AppVersion.getText().toString();
        AppVersion.setText(getAppVersion + "1.1");/*Тут будет версия приложения*/

        androidx.appcompat.widget.Toolbar SettingsToolbar = findViewById(R.id.SettingsToolbar);

        SettingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backActivity = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(backActivity);
                finish();
            }
        });
        VkLink.setMovementMethod(LinkMovementMethod.getInstance());

        VkLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/new2019year_top"));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        checkBox_EnableSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBox_EnableSound.setButtonDrawable(getResources().getDrawable(R.drawable.checkaccesbox));
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                    String IsChecked = "1";
                    SaveStateCheckBox(IsChecked);
                } else {
                    checkBox_EnableSound.setButtonDrawable(getResources().getDrawable(R.drawable.shape));
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMinVolume(AudioManager.STREAM_MUSIC), 0);

                    String IsChecked = "0";
                    SaveStateCheckBox(IsChecked);
                }
            }
        });
        DarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){SetNightMode();
                    DarkModeIsAvailable = true;
                    ValueThemeState = "1";

                    SaveStateThemeMode(ValueThemeState);
                } else{SetDayMode();
                    DarkModeIsAvailable = false;
                    ValueThemeState = "0";

                    SaveStateThemeMode(ValueThemeState); // Осталось только реализовать проверку данных прии создании активити
                }
            }
        });
    }
    protected void SetNightMode(){l.setBackgroundColor(getResources().getColor(R.color.Telegram_gray_color));
            DarkTheme.setTextColor(getResources().getColor(R.color.white));
            AppVersion.setTextColor(getResources().getColor(R.color.white));
            EnableSound.setTextColor(getResources().getColor(R.color.white));
            VkLink.setLinkTextColor(getResources().getColor(R.color.blue));}
    protected void SetDayMode(){l.setBackgroundColor(getResources().getColor(R.color.white));
        DarkTheme.setTextColor(getResources().getColor(R.color.black));
        VkLink.setLinkTextColor(getResources().getColor(R.color.link));
        AppVersion.setTextColor(getResources().getColor(R.color.black));
        EnableSound.setTextColor(getResources().getColor(R.color.black));
    }

    protected void SaveStateThemeMode(String stateTheme){
        String getValue = stateTheme;
        FileOutputStream out = null;

        try {
            out = openFileOutput(file_name, MODE_PRIVATE);

            out.write(getValue.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    protected void LoadStateThemeMode
            (
            ConstraintLayout Layout_setTheme,
            Switch Switch_setTheme,
            TextView VkLink,
            TextView AppVersion,
            TextView PermissionForSound
            )
    {
        DarkTheme = findViewById(R.id.ChangeTheme);

        FileInputStream setText = null;
        String stateThemeNow = null;
        try {
            setText = openFileInput(file_name);

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

                    if(stateThemeNow.equals("1")){
                        DarkTheme.setChecked(true);
                        SetNightMode();
                    } else if(stateThemeNow.equals("0")){
                        DarkTheme.setChecked(false);
                        SetDayMode();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void SaveStateCheckBox(String stateCheckBox){
        String getValue = stateCheckBox;
        FileOutputStream out = null;

        try {
            out = openFileOutput(file_checkBox, MODE_PRIVATE);
            out.write(getValue.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void LoadStateCheckBox(){
        FileInputStream input = null;
        String setStateCheckBox = null;
        try {
            input = openFileInput(file_checkBox);

            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String text;

            while ((text = bufReader.readLine()) != null){
                stringBuilder.append(text);
            }

            setStateCheckBox = stringBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(input != null){
                try {
                    input.close();

                    if(setStateCheckBox.equals("1")){
                        checkBox_EnableSound.setButtonDrawable(getResources().getDrawable(R.drawable.checkaccesbox));
                        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    } else if (setStateCheckBox.equals("0")){
                        checkBox_EnableSound.setButtonDrawable(getResources().getDrawable(R.drawable.shape));
                        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMinVolume(AudioManager.STREAM_MUSIC), 0);
                    } else if (setStateCheckBox == null){

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}