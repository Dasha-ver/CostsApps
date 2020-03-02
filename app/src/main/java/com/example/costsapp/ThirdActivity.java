package com.example.costsapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
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
    String searchCategory;
    Button start;
    Button finish;
    Button goIncome;
    Button goCash;
    Button thirdBut;
    ImageButton home;
    EditText thirdEdit;
    Spinner mySpinner;
    Calendar c;
    DatePickerDialog dpd;
    ArrayList<ThirdPageSpinnerInfo> mySpinnerInfo;
    private  String codeResult;
    private Realm bankCodeRealm;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);

        mySpinner = findViewById(R.id.spinner_category);
        start = findViewById(R.id.third_but_start);
        finish = findViewById(R.id.third_but_end);
        thirdEdit = findViewById(R.id.third_edit);
        thirdBut = findViewById(R.id.third_button);
        home = findViewById(R.id.third_home);
        goIncome = findViewById(R.id.third_go_income);
        goCash = findViewById(R.id.third_go_cash);

        mySpinnerInfo = spinnerList();
        SpinnerAdapter Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, mySpinnerInfo);
        mySpinner.setAdapter(Adapter);

        Realm.init(getApplicationContext());
        bankCodeRealm = Realm.getDefaultInstance();
        realm = Realm.getDefaultInstance();

        showCode();//получаем банковский код
        thirdEdit.setHint(codeResult);

        //слушатель на кнопку вызова DatePickerDialog для ввода начальной даты периода для расчёта
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        searchDay = mDay;
                        searchMonth = mMonth +1;
                        searchYear = mYear;
                    }
                },year, month, day);
                dpd.show();
            }
        });

        //слушатель на кнопку вызова DatePickerDialog для ввода даты окончания периода для расчёта
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        secSearchDay = mDay;
                        secSearchMonth = mMonth +1;
                        secSearchYear = mYear;
                    }
                },year, month, day);
                dpd.show();
            }
        });

        //слушатель на спиннер категорий
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mySpinner.getSelectedItem() != null) {
                    searchCategory = mySpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //добавляем слушатель на кнопку расчёта расходов, получаем результаты из спиннера,
        //делаем выборку из БД, рассчитываем результат
        thirdBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        //слушатель на кнопку возврата домой
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        //слушатель на кнопку перехода в расчёт доходов
        goIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdActivity.this, FoursActivity.class);
                startActivity(intent);
            }
        });

        //слушатель на кнопку перехода в кошелёк
        goCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdActivity.this, CashActivity.class);
                startActivity(intent);
            }
        });
    }

    //метод для добавления информации в спиннер
    public ArrayList<ThirdPageSpinnerInfo> spinnerList() {
        ArrayList<ThirdPageSpinnerInfo> mySpinnerInfo = new ArrayList<ThirdPageSpinnerInfo>();
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Расходы по всем категориям", R.drawable.other));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Еда", R.drawable.food));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Одежда", R.drawable.clothes));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Электроника", R.drawable.electro));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Коммунальные платежи", R.drawable.comunalka));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Бытовая химия и косметика", R.drawable.cosmetics));
        mySpinnerInfo.add(new ThirdPageSpinnerInfo("Развлечения и образование", R.drawable.entertainment));

        return mySpinnerInfo;
    }

    //метод для получения банковского кода
    public void showCode() {
        RealmResults<BankCode> cods = bankCodeRealm.where(BankCode.class).findAll();
        for (BankCode b : cods) {
            codeResult = b.getCode();
        }
    }
}