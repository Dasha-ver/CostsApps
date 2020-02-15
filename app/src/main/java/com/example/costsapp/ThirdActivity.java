package com.example.costsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

//класс для расчёта расходов

public class ThirdActivity extends Activity {

    int searchDay;
    int searchMonth;
    int searchYear;
    int secSearchDay;
    int secSearchMonth;
    int secSearchYear;
    String spinnerMonth;
    String secSpinnerMonth;
    String searchCategory;
    EditText thirdEdit;
    Button thirdBut;
    Button income;
    Button goCash;
    Spinner daySpinner;
    Spinner monthSpinner;
    Spinner yearSpinner;
    Spinner categorySpinner;
    Spinner secDaySpinner;
    Spinner secMonthSpinner;
    Spinner secYearSpinner;
    private  String codeResult;
    private Realm bankCodeRealm;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        thirdEdit = findViewById(R.id.third_edit);
        daySpinner = findViewById(R.id.spinner_day);
        monthSpinner = findViewById(R.id.spinner_month);
        yearSpinner = findViewById(R.id.spinner_year);
        secDaySpinner = findViewById(R.id.spinner_day_second);
        secMonthSpinner = findViewById(R.id.spinner_month_second);
        secYearSpinner = findViewById(R.id.spinner_year_second);
        categorySpinner = findViewById(R.id.spinner_category);
        thirdBut = findViewById(R.id.button_third);
        income = findViewById(R.id.button_third_income);
        goCash = findViewById(R.id.cash_third);

        Realm.init(getApplicationContext());
        bankCodeRealm = Realm.getDefaultInstance();
        realm = Realm.getDefaultInstance();

        showCode();//получаем банковский код
        thirdEdit.setHint(codeResult);

