package com.example.costsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

//класс для добавления расходов

public class SecondActivity extends Activity implements GetMyItem {

    int numberOfColumns = 2;
    boolean flag = true;
    String amount;
    Calendar calendar;
    Costs c;
    RecyclerView rv;
    ImageButton butForView;
    Button enterAmount;
    Button controlIncome;
    Button goCash;
    Button changeIncome;
    Button controlButton;
    EditText generalExpenses;
    private int dayNow;
    private int monthNow;
    private int yearNow;
    private String codeResult;
    private MyAdapter a;
    private MyAdapterForLineView lineAdapter;
    private Realm realm;
    private Realm bankCodeRealm;
    private List<Costs> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        calendar = Calendar.getInstance();

        butForView = findViewById(R.id.image_button);
        enterAmount = findViewById(R.id.first_button);
        generalExpenses = findViewById(R.id.other_man);
        controlButton = findViewById(R.id.control_button);
        controlIncome = findViewById(R.id.control_income);
        rv = findViewById(R.id.my_recycler_view);
        goCash = findViewById(R.id.go_cash_for_sec);
        changeIncome = findViewById(R.id.go_income_for_sec);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();
        bankCodeRealm = Realm.getDefaultInstance();

        addCategory("Общие расходы");
        addCategory("Еда");
        addCategory("Бытовая химия и косметика");
        addCategory("Развлечения и образование");
        addCategory("Коммунальные платежи");
        addCategory("Одежда");
        addCategory("Электороника");

        showCode();//получаем банковский код
        generalExpenses.setHint(codeResult);

        //вызываем конструктор адаптера, через интерфейс получаем расходы введённые в категориях,
        //проверяем была ли первой введена точка или пустое ли поле, если нет -
        //получаем текущий день, месяц, год и добавляем сумму в выбранную категорию
        a = new MyAdapter(list, codeResult, new GetMyItem() {
            @Override
            public void getMyItem(View view, String value, String category) {
                if (value.contains(".")) {
                    int myInt = value.indexOf(".");
                    if (myInt == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Введите сумму корректно! Сумма не может начинаться с точки!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                        monthNow = calendar.get(Calendar.MONTH) + 1;
                        yearNow = calendar.get(Calendar.YEAR);
                        addSpentMoney(dayNow, monthNow, yearNow, value, category);
                    }
                }
                else if (value.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите сумму", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                    monthNow = calendar.get(Calendar.MONTH) + 1;
                    yearNow = calendar.get(Calendar.YEAR);
                    addSpentMoney(dayNow, monthNow, yearNow, value, category);
                }
            }
        });

        rv.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        rv.setAdapter(a);
        prepareCategoriesData();

