package com.learn.timer;

import android.annotation.SuppressLint;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SetThemeModeClass {

    @SuppressLint("ResourceAsColor")
    public void SetNightTheme(ConstraintLayout Layout_setTheme, Switch Switch_setTheme, TextView VkLink, TextView AppVersion){
        Layout_setTheme.setBackgroundColor(R.color.Telegram_gray_color);
        Switch_setTheme.setTextColor(R.color.white);
        AppVersion.setTextColor(R.color.white);
        VkLink.setLinkTextColor(R.color.blue);
    }
    @SuppressLint("ResourceAsColor")
    public void SetDayTheme(ConstraintLayout Layout_setTheme, Switch Switch_setTheme, TextView VkLink, TextView AppVersion){
        Layout_setTheme.setBackgroundColor(R.color.white);
        Switch_setTheme.setTextColor(R.color.black);
        VkLink.setLinkTextColor(R.color.link);
        AppVersion.setTextColor(R.color.black);
    }
}