        //добавляем слушатель на кнопку расчёта расходов, получаем результаты из спиннера,
        //делаем выборку из БД рассчитываем результат
        thirdBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSpinnerResult();
                if(searchMonth == secSearchMonth && searchYear == secSearchYear) {//если надо получить расходы в рамках одного года и месяца
                    if (searchCategory.equals("Расходы по всем категориям")) {//в расходах по всем категориям
                        ArrayList<Double> list = new ArrayList<>();
                        double sum = 0;
                        String str = "";
                        RealmResults<SpentMoney> spentStart = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", searchMonth)
                                .findAll()
                                .where()
                                .greaterThanOrEqualTo("day", searchDay)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("day", secSearchDay)
                                .findAll();
                        for (SpentMoney s : spentStart) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        for (int j = 0; j < list.size(); j++) {
                            sum = sum + list.get(j);
                        }
                        str = String.valueOf(sum);
                        thirdEdit.setText(str);
                        list.clear();
                    } else {//если надо подсчитать расходы в рамках одного месяца и года  в определённой категории
                        ArrayList<Double> listForCategory = new ArrayList<>();
                        double sumForCategory = 0;
                        String strForCategory = "";
                        RealmResults<SpentMoney> spentStart = realm.where(SpentMoney.class)
                                .equalTo("category.name", searchCategory)
                                .findAll()
                                .where()
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", searchMonth)
                                .findAll()
                                .where()
                                .greaterThanOrEqualTo("day", searchDay)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("day", secSearchDay)
                                .findAll();
                        for (SpentMoney s : spentStart) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            listForCategory.add(d);
                        }
                        for (int j = 0; j < listForCategory.size(); j++) {
                            sumForCategory = sumForCategory + listForCategory.get(j);
                        }
                        strForCategory = String.valueOf(sumForCategory);
                        thirdEdit.setText(strForCategory);
                        listForCategory.clear();
                    }
                }
                if (searchYear < secSearchYear) {// если надо получить расходы за разные годы
                    if (searchCategory.equals("Расходы по всем категориям")) {//по всем категориям
                        ArrayList<Double> list = new ArrayList<>();
                        double sum = 0;
                        String str = "";
                        RealmResults<SpentMoney> spentStart = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", searchMonth)
                                .findAll()
                                .where()
                                .greaterThanOrEqualTo("day", searchDay)
                                .findAll();
                        for (SpentMoney s : spentStart) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .greaterThan("month", searchMonth)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("month", 12)
                                .findAll();
                        for (SpentMoney s : spent) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spentYears = realm.where(SpentMoney.class)
                                .greaterThan("year", searchYear)
                                .findAll()
                                .where()
                                .lessThan("year", secSearchYear)
                                .findAll();
                        for (SpentMoney s : spentYears) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spents = realm.where(SpentMoney.class)
                                .equalTo("year", secSearchYear)
                                .findAll()
                                .where()
                                .lessThan("month", secSearchMonth)
                                .findAll();
                        for (SpentMoney s : spents) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spentFinish = realm.where(SpentMoney.class)
                                .equalTo("year", secSearchYear)
                                .findAll()
                                .where()
                                .equalTo("month", secSearchMonth)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("day", secSearchDay)
                                .findAll();
                        for (SpentMoney s : spentFinish) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        for (int j = 0; j < list.size(); j++) {
                            sum = sum + list.get(j);
                        }
                        str = String.valueOf(sum);
                        thirdEdit.setText(str);
                        list.clear();
                    }
                }
                if (searchMonth < secSearchMonth && searchYear == secSearchYear) {//если надо получить расходы в период одного года, но разных месяцев
                    if (searchCategory.equals("Расходы по всем категориям")) {// по всем категориям
                        ArrayList<Double> list = new ArrayList<>();
                        double sum = 0;
                        String str = "";
                        RealmResults<SpentMoney> spentStart = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", searchMonth)
                                .findAll()
                                .where()
                                .greaterThanOrEqualTo("day", searchDay)
                                .findAll();
                        for (SpentMoney s : spentStart) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .greaterThan("month", searchMonth)
                                .lessThan("month", secSearchMonth)
                                .findAll();
                        for (SpentMoney s : spent) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        RealmResults<SpentMoney> spentFinish = realm.where(SpentMoney.class)
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", secSearchMonth)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("day", secSearchDay)
                                .findAll();
                        for (SpentMoney s : spentFinish) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            list.add(d);
                        }
                        for (int j = 0; j < list.size(); j++) {
                            sum = sum + list.get(j);
                        }
                        str = String.valueOf(sum);
                        thirdEdit.setText(str);
                        list.clear();
                    } else {//если надо получить расходы в период одного года, но разных месяцев в определённой категории
                        ArrayList<Double> listForCategory = new ArrayList<>();
                        double sumForCategory = 0;
                        String strForCategory = "";
                        RealmResults<SpentMoney> spentStart = realm.where(SpentMoney.class)
                                .equalTo("category.name", searchCategory)
                                .findAll()
                                .where()
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", searchMonth)
                                .findAll()
                                .where()
                                .greaterThanOrEqualTo("day", searchDay)
                                .findAll();
                        for (SpentMoney s : spentStart) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            listForCategory.add(d);
                        }
                        RealmResults<SpentMoney> spent = realm.where(SpentMoney.class)
                                .equalTo("category.name", searchCategory)
                                .findAll()
                                .where()
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .greaterThan("month", searchMonth)
                                .lessThan("month", secSearchMonth)
                                .findAll();
                        for (SpentMoney s : spent) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            listForCategory.add(d);
                        }
                        RealmResults<SpentMoney> spentFinish = realm.where(SpentMoney.class)
                                .equalTo("category.name", searchCategory)
                                .findAll()
                                .where()
                                .equalTo("year", searchYear)
                                .findAll()
                                .where()
                                .equalTo("month", secSearchMonth)
                                .findAll()
                                .where()
                                .lessThanOrEqualTo("day", secSearchDay)
                                .findAll();
                        for (SpentMoney s : spentFinish) {
                            String res = s.getSum();
                            double d = Double.valueOf(res);
                            listForCategory.add(d);
                        }
                        for (int j = 0; j < listForCategory.size(); j++) {
                            sumForCategory = sumForCategory + listForCategory.get(j);
                        }
                        strForCategory = String.valueOf(sumForCategory);
                        thirdEdit.setText(strForCategory);
                        listForCategory.clear();
                    }
                }
                if (searchMonth > secSearchMonth && searchYear == secSearchYear) {// если месяц больше, чем месяц конца периода, в пределах одного года
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите дату корректно!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (searchYear > secSearchYear) {//если год начала периода больше года кона периода
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите дату корректно!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //добавляем слушатель на кнопку перехода в расчёт доходов
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ThirdActivity.this, FoursActivity.class);
                startActivity(intent);
            }
        });

        //добавляем слушатель на кнопку перехода в кошелёк
        goCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ThirdActivity.this, CashActivity.class);
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

    //метод для получения и преобразования результатов спиннеров
    public void getSpinnerResult() {
        searchCategory = categorySpinner.getSelectedItem().toString();
        spinnerMonth = monthSpinner.getSelectedItem().toString();
        secSpinnerMonth = secMonthSpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();
        String secDay= secDaySpinner.getSelectedItem().toString();
        String secYear = secYearSpinner.getSelectedItem().toString();
        searchYear = Integer.valueOf(year);
        searchDay = Integer.valueOf(day);
        secSearchDay = Integer.valueOf(secDay);
        secSearchYear = Integer.valueOf(secYear);

        switch (spinnerMonth){
            case "Январь":
                searchMonth = 1;
                break;

            case "Февраль":
                searchMonth = 2;
                break;

            case "Март":
                searchMonth = 3;
                break;

            case "Апрель":
                searchMonth = 4;
                break;

            case "Май":
                searchMonth = 5;
                break;

            case "Июнь":
                searchMonth = 6;
                break;

            case "Июль":
                searchMonth = 7;
                break;

            case "Август":
                searchMonth = 8;
                break;

            case "Сентябрь":
                searchMonth = 9;
                break;

            case "Октябрь":
                searchMonth = 10;
                break;

            case "Ноябрь":
                searchMonth = 11;
                break;

            case "Декабрь":
                searchMonth = 12;
                break;
        }

        switch (secSpinnerMonth){
            case "Январь":
                secSearchMonth = 1;
                break;

            case "Февраль":
                secSearchMonth = 2;
                break;

            case "Март":
                secSearchMonth = 3;
                break;

            case "Апрель":
                secSearchMonth = 4;
                break;

            case "Май":
                secSearchMonth = 5;
                break;

            case "Июнь":
                secSearchMonth = 6;
                break;

            case "Июль":
                secSearchMonth = 7;
                break;

            case "Август":
                secSearchMonth = 8;
                break;

            case "Сентябрь":
                secSearchMonth = 9;
                break;

            case "Октябрь":
                secSearchMonth = 10;
                break;

            case "Ноябрь":
                secSearchMonth = 11;
                break;

            case "Декабрь":
                secSearchMonth = 12;
                break;
        }
    }
}
