package com.example.costsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

//адаптер для спиннера на странице старта

public class BankCodeSpinnerAdapter extends ArrayAdapter<BankCodeSpinnerInfo> {
    private Activity context;
    ArrayList<BankCodeSpinnerInfo> data = null;

    public BankCodeSpinnerAdapter(Activity context, int resource, ArrayList<BankCodeSpinnerInfo> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    //установка заголовка на спиннер
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.dropdawn_header_start_page, null);
        }
        return convertView;
    }

    //этот код выполняется, когда вы нажимаете на спиннер
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.start_spinner_layout, parent, false);
        }
        BankCodeSpinnerInfo item = data.get(position);
        // парсим данные с каждого объекта
        if (item != null) {
            TextView myCode = (TextView) row.findViewById(R.id.name_custom_spin_start);
            if (myCode != null)
                myCode.setText(item.getCodeName());
        }
        return row;
    }

}
