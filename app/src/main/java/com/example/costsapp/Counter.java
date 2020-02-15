package com.example.costsapp;

import io.realm.RealmObject;

public class Counter extends RealmObject {

    private int counter = 0;

    public Counter() {
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

