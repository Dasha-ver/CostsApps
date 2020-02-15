package com.example.costsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FirstActivity extends Activity {

    int myCount;
    String checked;
    String firstPage;
    EditText editFirstPage;
    Button buttonGo;
    Button enterFirstPage;
    Calendar calendar;
    private Realm incomeRealm;
    private Realm counterRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buttonGo = findViewById(R.id.but_go);
        enterFirstPage = findViewById(R.id.enter_first_page);
        editFirstPage = findViewById(R.id.edit_first_page);

        //получаем значение банковского кода из StartActivity
        checked = getIntent().getStringExtra("checked");
        editFirstPage.setHint(checked);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);

        incomeRealm = Realm.getDefaultInstance();
        counterRealm = Realm.getDefaultInstance();
        calendar = Calendar.getInstance();

        //переход на SecondActivity, если сумма дохода добавлена
        enterFirstPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firstPage = editFirstPage.getText().toString();
                //проверяем начинается ли сумма с точки, если нет- получаем индекс из БД (counter)
                //увеличиваем его на еденицу, получаем месяц и год на данный момент, добавляем значение
                //суммы доходов в БД, переходим на SecondActivity
                if (firstPage.contains(".")) {
                    int myInt = firstPage.indexOf(".");
                    if (myInt == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Введите сумму корректно! Сумма не может начинаться с точки!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        showCounter();
                        myCount++;
                        addCounter(myCount);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int year = calendar.get(Calendar.YEAR);
                        addIncome(myCount, month, year, firstPage);
                        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                    //проверяем пустое ли поле для ввода суммы, если нет- получаем индекс из БД (counter)
                    //увеличиваем его на еденицу, получаем месяц и год на данный момент, добавляем значение
                    //суммы доходов в БД, переходим на SecondActivity
                } else if (firstPage.equals("")) {
                    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    startActivity(intent);
                } else {
                    showCounter();
                    myCount++;
                    addCounter(myCount);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int year = calendar.get(Calendar.YEAR);
                    addIncome(myCount, month, year, firstPage);
                    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
            }
        });

        //переход на SecondActivity, если сумма дохода недобавлена
        buttonGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    //метод для получения значения индекса
    public void showCounter() {
        ArrayList<Integer> list = new ArrayList<>();
        RealmResults<Counter> count = counterRealm.where(Counter.class).findAll();
        for (Counter c : count) {
            int cnt = c.getCounter();
            list.add(cnt);
            myCount = Collections.max(list);
        }
    }

    //метод для добавления индекса в БД индексов
    public void addCounter(int counter) {
        Counter cou = new Counter();
        cou.setCounter(counter);
        counterRealm.beginTransaction();
        counterRealm.copyToRealm(cou);
        counterRealm.commitTransaction();
    }

    //метод для добавления суммы дохода в БД доходов
    public void addIncome(int incCounter, int month, int year, String sum) {
        MyIncome i = new MyIncome();
        i.setSum(sum);
        i.setiCounter(incCounter);
        i.setMonth(month);
        i.setYear(year);
        incomeRealm.beginTransaction();
        incomeRealm.copyToRealm(i);
        incomeRealm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counterRealm.close();
        incomeRealm.close();
    }
}
