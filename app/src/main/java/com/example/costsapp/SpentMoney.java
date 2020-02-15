package com.example.costsapp;

import io.realm.RealmObject;

public class SpentMoney extends RealmObject {

    private String sum;
    private int day;
    private int month;
    private int year;
    private Category category;

    public SpentMoney() {
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getMouth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category c) {
        category = c;
    }

}
