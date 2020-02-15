package com.example.costsapp;

import io.realm.RealmObject;

public class BankCode extends RealmObject {
    private String code;

    public BankCode() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
