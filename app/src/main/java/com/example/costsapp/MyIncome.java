package com.example.costsapp;

import io.realm.RealmObject;

public class MyIncome extends RealmObject {

    private String sum;
    private int iCounter;
    private int month;
    private int year;

    public MyIncome() {
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getiCounter() {
        return iCounter;
    }

    public void setiCounter(int iCounter) {
        this.iCounter = iCounter;
    }

    public int getMouth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}