        //добавляем слушатель на кнопку режима просмотра
        //вызываем конструктор адаптера, через интерфейс получаем расходы введённые в категориях,
        //проверяем была ли первой введена точка или пустое ли поле, если нет -
        //получаем текущий день, месяц, год и добавляем сумму в выбранную категорию
        butForView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag) {
                    butForView.setImageResource(R.drawable.ic_view_module_black_24dp);
                    lineAdapter = new MyAdapterForLineView(list, codeResult, new GetMyItem() {
                        @Override
                        public void getMyItem(View view, String value, String category) {
                            if (value.contains(".")) {
                                int myInt = value.indexOf(".");
                                if (myInt == 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Введите сумму корректно! Сумма не может начинаться с точки!", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                                    monthNow = calendar.get(Calendar.MONTH) + 1;
                                    yearNow = calendar.get(Calendar.YEAR);
                                    addSpentMoney(dayNow, monthNow, yearNow, value, category);
                                }
                            } else if (value.equals("")) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Введите сумму", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                                monthNow = calendar.get(Calendar.MONTH) + 1;
                                yearNow = calendar.get(Calendar.YEAR);
                                addSpentMoney(dayNow, monthNow, yearNow, value, category);
                            }
                        }
                    });
                    LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                    rv.setLayoutManager(lm);
                    rv.setItemAnimator(new DefaultItemAnimator());
                    rv.setAdapter(lineAdapter);
                } else {
                    butForView.setImageResource(R.drawable.ic_view_headline_black_24dp);
                    a = new MyAdapter(list, codeResult, new GetMyItem() {
                        @Override
                        public void getMyItem(View view, String value, String category) {
                            if (value.contains(".")) {
                                int myInt = value.indexOf(".");
                                if (myInt == 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Введите сумму корректно! Сумма не может начинаться с точки!", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                                    monthNow = calendar.get(Calendar.MONTH) + 1;
                                    yearNow = calendar.get(Calendar.YEAR);
                                    addSpentMoney(dayNow, monthNow, yearNow, value, category);
                                }
                            } else if (value.equals("")) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Введите сумму", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                                monthNow = calendar.get(Calendar.MONTH) + 1;
                                yearNow = calendar.get(Calendar.YEAR);
                                addSpentMoney(dayNow, monthNow, yearNow, value, category);
                            }
                        }
                    });
                    rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), numberOfColumns));
                    rv.setAdapter(a);
                }
                flag = !flag;
            }
        });

        //добавляем слушатель на кнопку ввода суммы в категорию "Общие расходы"
        //проверяем была ли первой введена точка или пустое ли поле, если нет -
        //получаем текущий день, месяц, год и добавляем сумму в выбранную категорию
        enterAmount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                amount = generalExpenses.getText().toString();
                if (amount.contains(".")) {
                    int myInt = amount.indexOf(".");
                    if (myInt == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Введите сумму корректно! Сумма не может начинаться с точки!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                        monthNow = calendar.get(Calendar.MONTH) + 1;
                        yearNow = calendar.get(Calendar.YEAR);
                        addSpentMoney(dayNow, monthNow, yearNow, amount, "Общие расходы");
                        generalExpenses.setText("");
                    }
                } else if (amount.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите сумму", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    dayNow = calendar.get(Calendar.DAY_OF_MONTH);
                    monthNow = calendar.get(Calendar.MONTH) + 1;
                    yearNow = calendar.get(Calendar.YEAR);
                    addSpentMoney(dayNow, monthNow, yearNow, amount, "Общие расходы");
                    generalExpenses.setText("");
                }
            }
        });

        //добавляем слушатель на кнопку перехода в кошелёк
        goCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, CashActivity.class);
                startActivity(intent);
            }
        });

        //добавляем слушатель на кнопку перехода в расчёт расходов
        controlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        //добавляем слушатель на кнопку перехода в расчёт доходов
        controlIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, FoursActivity.class);
                startActivity(intent);
            }
        });

        //добаляем слушатель на кнопку перехода в ввод доходов
        changeIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });
    }

    //метод для добавления категорий
    public void addCategory(String name) {
        Category myCategory = new Category();
        myCategory.setName(name);
        realm.beginTransaction();
        realm.copyToRealm(myCategory);
        realm.commitTransaction();
    }

    //метод для получения банковского кода
    public void showCode() {
        RealmResults<BankCode> cods = bankCodeRealm.where(BankCode.class).findAll();
        for (BankCode b : cods) {
            codeResult = b.getCode();
        }
    }

    //метод для добавления данных в адаптер
    private void prepareCategoriesData() {
        c = new Costs("Еда", R.drawable.food);
        list.add(c);

        c = new Costs("Бытовая химия и косметика", R.drawable.cosmetics);
        list.add(c);

        c = new Costs("Развлечения и образование", R.drawable.entertainment);
        list.add(c);

        c = new Costs("Коммунальные платежи", R.drawable.comunalka);
        list.add(c);

        c = new Costs("Одежда", R.drawable.clothes);
        list.add(c);

        c = new Costs("Электроника", R.drawable.electro);
        list.add(c);

        a.notifyDataSetChanged();
    }

    //метод для добавления расходов в БД
    public void addSpentMoney(int day, int month, int year, String sum, String category) {
        SpentMoney spent = new SpentMoney();
        spent.setSum(sum);
        spent.setDay(day);
        spent.setMonth(month);
        spent.setYear(year);
        Category myCategory = realm.where(Category.class).equalTo("name", category, Case.INSENSITIVE).findFirst();
        spent.setCategory(myCategory);
        realm.beginTransaction();
        realm.copyToRealm(spent);
        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void getMyItem(View view, String value, String category) {

    }
}
