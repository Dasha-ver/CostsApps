package com.example.costsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;

//класс старта приложения

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//назначаем toolbar функции actionbar

        Firsttimexecute();
    }

    // метод для получения состояния активности. Узнаём был ли выполнен вход первый раз, если да, то переходим
    // на StartActivity для выбора валюты, если вход выполнен более одного раза, то переходим на SecondActivity
    public void Firsttimexecute() {
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", false);
        if (firstRun == false) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
            Intent i = new Intent(MainActivity.this, StartActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent second = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(second);
            finish();
        }
    }
}