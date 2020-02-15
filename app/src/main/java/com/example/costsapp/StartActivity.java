package com.example.costsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//класс для выбора валюты

public class StartActivity extends Activity {

    String selected;
    String codeResult;
    String generalHint;
    Button enter;
    Spinner spinner;
    private Realm bankCodeRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        enter = findViewById(R.id.enter);
        spinner = findViewById(R.id.spinner);

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);
        bankCodeRealm = Realm.getDefaultInstance();

        //получаем выбранное значение спиннера, преобразовываем в банковский код, добавляем в БД,
        //передаём его в FirstActivity
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selected = spinner.getSelectedItem().toString();
                getHint(selected);
                addBankCode(generalHint);
                Intent intent = new Intent(StartActivity.this, FirstActivity.class);
                intent.putExtra("checked", generalHint);
                startActivity(intent);
            }
        });
    }

    //метод для преобразования выбранного значения спиннера в банковский код
    private void getHint(String choice) {
        switch (choice) {
            case "Манаты":
                generalHint = "00.00 AZN";
                break;

            case "Доллары":
                generalHint = "00.00 USD";
                break;

            case "Евро":
                generalHint = "00.00 EUR";
                break;

            case "Рубли":
                generalHint = "00.00 RUB";
                break;

            case "Гривны":
                generalHint = "00.00 UAH";
                break;
        }
    }

    //метод для добавления банковского кода в БД
    public void addBankCode(String code) {
        BankCode myCode = new BankCode();
        myCode.setCode(code);
        bankCodeRealm.beginTransaction();
        bankCodeRealm.copyToRealm(myCode);
        bankCodeRealm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bankCodeRealm.close();
    }
}
