package com.example.costsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import io.realm.Realm;
import io.realm.RealmResults;

//класс для расчёта доходов

public class FoursActivity extends Activity {
    int index;
    int month;
    int year;
    String selectedMonth;
    String selectedYear;
    String codeResult;
    Spinner incomeMonth;
    Spinner incomeYear;
    ImageButton home;
    Button foursButton;
    Button goCash;
    Button goSpent;
    EditText firstEdit;
    EditText different;
    private Realm incomeRealm;
    private Realm realm;
    private Realm bankCodeRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fours_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Realm.init(getApplicationContext());

        incomeRealm = Realm.getDefaultInstance();
        realm = Realm.getDefaultInstance();
        bankCodeRealm = Realm.getDefaultInstance();

        home = findViewById(R.id.inc_home);
        incomeMonth = findViewById(R.id.spinner_month_f);
        incomeYear = findViewById(R.id.spinner_year_f);
        foursButton = findViewById(R.id.b_fours);
        firstEdit = findViewById(R.id.edit_fours);
        different = findViewById(R.id.different);
        goCash = findViewById(R.id.cash);
        goSpent = findViewById(R.id.button_go_spent);

        showCode();//получаем банковский код
        firstEdit.setHint(codeResult);
        different.setHint(codeResult);


        //показываем доходы за определённые месяц, рассчитываем и показываем разницу "доход/расход"
        foursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double sumForMonth = 0;
                String resOfSearch = " ";
                String difForMonth="";
                ArrayList<Integer>list = new ArrayList<>();
                ArrayList<Double>listSpentForMonth = new ArrayList<>();
                selectedMonth = incomeMonth.getSelectedItem().toString();
                selectedYear = incomeYear.getSelectedItem().toString();
                getSelectedMonthForOneMonth();
                year = Integer.valueOf(selectedYear);

                RealmResults<MyIncome> inc = incomeRealm.where(MyIncome.class)
                        .lessThanOrEqualTo("year", year)
                        .findAll()
                        .where()
                        .lessThanOrEqualTo("month", month)
                        .findAll();
                for (MyIncome m : inc) {
                    int res = m.getiCounter();
                    list.add(res);
                    index = Collections.max(list);//получаем ближайший к запрашиваемой дате индекс из БД, так как
                    //пользователь может не изменять доход каждый месяц
                }
                if (index == 0) {//если он равен нулю, значит доходы не были добавлены до запрашиваемой даты
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Список Ваших доходов за данный месяц пуст!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {//если доходы добавлены
                    RealmResults<MyIncome> incRes = incomeRealm.where(MyIncome.class)
                            .equalTo("iCounter", index)
                            .findAll();
                    for (MyIncome m : incRes) {
                        resOfSearch = m.getSum();//сумма доходов
                    }
                    firstEdit.setText(resOfSearch);
                    list.clear();
                    RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                            .equalTo("year", year)
                            .findAll()
                            .where()
                            .equalTo("month", month)
                            .findAll();
                    for (SpentMoney s : spent) {
                        String spentForMonth = s.getSum();
                        double dSpent = Double.valueOf(spentForMonth);
                        listSpentForMonth.add(dSpent);
                    }
                    for (int j = 0; j < listSpentForMonth.size(); j++) {
                        sumForMonth = sumForMonth + listSpentForMonth.get(j);//сумма расходов на запрашиваемый период
                    }
                    //следующий блок для того, чтобы обрезать строку до максимальной длины не более 3 символов после точки
                    double d = Double.valueOf(resOfSearch);
                    double dif = d - sumForMonth;
                    difForMonth = String.valueOf(dif);
                    int end = difForMonth.length();
                    int start = difForMonth.indexOf(".");
                    int difOfStr = end - start;
                    if (difOfStr > 2) {
                        StringBuffer stringBuffer = new StringBuffer(difForMonth);
                        stringBuffer.delete(start + 3, end);
                        difForMonth = stringBuffer.toString();
                        different.setText(difForMonth);
                        listSpentForMonth.clear();
                    } else {
                        different.setText(difForMonth);
                        listSpentForMonth.clear();
                    }
                }

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoursActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        //слушатель на кнопку перехода на расчёт расходов
        goSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoursActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        //слушатель на кнопку перехода в кошелёк
        goCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoursActivity.this, CashActivity.class);
                startActivity(intent);
            }
        });
    }

    //метод для получения банковского кода
    public void showCode() {
        RealmResults<BankCode> cods = bankCodeRealm.where(BankCode.class).findAll();
        for (BankCode b : cods) {
            codeResult = b.getCode();
        }
    }

    //метод ля преобразования результатов спиннера
    public void getSelectedMonthForOneMonth() {
        switch (selectedMonth) {
            case "Январь":
                month = 1;
                break;

            case "Февраль":
                month = 2;
                break;

            case "Март":
                month = 3;
                break;

            case "Апрель":
                month = 4;
                break;

            case "Май":
                month = 5;
                break;

            case "Июнь":
                month = 6;
                break;

            case "Июль":
                month = 7;
                break;

            case "Август":
                month = 8;
                break;

            case "Сентябрь":
                month = 9;
                break;

            case "Октябрь":
                month = 10;
                break;
            case "Ноябрь":
                month = 11;
                break;

            case "Декабрь":
                month = 12;
                break;
        }

    }
}