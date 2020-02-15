package com.example.costsapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import io.realm.Realm;
import io.realm.RealmResults;

//класс для расчёта суммы в кошельке

public class CashActivity extends Activity {

    String codeResult;
    Calendar calendar;
    Button cashButton;
    EditText cashEdit;
    private int monthNow;
    private int yearNow;
    private Realm bankCodeRealm;
    private Realm incomeRealm;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_page);

        cashEdit = findViewById(R.id.cash_edit);
        cashButton = findViewById(R.id.cash_button);

        Realm.init(getApplicationContext());
        bankCodeRealm = Realm.getDefaultInstance();
        realm = Realm.getDefaultInstance();
        incomeRealm = Realm.getDefaultInstance();

        calendar = Calendar.getInstance();

        showCode();//получаем банковский код
        cashEdit.setHint(codeResult);

        //слушатель на кнопку для получения суммы в кошельке
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                int myIndex = 0;
                double sumForMonth = 0;
                double cash = 0;
                String cashResult = "0";
                String spentForMonth = "0";
                String resOfSearch = " ";
                ArrayList<Integer> myList = new ArrayList<>();
                ArrayList<Integer> list = new ArrayList<>();
                ArrayList<Double> cashValue = new ArrayList<>();
                ArrayList<Double> listSpentForMonth = new ArrayList<>();
                monthNow = calendar.get(Calendar.MONTH) + 1;
                yearNow = calendar.get(Calendar.YEAR);

                RealmResults<MyIncome> incs = incomeRealm.where(MyIncome.class)
                        .lessThanOrEqualTo("year", yearNow)
                        .findAll();
                for (MyIncome m : incs) {
                    int res = m.getiCounter();
                    myList.add(res);
                    myIndex = Collections.max(myList);
                }
                if (myIndex == 0) {//проверка были ли вообще введены доходы
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Список Ваших доходов пуст! Добавьте доходы!", Toast.LENGTH_SHORT);

                    toast.show();
                } else {//если доходы были введены
                    if (yearNow == 2020) {//если год равен 2020
                        for (int i = 1; i <= monthNow; i++) {
                            RealmResults<MyIncome> inc = incomeRealm.where(MyIncome.class)
                                    .equalTo("year", yearNow)
                                    .findAll()
                                    .where()
                                    .lessThanOrEqualTo("month", i)
                                    .findAll();
                            for (MyIncome m : inc) {
                                int res = m.getiCounter();
                                list.add(res);
                                index = Collections.max(list);//находим последний ближайший индекс к каждому месяцу
                            }
                            if (index == 0) {//если сумма доходов ещё не была добавлена вообще до перебираемого месяца,
                                cashValue.add(0.0);//то, мы сразу добавляем в массив разницы "доход/расход" нулевое значение
                            } else {//если сумма уже была добавлена
                                RealmResults<MyIncome> incRes = incomeRealm.where(MyIncome.class)
                                        .equalTo("iCounter", index)
                                        .findAll();
                                for (MyIncome m : incRes) {
                                    resOfSearch = m.getSum();//сумма доходов
                                }
                                list.clear();
                                RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                        .equalTo("year", yearNow)
                                        .findAll()
                                        .where()
                                        .equalTo("month", i)
                                        .findAll();
                                for (SpentMoney s : spent) {
                                    spentForMonth = s.getSum();//сумма расходов
                                    double dSpent = Double.valueOf(spentForMonth);
                                    listSpentForMonth.add(dSpent);
                                }
                                for (int j = 0; j < listSpentForMonth.size(); j++) {
                                    sumForMonth = sumForMonth + listSpentForMonth.get(j);
                                }
                                double d = Double.valueOf(resOfSearch);
                                double dif = d - sumForMonth;//разница "доход/расход"
                                cashValue.add(dif);//добавляем разницу в массив
                                listSpentForMonth.clear();
                            }
                        }
                    } else {//если год больше 2020
                        RealmResults<MyIncome> inc = incomeRealm.where(MyIncome.class)
                                .lessThanOrEqualTo("year", yearNow)
                                .findAll();
                        for (MyIncome m : inc) {
                            int res = m.getiCounter();
                            list.add(res);
                            index = Collections.max(list);//находим ближайший индекс к дате,
                            //нет перебора по месяцам, так как надо найти только ближайший индекс
                        }
                        RealmResults<MyIncome> incRes = incomeRealm.where(MyIncome.class)
                                .equalTo("iCounter", index)
                                .findAll();
                        for (MyIncome m : incRes) {
                            resOfSearch = m.getSum();//сумма доходов
                        }
                        list.clear();
                        for (int i = monthNow; i >= 1; i--) {//перебор месяцев данного года
                            RealmResults<MyIncome> incomess = incomeRealm.where(MyIncome.class)
                                    .equalTo("year", yearNow)
                                    .findAll()
                                    .where()
                                    .lessThanOrEqualTo("month", i)
                                    .findAll();
                            for (MyIncome m : incomess) {
                                int res = m.getiCounter();
                                list.add(res);
                                index = Collections.max(list);//находим последний ближайший индекс к каждому месяцу
                            }
                            if (index == 0) {//если индекс остаётся равным нулю, значит доход был введён в другом году и
                                //его сумма равна той, что в строке 136
                                RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                        .equalTo("year", yearNow)
                                        .findAll()
                                        .where()
                                        .equalTo("month", i)
                                        .findAll();
                                for (SpentMoney s : spent) {
                                    spentForMonth = s.getSum();//сумма расходов
                                    double dSpent = Double.valueOf(spentForMonth);
                                    listSpentForMonth.add(dSpent);
                                }
                                for (int j = 0; j < listSpentForMonth.size(); j++) {
                                    sumForMonth = sumForMonth + listSpentForMonth.get(j);
                                }
                                double d = Double.valueOf(resOfSearch);
                                double dif = d - sumForMonth;//разница "доход/расход"
                                cashValue.add(dif);
                                listSpentForMonth.clear();
                            } else {//если индекс не равен нулю, значит доход изменился
                                RealmResults<MyIncome> incResults = incomeRealm.where(MyIncome.class)
                                        .equalTo("iCounter", index)
                                        .findAll();
                                for (MyIncome m : incResults) {
                                    resOfSearch = m.getSum();//сумма доходов
                                }
                                RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                        .equalTo("year", yearNow)
                                        .findAll()
                                        .where()
                                        .equalTo("month", i)
                                        .findAll();
                                for (SpentMoney s : spent) {
                                    spentForMonth = s.getSum();//сумма расходов
                                    double dSpent = Double.valueOf(spentForMonth);
                                    listSpentForMonth.add(dSpent);
                                }
                                for (int j = 0; j < listSpentForMonth.size(); j++) {
                                    sumForMonth = sumForMonth + listSpentForMonth.get(j);
                                }
                                double d = Double.valueOf(resOfSearch);
                                double dif = d - sumForMonth;//разница "доход/расход"
                                cashValue.add(dif);
                                listSpentForMonth.clear();

                            }
                        }
                        yearNow--;
                        while (yearNow >= 2020) {//пока год не достигнет 2020 включительно
                            RealmResults<MyIncome> incNextYear = incomeRealm.where(MyIncome.class)
                                    .lessThanOrEqualTo("year", yearNow)
                                    .findAll();
                            for (MyIncome m : incNextYear) {
                                int res = m.getiCounter();
                                list.add(res);
                                index = Collections.max(list);//находим ближайший индекс к дате,
                                //нет перебора по месяцам, так как надо найти только ближайший индекс
                            }
                            RealmResults<MyIncome> incResNextYear = incomeRealm.where(MyIncome.class)
                                    .equalTo("iCounter", index)
                                    .findAll();
                            for (MyIncome m : incResNextYear) {
                                resOfSearch = m.getSum();//сумма доходов
                            }
                            list.clear();
                            for (int i = 12; i >= 1; i--) {//перебор месяцев данного года
                                RealmResults<MyIncome> incomess = incomeRealm.where(MyIncome.class)
                                        .equalTo("year", yearNow)
                                        .findAll()
                                        .where()
                                        .lessThanOrEqualTo("month", i)
                                        .findAll();
                                for (MyIncome m : incomess) {
                                    int res = m.getiCounter();
                                    list.add(res);
                                    index = Collections.max(list);//находим последний ближайший индекс к каждому месяцу
                                }
                                if (index == 0) {//если индекс остаётся равным нулю, значит доход был введён в другом году и
                                    //его сумма равна той, что в строке 214
                                    RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                            .equalTo("year", yearNow)
                                            .findAll()
                                            .where()
                                            .equalTo("month", i)
                                            .findAll();
                                    for (SpentMoney s : spent) {
                                        spentForMonth = s.getSum();//сумма расходов
                                        double dSpent = Double.valueOf(spentForMonth);
                                        listSpentForMonth.add(dSpent);
                                    }
                                    for (int j = 0; j < listSpentForMonth.size(); j++) {
                                        sumForMonth = sumForMonth + listSpentForMonth.get(j);
                                    }
                                    double d = Double.valueOf(resOfSearch);
                                    double dif = d - sumForMonth;//разница "доход/расход"
                                    cashValue.add(dif);
                                    listSpentForMonth.clear();
                                } else {//если индекс не равен нулю, значит доход изменился
                                    RealmResults<MyIncome> incResults = incomeRealm.where(MyIncome.class)
                                            .equalTo("iCounter", index)
                                            .findAll();
                                    for (MyIncome m : incResults) {
                                        resOfSearch = m.getSum();//сумма доходов
                                    }
                                    RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                            .equalTo("year", yearNow)
                                            .findAll()
                                            .where()
                                            .equalTo("month", i)
                                            .findAll();
                                    for (SpentMoney s : spent) {
                                        spentForMonth = s.getSum();//сумма расходов
                                        double dSpent = Double.valueOf(spentForMonth);
                                        listSpentForMonth.add(dSpent);
                                    }
                                    for (int j = 0; j < listSpentForMonth.size(); j++) {
                                        sumForMonth = sumForMonth + listSpentForMonth.get(j);
                                    }
                                    double d = Double.valueOf(resOfSearch);
                                    double dif = d - sumForMonth;//разница "доход/расход"
                                    cashValue.add(dif);
                                    listSpentForMonth.clear();
                                }
                            }
                            yearNow--;//уменьшаем год
                        }
                    }
                }
                for (int j = 0; j < cashValue.size(); j++) {
                    cash = cash + cashValue.get(j);//сумма в кошельке
                }
                //следующий блок для того, чтобы обрезать строку до максимальной длины не более 3 символов после точки
                cashResult = String.valueOf(cash);
                int end = cashResult.length();
                int start = cashResult.indexOf(".");
                int difOfStr = end - start;
                if (difOfStr > 2) {
                    StringBuffer stringBuffer = new StringBuffer(cashResult);
                    stringBuffer.delete(start + 3, end);
                    cashResult = stringBuffer.toString();
                    cashEdit.setText(cashResult);
                    cashValue.clear();
                } else {
                    cashEdit.setText(cashResult);
                    cashValue.clear();
                }
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
}